package ch.unibas.medizin.osce.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import ch.unibas.medizin.osce.domain.*;
import ch.unibas.medizin.osce.shared.AssignmentTypes;
import ch.unibas.medizin.osce.shared.PostType;

/**
 * Generates an optimal timetable for an OSCE with respect to minimizing the time required.
 * According to the number of rooms and posts, the most reasonable number of parcours and break
 * posts within a rotation are chosen. As a consequence, the TimetableGenerator provides a
 * correct number of student time slots and therefore makes sure all students can be examined.
 * 
 * @author dk
 *
 */
public class TimetableGenerator {
	
	static Logger log = Logger.getLogger(TimetableGenerator.class);
	
	// automatically assign colors to parcours
	private static final String[] parcourColors = {"blue", "green", "red", "yellow", "purple", "orange", "turquise", "brown"};
	
	// automatically assign sequence labels
	private static final String[] sequences = {"A", "B", "C", "D", "E"};

	// insert a long in the middle of a rotation if time of rotation (breaks excluded) exceeds this threshold
	private static final int LONG_BREAK_MIDDLE_THRESHOLD = 200;
	
	private Osce osce;
	private OsceDay osceDayRef;				// reference to first OSCE day (will not be removed if scaffold is re-created)
	private int numberMinsDayMax;			// needed for optimization purposes (will be overwritten with "osceDay.getTimeEnd() - osceDay.getTimeStart()")
	private int numberSlotsUntilSPChange;	// maximum number of slots that can be placed without a short break in between (defined by "slotsUntilChange" of most difficult role)
	private int numberDays;					// number of required days
	private int numberStudents;				// number of students to base schedule on
	private int numberPosts;				// number of posts defined by the OSCE
	private int numberBreakPosts;			// number of break posts
	private int postLength;					// length of a single post (in minutes)
	private int rotationsPerDay;			// max number of rotations per day
	private int numberParcours;				// number of parallel running parcours
	private List<Integer>[] rotations;		// information on break posts on individual parcours
	private List<Integer> rotationsByDay = new ArrayList<Integer>();	// number of rotations for each day
	private int timeNeeded;					// total time required to perform the OSCE (without breaks)
	private List<Integer> timeNeededByDay = new ArrayList<Integer>();	// time need per individual day (required to calculate exact end-time of each day)

	private Set<Assignment> assignments;

	private int simpatSequenceNumber;

	private long[] simAssLastId;
	
	/**
	 * Calculate an optimal timetable with respect to the given parameters by trying and comparing
	 * different numbers of courses and break-posts
	 * 
	 * @param osce
	 * @return optimal timetable
	 */
	public static TimetableGenerator getOptimalSolution(Osce osce) {
		log.info("calculating optimal solution for osce " + osce.getId());
		
		try {
			checkOsce(osce);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int numberPostsWithRoom = osce.numberPostsWithRooms();
		
		// max number of courses (decrease while looking for optimum)
		int numberParcoursMax = osce.getNumberRooms() / numberPostsWithRoom;
		
		TimetableGenerator ttGen;
		TimetableGenerator optGen = null;
		double optTimeNeeded = Double.POSITIVE_INFINITY;
		
		for(int nParcours = numberParcoursMax; nParcours >= 1; nParcours--) {
			
			// iterate until (numberPosts - number of posts with post_type=BREAK).
			// NOTE: a number of breaks equal to numberPosts would result in an additional rotation.
			for(int breakPosts = 0; breakPosts <= 0; breakPosts++) {
				ttGen = new TimetableGenerator(osce, breakPosts, nParcours);
				ttGen.calcAddBreakPosts();
				ttGen.calcTimeNeeded();
				
				// replace optimal timetable with current timetable if overall time is shorter
				if(ttGen.getTimeNeeded() < optTimeNeeded) {
					optGen = ttGen;
					optTimeNeeded = optGen.getTimeNeeded();
				}
			}
		}
		
		log.info("optimal timetable");
		log.info("===============================");
		optGen.calcTimeNeeded();
		
		return optGen;
	}
	
	@SuppressWarnings("unchecked")
	public TimetableGenerator(Osce osce, int nBreakPosts, int nParcours) {
		
		this.osce = osce;
		numberStudents = osce.getMaxNumberStudents();
		numberPosts = osce.getOscePostBlueprints().size();
		postLength = osce.getPostLength();
		numberSlotsUntilSPChange = osce.slotsOfMostDifficultRole();
		numberBreakPosts = nBreakPosts;
		numberParcours = nParcours;

		osceDayRef = osce.getOsce_days().iterator().next();
		numberMinsDayMax = (int) (osceDayRef.getTimeEnd().getTime() - osceDayRef.getTimeStart().getTime()) / (60 * 1000);
		rotationsPerDay = 1;
		
		rotations = new ArrayList[nParcours];
		for(int i = 0; i < nParcours; i++) {
			rotations[i] = new ArrayList<Integer>();
		}
	}
	
	/**
	 * Calculate break posts for each rotation at each parcour.
	 */
	private void calcAddBreakPosts() {
		// add break posts to regular posts
		int nPosts = numberPosts + numberBreakPosts;
		
		int maxRotations = numberStudents / (nPosts * numberParcours);
		
//		log.info(numberPosts + " " + numberBreakPosts);
//		log.info("nPosts: "+nPosts);
//		log.info("maxRotations: "+maxRotations);
		
		// init all rotations of parcours with number of break posts
		for(int i = 0; i < numberParcours; i++) {
			for(int j = 0; j < maxRotations; j++) {
				rotations[i].add(numberBreakPosts);
			}
		}
		
		// remaining number of students that have no slot yet (slots are created by adding break posts)
		int diff = numberStudents - maxRotations * (nPosts * numberParcours);
		
		// each student has a slot - we are happy
		if(diff == 0) {
			return;
		}
		
		// add additional break post to individual rotations
		int numberPostsNew = numberBreakPosts + 1;
		
		/**
		 * add additional break posts according to the following rules:
		 * 1. add break post to first rotation of each parcour
		 * 2. add break post to second rotation of each parcour
		 * ...
		 * until all additional slots have been created
		 */
		int parcourIndex = 0;
		int rotationIndex = 0;
		for(int i = 0; i < diff; i++) {
			rotations[parcourIndex].set(rotationIndex, numberPostsNew);
//			System.out.println(parcourIndex + " - " + rotations[parcourIndex].get(rotationIndex));

			if(parcourIndex >= rotations.length - 1) {
				parcourIndex = 0;

				if(rotationIndex >= maxRotations - 1) {
					rotationIndex = 0;
				} else {
					rotationIndex++;
				}
			} else {
				parcourIndex++;
			}
		}
		
//		rotationsPerDay = (numberMinsDayMax - osce.getLunchBreak()) /
//				(numberPosts * postLength + (numberPosts - 1) * osce.getShortBreak() +
//				(numberPosts * postLength > LONG_BREAK_MIDDLE_THRESHOLD ? osce.getLongBreak() : 0) +
//				osce.getLongBreak() / numberSlotsUntilSPChange +
//				(numberPosts * postLength > LONG_BREAK_MIDDLE_THRESHOLD ? 0 : osce.getMiddleBreak()));
		rotationsPerDay = (numberMinsDayMax - osce.getLunchBreak()) / (numberPosts * postLength + (numberPosts - 1) * osce.getShortBreak());
	}
	
	public void calcTimeNeeded() {
		boolean numberDaysVerified = false;
		
		while(numberDaysVerified == false) {
			
			numberDays = (int) Math.ceil((double) rotations[0].size() / (double) rotationsPerDay);
			log.info("calculating time needed (" + numberDays + " day(s)");
			
			int rotationsMax = rotationsPerDay > rotations[0].size() ? rotations[0].size() : rotationsPerDay;
			
			rotationsByDay.clear();
			timeNeededByDay.clear();
			timeNeeded = 0;
			int slotsSinceLastSimpatChange = 0;
			
			for(int i = 0; i < numberDays; i++) {
				if(i == numberDays - 1 && numberDays > 1)
					rotationsByDay.add(i, (rotations[0].size() % rotationsPerDay > 0 ? rotations[0].size() % rotationsPerDay : rotationsPerDay));
				else
					rotationsByDay.add(i, rotationsMax);
			}
			
			// days
			for(int i = 0; i < numberDays; i++) {
				int timeNeededCurrentDay = 0;
				
				slotsSinceLastSimpatChange = 0;
				log.info("day " + i + " (rotations: " + rotationsByDay.get(i) + ") / rotationsMax: " + rotationsMax);
				
				// rotations
				for(int j = (i * rotationsMax); j < (i * rotationsMax + rotationsByDay.get(i)); j++) {
					int numberBreakPostsThisRotation = rotations[0].get(j);
					
					// add break posts to regular posts
					int nPostsGeneral = numberPosts + numberBreakPosts;
					int nPostsThisRotation = nPostsGeneral + numberBreakPostsThisRotation;
					
					// index where a SP needs to be changed during the rotation (in the middle if there is
					// only one change, after number of slots of most complicated role otherwise)
					int changeIndex = nPostsThisRotation / numberSlotsUntilSPChange > 1 ? numberSlotsUntilSPChange : nPostsThisRotation / 2 + 1;
					
					boolean longBreakInRotationHalf = nPostsThisRotation * osce.getPostLength() > LONG_BREAK_MIDDLE_THRESHOLD;
					
					log.info("  rotation " + j + " (breakposts: " + numberBreakPostsThisRotation + ") - start: " + timeNeededCurrentDay);
					
					// posts
					for(int k = 0; k < nPostsThisRotation; k++) {
						boolean halfTimeSlots = k == nPostsThisRotation / 2 - 1;
						
						timeNeededCurrentDay += postLength;
						
						if(longBreakInRotationHalf && halfTimeSlots) {
							slotsSinceLastSimpatChange = 0;
							timeNeededCurrentDay += osce.getLongBreak();
						} else {
							if(simpatChangeWithinSlots(slotsSinceLastSimpatChange) && k % changeIndex == changeIndex - 1) {
								slotsSinceLastSimpatChange = 0;
								timeNeededCurrentDay += osce.getShortBreakSimpatChange();
							} else {
								if(k < nPostsThisRotation - 1)
									timeNeededCurrentDay += osce.getShortBreak();
							}
						}
						
						slotsSinceLastSimpatChange++;
					}
					
					log.info("  rotation " + j + " end: " + timeNeededCurrentDay);
					
					// additional breaks
					if((j % rotationsMax) < rotationsByDay.get(i) - 1) {
						if(lunchBreakNeeded((j + 1) % rotationsMax)) {
							slotsSinceLastSimpatChange = 0;
							timeNeededCurrentDay += osce.getLunchBreak();
							log.info("  lunch break");
						} else if(simpatChangeWithinSlots(slotsSinceLastSimpatChange + nPostsGeneral + rotations[0].get(j + 1)) || longBreakInRotationHalf) {
							slotsSinceLastSimpatChange = 0;
							timeNeededCurrentDay += osce.getLongBreak();
							log.info("  long break");
						} else {
							timeNeededCurrentDay += osce.getMiddleBreak();
							log.info("  middle break");
						}
					}
				}
				
				timeNeededByDay.add(i, timeNeededCurrentDay);
				
				timeNeeded += timeNeededCurrentDay;
			}
			
			// see whether calculated time fits into the time available of first day - do another round if not
			int numberDays2 = (int) Math.ceil((double) timeNeeded / (double) numberMinsDayMax);
			if(numberMinsDayMax < timeNeededByDay.get(0)) {
				//numberDays = numberDays2;
				rotationsPerDay--;
				log.warn("do another round... | numberDays = " + numberDays + ", numberDays2 = " + numberDays2 + ", numberMinsDayMax = " + numberMinsDayMax + ", timeNeededByDay.get(0) = " + timeNeededByDay.get(0) + ", timeNeeded = " + timeNeeded);
			} else {
				numberDaysVerified = true;
			}
		}
	}
	
	private double getTimeNeeded() {
		return timeNeeded;
	}
	
	/**
	 * Print information on timetable setting
	 */
	public String toString() {
		
		OsceDay osceDay = osce.getOsce_days().iterator().next();
		
		StringBuffer sb = new StringBuffer();
		sb.append("======================================\n");
		sb.append("OSCE -");
		sb.append(" students: " + numberStudents);
		sb.append(" posts: " + numberPosts);
		sb.append(" post length (min): " + postLength + "\n");
		sb.append("max number of minutes per day: " + numberMinsDayMax + "\n");
		sb.append("number of required days: " + numberDays + "\n");
		sb.append("max number of rotations per parcour (per day): " + rotationsPerDay + "\n");
		sb.append("start time first day: "+osceDay.getTimeStart()+"\n");
		sb.append("end time first day: "+osceDay.getTimeEnd()+"\n");
		sb.append("--------------------------------------\n");

		sb.append("number of parcours: " + numberParcours + "\n");
		
		int slotsTotal = 0;
		for(int i = 0; i < rotations.length; i++) {
			ArrayList<Integer> rotationX = (ArrayList<Integer>) rotations[i];
			
			sb.append("    number of rotations for parcour " + (i + 1) + ": " + rotationX.size() + "\n");
			
			if(rotationX.size() > 0) {
				int courseSlotsTotal = 0;
				Iterator<Integer> it = rotationX.iterator();
				sb.append("        ");
				while(it.hasNext()) {
					int slots = it.next();
					sb.append(slots + " ");
					courseSlotsTotal += numberPosts + slots;
				}
				sb.append("\n        total number of slots in parcour: " + courseSlotsTotal +  "\n");
				
				slotsTotal += courseSlotsTotal;
			}
		}
		
		sb.append("total slots: " + slotsTotal + "\n");
		sb.append("timeNeeded: " + timeNeeded + "\n");
		
		OsceDay day0 = osce.getOsce_days().iterator().next();
		sb.append(" - from: " + day0.getTimeStart() + " to " + dateAddMin(day0.getTimeStart(), timeNeeded) + "\n");
		sb.append("======================================\n\n");
		
		return sb.toString();
	}
	
	/**
	 * Setup OSCE days and corresponding sequences, parcours and posts.
	 * Sequences are created according to the following scheme:
	 * 1 OSCE day: sequence A for the first half of the rotations, sequence B for the second half
	 * > 1 OSCE day: one sequence for each day
	 * 
	 */
	public void createScaffold() {
		log.info("remove old scaffold");
		// remove old scaffold if existing (and re-create first OSCE day
		removeOldScaffold();
		
		log.info("old scaffold removed");
		
		List<OsceDay> days = insertOsceDays();
		List<OsceSequence> osceSequences = new ArrayList<OsceSequence>();
		
		// only one day --> seq A in the morning, seq B in the afternoon
		if(days.size() == 1) {
			OsceDay osceDay = days.iterator().next();
			
			log.info("rotations for day 0:" + rotationsByDay.get(0));
			
			int[] rotSeq = new int[2];
			rotSeq[0] = rotationsByDay.get(0) / 2;
			rotSeq[1] = rotationsByDay.get(0) - rotSeq[0];
			
			// create sequence for each half day
			for(int i = 0; i < 2; i++) {
				// insert sequence
				OsceSequence seq = new OsceSequence();
				seq.setLabel(sequences[i]);
				seq.setNumberRotation(rotSeq[i]);
				seq.setOsceDay(osceDay);
				
				// insert parcours
				List<Course> parcours = insertParcoursForSequence(seq);
				
				// insert posts
				List<OscePost> posts = insertPostsForSequence(seq);
				
				seq.setCourses(parcours);
				seq.setOscePosts(posts);
				
				seq.persist();
				
				// insert osce_post_rooms
				insertOscePostRoomsForParcoursAndPosts(parcours, posts);
				
				osceSequences.add(seq);
			}
			
			osceDay.setOsceSequences(osceSequences);
		} else { // multiple days --> one sequence for each day
			Iterator<OsceDay> it = days.iterator();
			int i = 0;
			while (it.hasNext()) {
				OsceDay osceDay = (OsceDay) it.next();
				
				// insert sequence
				OsceSequence seq = new OsceSequence();
				seq.setLabel(sequences[i]);
				seq.setNumberRotation(rotationsByDay.get(i));
				seq.setOsceDay(osceDay);
				
				// insert parcours
				List<Course> parcours = insertParcoursForSequence(seq);
				
				// insert posts
				List<OscePost> posts = insertPostsForSequence(seq);
				
				seq.setCourses(parcours);
				seq.setOscePosts(posts);
				
				seq.persist();
				
				// insert osce_post_rooms
				insertOscePostRoomsForParcoursAndPosts(parcours, posts);
				
				osceSequences.add(seq);
				
				i++;
				
				osceDay.setOsceSequences(osceSequences);
			}
		}
	}

	/**
	 * Remove old calculated scaffold (when OsceStatus is changed from GENERATED to BLUEPRINT again)
	 * 
	 */
	@SuppressWarnings("deprecation")
	private void removeOldScaffold() {
		// temp variables for first OSCE day (used to re-create OSCE day after deleting whole scaffold)
//		Date dayRefTimeStart = osceDayRef.getTimeStart();
//		Date dayRefTimeEnd = osceDayRef.getTimeEnd();
		
		//SPEC[		
		log.info("removeOldScaffold start");
		Iterator<OsceDay> itDay = osce.getOsce_days().iterator();		
		OsceDay firstDay = itDay.next();							
		OsceDay removeDay = null;
		log.info("First Day : " + firstDay.getId() + " : " + firstDay.getOsceDate().toLocaleString());
		while(itDay.hasNext())
		{			
			OsceDay nextDay = itDay.next();
			log.info("Next Day : " + nextDay.getId() + " : " + nextDay.getOsceDate().toLocaleString());
			if(firstDay.getOsceDate().after(nextDay.getOsceDate()))
			{
				log.info("First Day after Next Day");
				if(firstDay.getPatientInSemesters() == null || firstDay.getPatientInSemesters().size()==0)
				{
					removeDay = firstDay;					
				}
				
				firstDay = nextDay;
			}
			else
			{
				log.info("Next Day after First Day");
				if(nextDay.getPatientInSemesters() == null || nextDay.getPatientInSemesters().size()==0)
				{
					log.info("Next Day going to be deleted");
					removeDay = nextDay;
					log.info("Next Day is deleted");					
				}
			}
			
			log.info("remove Day going to be deleted");
			osce.getOsce_days().remove(removeDay);
			removeDay.remove();			
			log.info("Next Day is deleted");
			
			
		}
		
		
		if(firstDay != null)
		{
			log.info("First Day not null : " + firstDay.getId() + " : " + firstDay.getOsceDate().toLocaleString());						
			List<OsceSequence> setOsceSeq = firstDay.getOsceSequences();
			Iterator<OsceSequence> itOsceSeq = setOsceSeq.iterator();
			//firstDay.getOsceSequences().removeAll(setOsceSeq);			
			while(itOsceSeq.hasNext())
			{				
				OsceSequence osceSequence = itOsceSeq.next();
				log.info("Removing osce sequence : " + osceSequence.getId() );
				firstDay.getOsceSequences().remove(osceSequence);
				osceSequence.remove();
			}
			
			osceDayRef = firstDay;
		}
//		else
//		{
//			// re-create new OSCE day
//			OsceDay newDay = new OsceDay();
//			newDay.setOsce(osce);
//			newDay.setOsceDate(dayRefTimeStart);
//			newDay.setTimeStart(dayRefTimeStart);
//			newDay.setTimeEnd(dayRefTimeEnd);
//			newDay.persist();
//			osceDayRef = newDay;
//		}
		
		//SPEC]
	}

	/**
	 * Create all parcours for a sequence (number of parcours was calculated,
	 * number of sequences is given by number of days).
	 * 
	 * @param seq
	 * @return
	 */
	private List<Course> insertParcoursForSequence(OsceSequence seq) {
		List<Course> parcours = new ArrayList<Course>();
		
		for(int j = 0; j < numberParcours; j++) {
			Course c = new Course();
			c.setColor(parcourColors[j]);
			c.setOsce(osce);
			c.setOsceSequence(seq);
			parcours.add(c);
		}
		
		return parcours;
	}

	/**
	 * Create all posts for a sequence (transcribe all OscePostBlueprint into OscePost)
	 * 
	 * @param seq
	 * @return
	 */
	private List<OscePost> insertPostsForSequence(OsceSequence seq) {
		List<OscePost> posts = new ArrayList<OscePost>();
		
		OscePost current = null;
		
		Iterator<OscePostBlueprint> itBP = osce.getOscePostBlueprints().iterator();
		while (itBP.hasNext()) {
			OscePostBlueprint oscePostBlueprint = (OscePostBlueprint) itBP.next();
			
			current = new OscePost();
			current.setOscePostBlueprint(oscePostBlueprint);
			current.setOsceSequence(seq);
			current.setSequenceNumber(oscePostBlueprint.getSequenceNumber());
			
			posts.add(current);
		}
		
		return posts;
	}
	
	/**
	 * Insert the number of needed days for this OSCE into the database.
	 * 
	 * @return
	 */
	private List<OsceDay> insertOsceDays() {
		List<OsceDay> days = new ArrayList<OsceDay>();
		
		OsceDay day0 = osce.getOsce_days().iterator().next();
		days.add(day0);
		
		Calendar dayCal = Calendar.getInstance();
		dayCal.setTime(day0.getTimeStart());
		
		int rotationsMax = rotationsPerDay > rotations[0].size() ? rotations[0].size() : rotationsPerDay;
		
		rotationsByDay.set(0, rotationsMax);
		
		// number of rotations we still have to assign to further days
		int rotationsLeft = rotations[0].size() - rotationsMax;
		
		if(numberDays > 1 && rotationsLeft > 0) {
			// create new days and corresponding sequences and parcours
			for(int i = 1; i < numberDays; i++) {
				rotationsByDay.set(i, rotationsLeft);
				dayCal.add(Calendar.DATE, i);
				
				OsceDay day = new OsceDay();
				day.setOsce(osce);
				day.setOsceDate(dayCal.getTime());
				day.setTimeStart(dayCal.getTime());
				day.setTimeEnd(dateAddMin(dayCal.getTime(), timeNeededByDay.get(i)));
				
				day.persist();
				days.add(day);
				
				rotationsLeft -= rotationsPerDay;
			}
		}
		
		day0.setTimeEnd(dateAddMin(day0.getTimeStart(), (long) timeNeededByDay.get(0)));
		day0.flush();
		
		return days;
	}
	
	
	private Set<OscePostRoom> insertOscePostRoomsForParcoursAndPosts(List<Course> parcours, List<OscePost> posts) {
		Set<OscePostRoom> oscePostRooms = new HashSet<OscePostRoom>();
		
		Iterator<Course> itParcour = parcours.iterator();
		while (itParcour.hasNext()) {
			Course parcour = (Course) itParcour.next();
			
			Iterator<OscePost> itPost = posts.iterator();
			while (itPost.hasNext()) {
				OscePost oscePost = (OscePost) itPost.next();
				
				OscePostRoom opr = new OscePostRoom();
				opr.setCourse(parcour);
				opr.setOscePost(oscePost);
				opr.setRoom(null);
				
				opr.persist();
			}
		}
		
		return oscePostRooms;
	}
	
	/**
	 * Create assignments for students, SimPats and examiners.
	 * @return set containing all assignments
	 */
	public Set<Assignment> createAssignments() {
		assignments = new HashSet<Assignment>();
		
		List<OsceDay> days = new ArrayList<OsceDay>(osce.getOsce_days());
		
		// get posts (sorted by sequenceNumber)
		List<OscePostBlueprint> posts = osce.getOscePostBlueprints();
		
		// total number of rotations is split among sequences, rotationOffset will be incremented
		// by the number of rotations of a sequence after handling it.
		// (e.g. and 1 osce-day has 2 sequences with 4 and 3 rotations, rotationOffset will then
		// first be 0 and 4 after first iteration of sequences)
		int rotationOffset = 0;
		
		int studentIndexLowerBound = 1;
		
		// iterate over all days
		Iterator<OsceDay> itDays = days.iterator();
		while (itDays.hasNext()) {
			OsceDay osceDay = (OsceDay) itDays.next();
			
			log.info("day " + osceDay.getOsceDate() + " started");
			
			Date time = osceDay.getTimeStart();
			
			Date sequenceStartTime = time;
			
			// iterate over sequences ("A", "B", etc.)
			Iterator<OsceSequence> itSeq = osceDay.getOsceSequences().iterator();
			while (itSeq.hasNext()) {
				OsceSequence osceSequence = (OsceSequence) itSeq.next();
				
				log.info("sequence " + osceSequence.getLabel() + " started");
				
				// number of rotations for current sequence (valid for all parcours in this sequence)
				int numberRotations = osceSequence.getNumberRotation();
				
				Date parcourStartTime = sequenceStartTime;
				
				// iterate over all parcours (red, green, blue, etc.)
				Iterator<Course> itParc = osceSequence.getCourses().iterator();
				int parcourIndex = 0;
				while (itParc.hasNext()) {
					Course course = (Course) itParc.next();
					
					log.info("parcour " + course.getColor() + " started");
					
					int postsSinceSimpatChange = 0;
					
					simpatSequenceNumber = 1;
					
					Date rotationStartTime = parcourStartTime;
					
					simAssLastId = new long[numberPosts];
					
					// flag to define whether the assignments of ANAMNESIS_THERAPY have to be switched in the end (to avoid
					// time overlapping)
					boolean earlyStartFirst = false;
					
					// create assignments for rotations (given by sequence)
					for(int currRotationNumber = rotationOffset; currRotationNumber < (rotationOffset + numberRotations); currRotationNumber++) {
						int numberBreakPosts = rotations[parcourIndex].get(currRotationNumber);
						int numberSlotsTotal = posts.size() + numberBreakPosts;
						
						Set<Assignment> assThisRotation = new HashSet<Assignment>();
						
						log.info("rotation " + currRotationNumber + " started - rotation "+(currRotationNumber - rotationOffset + 1)+"/"+numberRotations+" (total rotation: "+currRotationNumber+"/"+(rotationOffset + numberRotations - 1)+"), numberBreakPosts: "+numberBreakPosts+", numberSlotsTotal: "+numberSlotsTotal);
						
						// get max slots from current rotation (defines when next rotation is about to
						// start since rotations need to start at the same time!)
						int maxPostsCurrentRotation = numberPosts + getMaxBreakPostsCurrentRotation(osceSequence.getCourses(), currRotationNumber);
						
						boolean firstRotation = currRotationNumber == rotationOffset;
						boolean halfRotations = currRotationNumber == (rotationOffset + numberRotations) / 2 - 1;
						boolean lastRotation = currRotationNumber == (rotationOffset + numberRotations - 1);
						boolean changeSimpatDuringRotation = simpatChangeWithinSlots(postsSinceSimpatChange + numberSlotsTotal);
						
						// insert long break in the middle of a rotation if the time of all posts exceeds some threshold
						boolean longBreakInRotationHalf = numberSlotsTotal * osce.getPostLength() > LONG_BREAK_MIDDLE_THRESHOLD;
						
						// index where a SP needs to be changed during the rotation (in the middle if there is
						// only one change, after number of slots of most complicated role otherwise)
						int changeIndex = numberSlotsTotal / numberSlotsUntilSPChange > 1 ? numberSlotsUntilSPChange : numberSlotsTotal / 2 + 1;
						
						// calculate slots that this and next rotation have and check whether a SP change after the rotation is necessary
						int numberSlotsThisAndNextRotation = numberSlotsTotal + numberPosts;
						if(currRotationNumber < (rotationOffset + numberRotations) - 1) {
							numberSlotsThisAndNextRotation += rotations[parcourIndex].get(currRotationNumber + 1);
						}
						boolean changeSimpatAfterRotation = simpatChangeWithinSlots(postsSinceSimpatChange + numberSlotsThisAndNextRotation);
						
						Date nextRotationStartTime = null;
						
						// iterate through all posts as many times as there are posts
						// (in the end, we want to have a n*n matrix where n denotes the number of posts in this OSCE)
						for(int i = 0; i < numberSlotsTotal; i++) {
							OscePostBlueprint postBP = null;
							PostType postType = null;
							OscePost post = null;
							OscePostRoom oscePR = null;
							
							log.info("post " + i + " started");
							
							if(i < posts.size()) {
								postBP = posts.get(i);
								postType = postBP.getPostType();
								post = OscePost.findOscePostsByOscePostBlueprintAndOsceSequence(postBP, osceSequence).getSingleResult();
								
								if(!postType.equals(PostType.BREAK))
									oscePR = OscePostRoom.firstOscePostRoomByCourseAndOscePost(course, post, (i + 1));
							}
							
							// reset to the point in time where the rotation starts (necessary since
							// we increase the time while going through all posts of a rotation)
							time = rotationStartTime;
							
							// post must be a possible start (PAUSE - which is at the end is always a possible start)
							// fill slots for one student in current rotation
							int studentIndexOffset = (i + 1);
							
							for(int j = 0; j < numberSlotsTotal; j++) {
								
								boolean firstTimeSlot = j == 0;
								boolean halfTimeSlots = j == numberSlotsTotal / 2 - 1;
								boolean lastTimeSlot = j == numberSlotsTotal - 1;
								
								// calculate student index for current time slot j in post i
								// for a circuit of 4 posts and one break post, the indexes should be as follows:
								// Posts:		1 2 3 4 break
								// Slot 1:		1 2 3 4 5
								// Slot 2:		5 1 2 3 4
								// Slot 3:		4 5 1 2 3
								// Slot 4:		3 4 5 1 2
								// Slot 5:		2 3 4 5 1
								// NOTE: student indices for first part of PREPARATION are the same as for second part, but with early start
								int studentIndex = studentIndexLowerBound + (numberSlotsTotal + studentIndexOffset - (j + 1)) % numberSlotsTotal;
								if(postBP != null) {
									if(postType.equals(PostType.PREPARATION) && postBP.getIsFirstPart()) {
										studentIndex = studentIndexLowerBound + (numberSlotsTotal + studentIndexOffset - j) % numberSlotsTotal;
									} else if(postType.equals(PostType.ANAMNESIS_THERAPY) && earlyStartFirst) {
										if(postBP.getIsFirstPart()) {
											studentIndex = studentIndexLowerBound + (numberSlotsTotal + studentIndexOffset - j) % numberSlotsTotal;
										} else {
											studentIndex = studentIndexLowerBound + (numberSlotsTotal + studentIndexOffset - (j + 2)) % numberSlotsTotal;
										}
									}
								}
								
								Date startTime = time;
								
								if(postBP != null) {
									// early start
									boolean isAnamnesisTherapy = postType.equals(PostType.ANAMNESIS_THERAPY) &&
											((!earlyStartFirst && !postBP.getIsFirstPart()) ||
											(earlyStartFirst && postBP.getIsFirstPart()));
									boolean isPreparation = postType.equals(PostType.PREPARATION) && postBP.getIsFirstPart();
									if(firstTimeSlot && (isAnamnesisTherapy || isPreparation)) {
										startTime = dateSubtractMin(startTime, osce.getPostLength());
										
										if(changeSimpatDuringRotation && (j % changeIndex == changeIndex - 1)) {
											startTime = dateSubtractMin(startTime, osce.getShortBreakSimpatChange());
										} else {
											startTime = dateSubtractMin(startTime, osce.getShortBreak());
										}
									}
								}
								
								Date endTime = dateAddMin(startTime, osce.getPostLength());
								
								if(currRotationNumber < 1)
									log.info("\t student index " + studentIndex + " for post " + i);

								assThisRotation.add(createStudentAssignment(osceDay, oscePR, studentIndex, startTime, endTime));
								
								// ANAMNESIS_THERAPY --> double length in same room BUT different role for second part!
								if(postBP != null && postType.equals(PostType.ANAMNESIS_THERAPY)) {
									// WARNING! only short break (no shortBreakSimpatChange) is handled here. This is on
									// purpose, since SP change during a double-post might cause big trouble!
									Date startTime2 = dateAddMin(endTime, osce.getShortBreak());
									Date endTime2 = dateAddMin(startTime2, osce.getPostLength());
									
									OscePostRoom oscePRAlt = OscePostRoom.altOscePostRoom(oscePR);
									
									assThisRotation.add(createStudentAssignment(osceDay, oscePRAlt, studentIndex, startTime2, endTime2));
									
									endTime = endTime2;
								}
								
								log.info("added assignment for student " + studentIndex);

								if(postBP != null && postType.equals(PostType.ANAMNESIS_THERAPY) ) {
									if(numberSlotsTotal % 2 == 1) {
										if((!earlyStartFirst && !postBP.getIsFirstPart()) || (earlyStartFirst && postBP.getIsFirstPart())) {
											lastTimeSlot = j == numberSlotsTotal - 1;
										} else {
											lastTimeSlot = j == numberSlotsTotal - 3;
										}
									} else {
										lastTimeSlot = j == numberSlotsTotal - 2;
									}
								}
								
								log.info("calculated lastTimeSlot");
								
								// handle SP assignments
								if(post != null && post.getStandardizedRole() != null && post.requiresSimpat()) {
									// create first SP slot
									if(firstRotation && firstTimeSlot) {
										createSPAssignment(i, osceDay, startTime, oscePR);
										log.warn("create SP assignment for post " + i + " " + debugTime(startTime));
									}
									// finalize last SP slot
									if(lastRotation && lastTimeSlot) {
										finalizeSPAssignment(i, endTime);
										log.warn("finalize SP assignment for post " + i + " " + debugTime(endTime));
									}
								}
								
								log.info("handled SP assignments");
								
								if(postBP != null && postType.equals(PostType.ANAMNESIS_THERAPY)) {
									// skip next time-slot
									j++;
								}
								
								if(!lastTimeSlot) {
									// add short break between posts
									// if, for example, there is a maximum of 8 posts and the most difficult role-topic needs a SimPat change after
									// 5 students, there is only one possible SimPat change during a rotation (best placed in the middle)
									if(changeSimpatDuringRotation && (j % changeIndex == changeIndex - 1)) {
										Date endTimeNew;
										if(halfTimeSlots && longBreakInRotationHalf)
											endTimeNew = dateAddMin(endTime, osce.getLongBreak());
										else
											endTimeNew = dateAddMin(endTime, osce.getShortBreakSimpatChange());
										
										if(post != null && post.getStandardizedRole() != null && post.requiresSimpat()) {
											changeSP(i, osceDay, endTime, endTimeNew, oscePR);
											log.warn("change SP assignment for post " + i + " " + debugTime(endTime) + " / " + debugTime(endTimeNew) + " (during rotation)");
										}
										
										endTime = endTimeNew;
									} else {
										if(halfTimeSlots && longBreakInRotationHalf)
											endTime = dateAddMin(endTime, osce.getLongBreak());
										else
											endTime = dateAddMin(endTime, osce.getShortBreak());
									}
								} else {
									if((!lastRotation && changeSimpatAfterRotation) || (osceDay.getOsceSequences().size() == 1 && halfRotations)) {
										
										if(post != null && post.getStandardizedRole() != null && post.requiresSimpat()) {
											Date endTimeOld = endTime;
											Date startTimeNew = dateAddMin(endTimeOld, osce.getLongBreak());
											
											// fix new start time for SP (next rotation) when lastTimeSlot and switch of assignments will occur
											if(postBP != null && postType.equals(PostType.ANAMNESIS_THERAPY) && numberSlotsTotal % 2 == 1 &&
													(!earlyStartFirst && postBP.getIsFirstPart() || earlyStartFirst && !postBP.getIsFirstPart())) {
												startTimeNew = dateAddMin(startTimeNew, osce.getShortBreakSimpatChange() - osce.getShortBreak());
											}
											
											changeSP(i, osceDay, endTimeOld, startTimeNew, oscePR);
											log.warn("change SP assignment for post " + i + " " + debugTime(endTime) + " / " + debugTime(startTimeNew) + " (after rotation)");
										}
									}
								}
								
								time = endTime;
								
								// leave loop after last time slot (otherwise we would have too many time slots for double posts)
								if(lastTimeSlot) {
									break;
								}
							}
							// STUDENTS END
							
							// calculate time when next rotation starts
							nextRotationStartTime = dateAddMin(rotationStartTime, maxPostsCurrentRotation * (osce.getPostLength() + osce.getShortBreak()) - osce.getShortBreak());
							
							// if SP was changed during rotation, add the SP change-break
							if(changeSimpatDuringRotation) {
								int numberBreakDuringRotation = numberSlotsTotal / numberSlotsUntilSPChange;
								if(numberSlotsTotal % numberSlotsUntilSPChange == 0)
									numberBreakDuringRotation -= 1;
								
								log.info("numberBreakDuringRotation: " + numberBreakDuringRotation);
								
								nextRotationStartTime = dateAddMin(nextRotationStartTime, numberBreakDuringRotation * (osce.getShortBreakSimpatChange() - osce.getShortBreak()));
								postsSinceSimpatChange = numberSlotsTotal % numberSlotsUntilSPChange;
								
								if(longBreakInRotationHalf)
									nextRotationStartTime = dateSubtractMin(nextRotationStartTime, osce.getShortBreakSimpatChange() - osce.getShortBreak());
							}
							
							if(longBreakInRotationHalf)
								nextRotationStartTime = dateAddMin(nextRotationStartTime, osce.getLongBreak() - osce.getShortBreak());
							
							// add middle break at the end of each rotation (except for last rotation, where either
							// lunch break or nothing is added)
							if(!lastRotation) {
								if(osceDay.getOsceSequences().size() == 1 && halfRotations) {
									nextRotationStartTime = dateAddMin(nextRotationStartTime, osce.getLunchBreak());
									// trick to make sure postsSinceSimpatChange is 0 after outer loop (is incremented by numberSlotsTotal in outer-loop)
									postsSinceSimpatChange = -1 * numberSlotsTotal;
								} else {
									// also insert long break after rotation if there was a long break in the middle of the rotation (SP change!)
									if(changeSimpatAfterRotation || longBreakInRotationHalf) {
										nextRotationStartTime = dateAddMin(nextRotationStartTime, osce.getLongBreak());
										postsSinceSimpatChange = 0;
									} else {
										nextRotationStartTime = dateAddMin(nextRotationStartTime, osce.getMiddleBreak());
									}
								}
							}
							log.info("next rotation start time: " + nextRotationStartTime);
							
							log.info("students inserted");
							log.info("post " + i + " finished");
						}
						
						rotationStartTime = nextRotationStartTime;
						
						// increase number of already performed posts since SPs were last changed
						if(!changeSimpatAfterRotation && !changeSimpatDuringRotation)
							postsSinceSimpatChange += numberSlotsTotal;
						
						// increase lower bound of student indexes
						studentIndexLowerBound += numberSlotsTotal;
						
						assignments.addAll(assThisRotation);
						
						// switch assignments of next rotation if number of slots is odd
						if(numberSlotsTotal % 2 == 1) {
							earlyStartFirst = !earlyStartFirst;
						}
						
						log.info("rotation " + currRotationNumber + " finished");
					}
					
					log.info("parcour " + course.getColor() + " finished");
					
					parcourIndex++;
				}
				
				log.info("sequence " + osceSequence.getLabel() + " finished");
				
				// add lunch break after sequence one (WARNING: more than two sequences a day is not taken into account!)
				if(osceDay.getOsceSequences().size() > 1)
					sequenceStartTime = dateAddMin(time, osce.getLunchBreak());
				
				rotationOffset += osceSequence.getNumberRotation();
			}
			
			log.info("day " + osceDay.getOsceDate() + " finished");
		}
		
		// persist all student assignments of this rotation
		Iterator<Assignment> itAss = assignments.iterator();
		while (itAss.hasNext()) {
			Assignment assignment = (Assignment) itAss.next();
			assignment.persist();
		}
		
//		printAllStudents(assignments);
//		printAllSP(assignments);
		
		createSPBreakAssignments();
		
		return assignments;
	}
	
	/**
	 * Generate break slots for SP (time-slots with same times as
	 * "normal" posts but with osce_post_room = null). SP that are
	 * not allocated to any post at a given time are allocated to this
	 * assignment.
	 */
	private void createSPBreakAssignments() {
		List<Assignment> refAssignments = Assignment.retrieveAssignmentsOfTypeSPUniqueTimes(osce);
		Iterator<Assignment> it = refAssignments.iterator();
		while (it.hasNext()) {
			Assignment assignment = (Assignment) it.next();
			
			Assignment ass = new Assignment();
			ass.setType(AssignmentTypes.PATIENT);
			ass.setOscePostRoom(null);
			ass.setOsceDay(assignment.getOsceDay());
			ass.setTimeStart(assignment.getTimeStart());
			ass.setTimeEnd(assignment.getTimeEnd());
			ass.persist();
		}
	}

	/**
	 * Create new student assignment
	 * @param osceDay day on which this assignment takes place
	 * @param oscePR post-room-assignment
	 * @param studentIndex student which is examined in this assignment
	 * @param startTime time when the assignment starts
	 * @param endTime time when the assignment ends
	 * @return
	 */
	private Assignment createStudentAssignment(OsceDay osceDay, OscePostRoom oscePR, int studentIndex, Date startTime, Date endTime) {
		Assignment ass2 = new Assignment();
		ass2.setType(AssignmentTypes.STUDENT);
		ass2.setOsceDay(osceDay);
		ass2.setSequenceNumber(studentIndex);
		ass2.setTimeStart(startTime);
		ass2.setTimeEnd(endTime);
		ass2.setOscePostRoom(oscePR);
		return ass2;
	}
	
	/**
	 * Create new SP assignment for post i at specific time
	 * @param i index of the post in "posts"
	 * @param osceDay day on which this assignment is put
	 * @param startTime time when the assignment starts
	 * @param oscePR OscePostRoom-link
	 */
	private void createSPAssignment(int i, OsceDay osceDay, Date startTime, OscePostRoom oscePR) {
		Assignment ass = new Assignment();
		ass.setType(AssignmentTypes.PATIENT);
		ass.setOsceDay(osceDay);
		ass.setSequenceNumber(simpatSequenceNumber++);
		ass.setTimeStart(startTime);
		ass.setTimeEnd(startTime);
		ass.setOscePostRoom(oscePR);
		ass.persist();
		
		simAssLastId[i] = ass.getId();
	}
	
	/**
	 * Finalize the previously created SP assignment
	 * @param i index of the post in "posts"
	 * @param endTime time when the assignment ends
	 */
	private void finalizeSPAssignment(int i, Date endTime) {
		Assignment ass = Assignment.findAssignment(simAssLastId[i]);
		if(ass != null) {
			ass.setTimeEnd(endTime);
			ass.flush();
		}
	}
	
	/**
	 * Change an SP on a specific post i. This methods invokes appropriate calls
	 * to "finalizeSPAssignment(...)" and "createSPAssignment(...)"
	 * @param i index of the post in "posts"
	 * @param osceDay day on which this assignment is put
	 * @param endTimeOld time when the old assignment ends
	 * @param startTimeNew time when the assignments starts
	 * @param oscePR OscePostRoom-link for new assignment
	 */
	private void changeSP(int i, OsceDay osceDay, Date endTimeOld, Date startTimeNew, OscePostRoom oscePR) {
		finalizeSPAssignment(i, endTimeOld);
		createSPAssignment(i, osceDay, startTimeNew, oscePR);
	}
	
	@SuppressWarnings("unused")
	private void printAllStudents(Set<Assignment> assThisRotation) {
		List<Assignment> assList = new ArrayList<Assignment>(assThisRotation);
		Collections.sort(assList, new Comparator<Assignment>() {
			public int compare(Assignment ass1, Assignment ass2) {
				if(ass1.getSequenceNumber() == ass2.getSequenceNumber()) {
					return (int) (ass1.getTimeStart().getTime() - ass2.getTimeStart().getTime());
				} else {
					return (int) (ass1.getSequenceNumber() - ass2.getSequenceNumber());
				}
			}
			
		});
		
		Iterator<Assignment> it = assList.iterator();
		int oldSeq = 0;
		while(it.hasNext()) {
			Assignment ass = (Assignment) it.next();
			
			if(ass.getSequenceNumber() > oldSeq) {
				System.out.println("\t ---");
				oldSeq = ass.getSequenceNumber();
			}
			
			if(ass.getType().equals(AssignmentTypes.STUDENT))
				System.out.println(debugStudent(ass));
		}
		
	}
	
	@SuppressWarnings("unused")
	private void printAllSP(Set<Assignment> assignments) {
		Iterator<Assignment> it = assignments.iterator();
		while(it.hasNext()) {
			Assignment ass = (Assignment) it.next();
			
			if(ass.getType().equals(AssignmentTypes.PATIENT))
				System.out.println(debugSP(ass));
		}
		
	}
	
	/**
	 * Add some minutes to a datetime and re-convert it.
	 * @param date
	 * @param minToAdd
	 * @return
	 */
	private Date dateAddMin(Date date, long minToAdd) {
		return new Date((long) (date.getTime() + minToAdd * 60 * 1000));
	}
	
	/**
	 * Subtract some minutes from a datetime and re-convert it.
	 * @param date
	 * @param minToSubtract
	 * @return
	 */
	private Date dateSubtractMin(Date date, long minToSubtract) {
		return new Date((long) (date.getTime() - minToSubtract * 60 * 1000));
	}

	/**
	 * Detect whether SP has to be changed after a certain number of slots
	 * @param numberSlots
	 * @return
	 */
	private boolean simpatChangeWithinSlots(int numberSlots) {
		return (numberSlotsUntilSPChange > 0 && numberSlots > numberSlotsUntilSPChange);
	}
	
	/**
	 * Check for lunch break after half of the rotations
	 * @param rotationNr
	 * @return
	 */
	private boolean lunchBreakNeeded(int rotationNr) {
		return rotationNr == rotationsPerDay / 2;
	}
	
	/**
	 * Get maximum of posts (of all parallel parcours) for current rotation
	 * 
	 * ex. parcour 1 has 5 posts while parcour 2 has 4 posts. Return value
	 * is then 5. It is needed to make sure parallel parcours always start
	 * at the same time.
	 * 
	 * @param parcours
	 * @param rotationNumber
	 * @return
	 */
	private int getMaxBreakPostsCurrentRotation(List<Course> parcours, int rotationNumber) {
		int maxPosts = 0;
		for(int j = 0; j < parcours.size(); j++) {
			int currPosts = rotations[j].get(rotationNumber);
			if(currPosts > maxPosts) {
				maxPosts = currPosts;
			}
		}
		return maxPosts;
	}
	
	/**
	 * Check OSCE for valid information set needed to optimize
	 * 
	 * @param osce
	 */
	private static void checkOsce(Osce osce) throws Exception {
		String errString = "missing information: %s";
		
		if(osce.getMaxNumberStudents() == null || osce.getMaxNumberStudents() <= 0)
			throw new Exception(String.format(errString, "maximum number of students"));
		
		if(osce.getNumberRooms() == null || osce.getNumberRooms() <= 0)
			throw new Exception(String.format(errString, "number of rooms available"));
		
		if(osce.getPostLength() == null || osce.getPostLength() <= 0)
			throw new Exception(String.format(errString, "post length"));
		
		if(osce.getShortBreak() == null || osce.getShortBreak() <= 0)
			throw new Exception(String.format(errString, "duration of short break (after a post)"));
		
		if(osce.getShortBreakSimpatChange() == null || osce.getShortBreakSimpatChange() <= 0)
			throw new Exception(String.format(errString, "duration of simpat change break (when a change of simpat is needed WITHIN rotation)"));
		
		if(osce.getMiddleBreak() == null || osce.getMiddleBreak() <= 0)
			throw new Exception(String.format(errString, "duration of middle break (after a rotation)"));
		
		if(osce.getLongBreak() == null || osce.getLongBreak() <= 0)
			throw new Exception(String.format(errString, "duration of long break (when a change of simpat is needed AFTER rotation)"));
		
		if(osce.getLunchBreak() == null || osce.getLunchBreak() <= 0)
			throw new Exception(String.format(errString, "duration of lunch break"));
		
		if(!(osce.getOsce_days().size() > 0))
			throw new Exception(String.format(errString, "no OsceDay for this Osce defined"));
	}
	
	/**
	 * Print debug information on assignment (namely start- and end-time consiting only of HH:mm)
	 * @param ass
	 * @return
	 */
	private String debugTimeStartEnd(Assignment ass) {
		String timeString = debugTime(ass.getTimeStart()) + " - " + debugTime(ass.getTimeEnd());
		return timeString;
	}
	
	private String debugTime(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		return format.format(date);
	}
	
	private String debugStudent(Assignment ass) {
		String room = "none";
		OscePostBlueprint postBP = null;
		if(ass.getOscePostRoom() != null) {
			room = ass.getOscePostRoom().getRoom().getRoomNumber();
			postBP = ass.getOscePostRoom().getOscePost().getOscePostBlueprint();
		}
		
		return "\t student "+ass.getSequenceNumber()+" (room: "+room+", "+debugTimeStartEnd(ass)+") inserted..." + (postBP != null && postBP.equals(PostType.ANAMNESIS_THERAPY) ? "first: " + postBP.getIsFirstPart() : "");
	}
	
	private String debugSP(Assignment ass) {
		String room = "none";
		if(ass.getOscePostRoom() != null) {
			room = ass.getOscePostRoom().getRoom().getRoomNumber();
		}
		return "(room: "+room+", "+debugTimeStartEnd(ass)+") inserted...";
	}
}
