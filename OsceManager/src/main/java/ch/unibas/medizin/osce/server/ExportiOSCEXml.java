package ch.unibas.medizin.osce.server;

import static org.apache.commons.lang.StringUtils.defaultString;
import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.log4j.Logger;

import ch.unibas.medizin.osce.domain.Assignment;
import ch.unibas.medizin.osce.domain.BucketInformation;
import ch.unibas.medizin.osce.domain.CheckList;
import ch.unibas.medizin.osce.domain.ChecklistCriteria;
import ch.unibas.medizin.osce.domain.ChecklistItem;
import ch.unibas.medizin.osce.domain.ChecklistOption;
import ch.unibas.medizin.osce.domain.Course;
import ch.unibas.medizin.osce.domain.Doctor;
import ch.unibas.medizin.osce.domain.Osce;
import ch.unibas.medizin.osce.domain.OsceDay;
import ch.unibas.medizin.osce.domain.OscePostRoom;
import ch.unibas.medizin.osce.domain.OsceSequence;
import ch.unibas.medizin.osce.domain.StandardizedRole;
import ch.unibas.medizin.osce.server.i18n.GWTI18N;
import ch.unibas.medizin.osce.server.service.ObjectFactory;
import ch.unibas.medizin.osce.server.service.Oscedata;
import ch.unibas.medizin.osce.server.service.Oscedata.Candidates;
import ch.unibas.medizin.osce.server.service.Oscedata.Candidates.Candidate;
import ch.unibas.medizin.osce.server.service.Oscedata.Checklists;
import ch.unibas.medizin.osce.server.service.Oscedata.Checklists.Checklist;
import ch.unibas.medizin.osce.server.service.Oscedata.Checklists.Checklist.Checklistitems;
import ch.unibas.medizin.osce.server.service.Oscedata.Checklists.Checklist.Checklistitems.Checklistitem;
import ch.unibas.medizin.osce.server.service.Oscedata.Checklists.Checklist.Checklistitems.Checklistitem.Checklistcriterias;
import ch.unibas.medizin.osce.server.service.Oscedata.Checklists.Checklist.Checklistitems.Checklistitem.Checklistoptions;
import ch.unibas.medizin.osce.server.service.Oscedata.Checklists.Checklist.Checklistitems.Checklistitem.Checklistoptions.Checklistoption;
import ch.unibas.medizin.osce.server.service.Oscedata.Courses;
import ch.unibas.medizin.osce.server.service.Oscedata.Credentials;
import ch.unibas.medizin.osce.server.service.Oscedata.Credentials.Host;
import ch.unibas.medizin.osce.server.service.Oscedata.Examiners;
import ch.unibas.medizin.osce.server.service.Oscedata.Examiners.Examiner;
import ch.unibas.medizin.osce.server.service.Oscedata.Rotations;
import ch.unibas.medizin.osce.server.service.Oscedata.Rotations.Rotation;
import ch.unibas.medizin.osce.server.service.Oscedata.Stations;
import ch.unibas.medizin.osce.server.service.Oscedata.Stations.Station;
import ch.unibas.medizin.osce.shared.BucketInfoType;
import ch.unibas.medizin.osce.shared.i18n.OsceConstantsWithLookup;

public class ExportiOSCEXml {
	private static Logger Log = Logger.getLogger(ExportiOSCEXml.class);
	
	public ExportiOSCEXml() {
	}
	
	public static String createiOSCEXmlFile(HttpServletRequest request, ByteArrayOutputStream os, Long osceId) {
		String fileName = "";
		try {
			OsceConstantsWithLookup constants = GWTI18N.create(OsceConstantsWithLookup.class);
			Osce osce = Osce.findOsce(osceId);
			byte[] bytes = new ExportiOSCEXml().generateXmlFileByOsceId(osceId, osce);
			os.write(bytes);
			
			fileName = osce.getSemester().getSemester().toString() 
					+ osce.getSemester().getCalYear().toString().substring(2, osce.getSemester().getCalYear().toString().length()) 
					+ "-" + (constants.getString(osce.getStudyYear().toString()).replace(".", "")); 
									
			fileName = fileName + OsMaFilePathConstant.IOSCE_FILE_EXTENSION;
			
			return fileName;
		}
		catch (Exception e) {
			Log.error(e.getMessage(), e);
			return fileName;
		}
	}

	public byte[] generateXmlFileByOsceId(Long osceId, Osce osce) {
		Log.info("export Osce called at : StandardizedRole");

		try {
			OsceConstantsWithLookup constants = GWTI18N.create(OsceConstantsWithLookup.class);

			ObjectFactory factory = new ObjectFactory();

			Oscedata oscedata = factory.createOscedata();
			oscedata.setVersion(1.1f);
			
			BucketInformation bucketInformation = BucketInformation.findBucketInformationBySemesterForExport(osce.getSemester().getId());
			Credentials credentials = factory.createOscedataCredentials();
			oscedata.setCredentials(credentials);
			
			if(bucketInformation == null || bucketInformation.getType() == null) {
				credentials.setType(BucketInfoType.S3.getStringValue());
			}else {
				credentials.setType(bucketInformation.getType().getStringValue());	
			}
			
			Host host = factory.createOscedataCredentialsHost();
			credentials.setHost(host);
			if(bucketInformation != null && BucketInfoType.FTP.equals(bucketInformation.getType())) {
				host.setBasepath(bucketInformation.getBasePath());
			}
			
			host.setValue(bucketInformation == null ? "" : defaultString(bucketInformation.getBucketName()));
			credentials.setUser(bucketInformation == null ? "" : defaultString(bucketInformation.getAccessKey()));
			credentials.setPassword(bucketInformation == null ? "" : defaultString(bucketInformation.getSecretKey()));
			credentials.setEncryptionKey(bucketInformation == null ? "" : defaultString(bucketInformation.getEncryptionKey()));

			exportChecklist(factory, oscedata, osceId);
			exportExaminers(osceId, factory, oscedata);
			exportCandidates(osceId, factory, oscedata);
			exportStations(osceId, factory, oscedata);
			exportCourses(osceId, factory, oscedata, constants);
			exportRotations(osceId, factory, oscedata, constants);

			JAXBContext jaxbContext = JAXBContext.newInstance(Oscedata.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	 
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
	 		ByteArrayOutputStream stream = new ByteArrayOutputStream();
			jaxbMarshaller.marshal(oscedata, stream);
			String data = new String(stream.toByteArray(),"UTF-8");
			data = data.replaceAll("xsi:oscedata", "oscedata");
			
			return data.getBytes();

		} catch (IOException e) {
			Log.error(e.getMessage(), e);
		} catch (Exception e) {
			Log.error(e.getMessage(), e);
		}
		return null;
	}
	
	private void exportChecklist(ObjectFactory factory, Oscedata oscedata, Long osceId) {

		Log.info("export exportChecklist at : StandardizedRole");

		Checklists checklistsBean = factory.createOscedataChecklists();
		oscedata.setChecklists(checklistsBean);
		
		List<CheckList> checkLists =  CheckList.findAllCheckListforOsce(osceId);
		
		for (CheckList checklist : checkLists) {
		
			Checklist checklistBean = factory.createOscedataChecklistsChecklist();

			checklistsBean.getChecklist().add(checklistBean);

			checklistBean.setId(checklist.getId());
			checklistBean.setName(defaultString(checklist.getTitle()));

			Checklistitems checklistitems = factory.createOscedataChecklistsChecklistChecklistitems();
			checklistBean.setChecklistitems(checklistitems);
			
			List<ChecklistItem> checklistItems = ChecklistItem.findAllChecklistItemsForChecklist(checklist.getId());
			
			for (ChecklistItem checklistItem : checklistItems) {
				Checklistitem checklistitemBean = factory.createOscedataChecklistsChecklistChecklistitemsChecklistitem();
				checklistitems.getChecklistitem().add(checklistitemBean);
				exportChecklistItemBean(factory, checklistItem, checklistitemBean);
			
				List<ChecklistItem> checklistItemChilds = ChecklistItem.findAllChecklistItemsChild(checklistItem.getId());
				exportChecklistItemsChild(checklistItemChilds,checklistitemBean,factory);
			}
		}		
	}

	private void exportChecklistItemBean(ObjectFactory factory, ChecklistItem checklistItem, Checklistitem checklistitemBean) {
		checklistitemBean.setId(checklistItem.getId());
		checklistitemBean.setName(checklistItem.getName());
		checklistitemBean.setType(checklistItem.getItemType().ordinal());
		
		if(checklistItem.getOptionType() != null) {
			checklistitemBean.setOptiontype(checklistItem.getOptionType().ordinal());	
		} else {
			checklistitemBean.setOptiontype(null);
		}
		
		if(checklistItem.getIsRegressionItem() != null) {
			checklistitemBean.setIsRegressionItem(checklistItem.getIsRegressionItem().toString());	
		} else {
			checklistitemBean.setIsRegressionItem(null);
		}
		
		if(checklistItem.getSequenceNumber() != null) {
			checklistitemBean.setSequencenumber(checklistItem.getSequenceNumber());	
		}
		
		if(checklistItem.getDescription() != null) {
			checklistitemBean.setDescription(checklistItem.getDescription());
		}
		
		if(checklistItem.getCheckListCriterias() != null && checklistItem.getCheckListCriterias().isEmpty() == false) {
			final Checklistcriterias checklistcriteriasBean = factory.createOscedataChecklistsChecklistChecklistitemsChecklistitemChecklistcriterias();
			checklistitemBean.setChecklistcriterias(checklistcriteriasBean);
			
			for (final ChecklistCriteria checklistCriteria : checklistItem.getCheckListCriterias()) {
				final ch.unibas.medizin.osce.server.service.Oscedata.Checklists.Checklist.Checklistitems.Checklistitem.Checklistcriterias.Checklistcriteria checklistcriteriaBean = factory.createOscedataChecklistsChecklistChecklistitemsChecklistitemChecklistcriteriasChecklistcriteria();
				checklistcriteriasBean.getChecklistcriteria().add(checklistcriteriaBean);
				checklistcriteriaBean.setId(checklistCriteria.getId());
				checklistcriteriaBean.setName(defaultString(checklistCriteria.getCriteria()));
				checklistcriteriaBean.setSequencenumber(checklistCriteria.getSequenceNumber());
				checklistcriteriaBean.setDescription(defaultString(checklistCriteria.getDescription()));
			}
		}
		
		if(checklistItem.getCheckListOptions() != null && checklistItem.getCheckListOptions().isEmpty() == false) {
			final Checklistoptions checklistoptionsBean = factory.createOscedataChecklistsChecklistChecklistitemsChecklistitemChecklistoptions();
			checklistitemBean.setChecklistoptions(checklistoptionsBean);
			for (final ChecklistOption option : checklistItem.getCheckListOptions()) {
				final Checklistoption checklistoptionBean = factory.createOscedataChecklistsChecklistChecklistitemsChecklistitemChecklistoptionsChecklistoption();
				checklistoptionsBean.getChecklistoption().add(checklistoptionBean);
				
				checklistoptionBean.setId(option.getId());
				checklistoptionBean.setName(defaultString(option.getOptionName()));
				checklistoptionBean.setDescription(defaultString(option.getDescription()));
				checklistoptionBean.setValue1(defaultString(option.getValue()));
				checklistoptionBean.setSequencenumber(option.getSequenceNumber());
				
				if(option.getCriteriaCount() != null) {
					checklistoptionBean.setCriteriacount(option.getCriteriaCount());	
				} else {
					checklistoptionBean.setCriteriacount(0);
				}
				
			}
		}
		
	}

	private void exportChecklistItemsChild(List<ChecklistItem> checklistItems, Checklistitem checklistitemParentBean, ObjectFactory factory) {
		for (ChecklistItem checklistItem : checklistItems) {
			Checklistitem checklistitemBean = factory.createOscedataChecklistsChecklistChecklistitemsChecklistitem();
			checklistitemParentBean.getChecklistitem().add(checklistitemBean);
			exportChecklistItemBean(factory, checklistItem, checklistitemBean);
		
			List<ChecklistItem> checklistItemChilds = ChecklistItem.findAllChecklistItemsChild(checklistItem.getId());
			exportChecklistItemsChild(checklistItemChilds,checklistitemBean,factory);
		}
		
		
	}

	private void exportExaminers(Long osceId, ObjectFactory factory, Oscedata oscedata) {

		Log.info("export exportExaminers at : StandardizedRole");
		
		Examiners examinersBean = factory.createOscedataExaminers();
		oscedata.setExaminers(examinersBean);

		List<Doctor> examinerAssList = Assignment.findAssignmentExamnierByOsce(osceId);
		
		for (Doctor examinerAss : examinerAssList)
		{
			Examiner examinerBean = factory.createOscedataExaminersExaminer();
			examinersBean.getExaminer().add(examinerBean);

			examinerBean.setId(examinerAss.getId());
			examinerBean.setSalutation(defaultString(examinerAss.getTitle()));
			examinerBean.setFirstname(defaultString(examinerAss.getPreName()));
			examinerBean.setLastname(defaultString(examinerAss.getName()));
			if (isNotBlank(examinerAss.getTelephone())) {
				examinerBean.setPhone(getPhoneNumber(examinerAss.getTelephone()));
			}
		}
	}

	private void exportCandidates(Long osceId, ObjectFactory factory, Oscedata oscedata) {

		Log.info("export exportCandidates at : StandardizedRole");

		Candidates candidatesBean = factory.createOscedataCandidates();
		oscedata.setCandidates(candidatesBean);
		
		Set<String> done = new HashSet<String>();
		List<Assignment> assList = new ArrayList<Assignment>();
		assList.addAll(Assignment.findAssignmentStudentsByOsce(osceId));
		assList.addAll(Assignment.findAssignmentOfLogicalBreakPost(osceId));
		Collections.sort(assList, new Comparator<Assignment>() {

			@Override
			public int compare(Assignment o1, Assignment o2) {
				return o1.getId().compareTo(o2.getId());
			}
		});
		
		for (Assignment studAss : assList)
		{	
			String studIdComp = studAss.getStudent() == null ? "" : studAss.getStudent().getId().toString();
			
			if(done.contains(studIdComp) == false) {
				done.add(studIdComp);
				
				Candidate candidateBean = factory.createOscedataCandidatesCandidate();
				candidatesBean.getCandidate().add(candidateBean);

				if(studAss.getStudent() != null) {
					candidateBean.setId(studAss.getStudent().getId());	
				} else {
					candidateBean.setId(0l);
				}
				
				String firstName = studAss.getStudent() == null ? ("S" + String.format("%03d", studAss.getSequenceNumber())) : (studAss.getStudent().getPreName() == null ? "" : studAss.getStudent().getPreName());
				candidateBean.setFirstname(firstName);
				String lastName = studAss.getStudent() == null ? ("S" + String.format("%03d", studAss.getSequenceNumber())) : (studAss.getStudent().getName() == null ? "" : studAss.getStudent().getName());
				candidateBean.setLastname(lastName);
				String email = studAss.getStudent() == null ? "" : (studAss.getStudent().getEmail() == null ? "" : studAss.getStudent().getEmail());
				candidateBean.setEmail(email);
			}
		}
	}

	private void exportStations(Long osceId, ObjectFactory factory, Oscedata oscedata) {

		Log.info("export exportStations at : StandardizedRole");

		Stations stationsBean = factory.createOscedataStations();
		oscedata.setStations(stationsBean);

		List<OscePostRoom> oscePostRoomList = OscePostRoom.findOscePostRoomByOsce(osceId);
		
		for (OscePostRoom oscePostRoom : oscePostRoomList)
		{
			Station stationBean = factory.createOscedataStationsStation();
			stationsBean.getStation().add(stationBean);

			stationBean.setId(oscePostRoom.getId());
			//stationBean.setTitle(constants.STATION_TITLE());
			
			if (oscePostRoom.getRoom() == null)
				stationBean.setIsBreakStation("yes");
			else 
				stationBean.setIsBreakStation("no");
			
			if (oscePostRoom.getOscePost() != null && oscePostRoom.getOscePost().getStandardizedRole() != null) {
				StandardizedRole standardizedRole = oscePostRoom.getOscePost().getStandardizedRole();
				
				if (standardizedRole.getCheckList() != null) {
					CheckList checkList = standardizedRole.getCheckList();
					
					stationBean.setChecklistId(checkList.getId());
					
					if (standardizedRole.getRoleTopic() != null && standardizedRole.getRoleTopic().getSpecialisation() != null)
					{
						String stationTitle = standardizedRole.getRoleTopic().getSpecialisation().getName() + " / " + standardizedRole.getRoleTopic().getName();
						stationBean.setTitle(stationTitle);
					}
					else{
						stationBean.setTitle("");
					}
				}
			}
		}
		
		Long logicalBreakAssignmentCount = Assignment.countAssignmentOfLogicalBreakPostPerOsce(osceId);
		
		if (logicalBreakAssignmentCount > 0)
		{
			Station stationBean = factory.createOscedataStationsStation();
			stationsBean.getStation().add(stationBean);
			stationBean.setIsBreakStation("yes");
			stationBean.setTitle("");
		}
	}

	private void exportCourses(Long osceId, ObjectFactory factory, Oscedata oscedata, OsceConstantsWithLookup constants) {

		Log.info("export exportCourses at : StandardizedRole");

		Courses coursesBean = factory.createOscedataCourses();
		oscedata.setCourses(coursesBean);
		
		List<OsceDay> osceDayList = OsceDay.findOsceDayByOsce(osceId);
		
		for (int j=0; j<osceDayList.size(); j++)
		{
			List<OsceSequence> sequenceList = OsceSequence.findOsceSequenceByOsceDay(osceDayList.get(j).getId());
			
			for (int k=0; k<sequenceList.size(); k++)
			{
				List<Course> courseList = sequenceList.get(k).getCourses();
				
				for (Course course : courseList)
				{
					ch.unibas.medizin.osce.server.service.Oscedata.Courses.Course courseBean = factory.createOscedataCoursesCourse();
					coursesBean.getCourse().add(courseBean);
					
					courseBean.setId(course.getId());
					courseBean.setTitle(constants.getString(course.getColor()));
				}
			}
		}
	}

	private void exportRotations(Long osceId, ObjectFactory factory, Oscedata oscedata, OsceConstantsWithLookup constants) {

		Log.info("export exportRotations at : StandardizedRole");


		List<OsceDay> osceDayList = OsceDay.findOsceDayByOsce(osceId);
		int startrotation = 0;
		int totalrotation = 0;
		
		Rotations rotationsBean = factory.createOscedataRotations();
		oscedata.setRotations(rotationsBean);
		
		for (int j=0; j<osceDayList.size(); j++)
		{
			List<OsceSequence> sequenceList = OsceSequence.findOsceSequenceByOsceDay(osceDayList.get(j).getId());
			
			for (int k=0; k<sequenceList.size(); k++)
			{
				startrotation = totalrotation;
				totalrotation = totalrotation + sequenceList.get(k).getNumberRotation();
				
				List<Course> courseList = sequenceList.get(k).getCourses();
				
				for (Course course : courseList)
				{
					for (int l=startrotation; l<totalrotation; l++)
					{	
						Rotation rotationBean = factory.createOscedataRotationsRotation();
						rotationsBean.getRotation().add(rotationBean);
						
						rotationBean.setId(Long.parseLong((l+1) + "" + course.getId()));
						rotationBean.setTitle(("Rotation " + String.format("%02d", (l+1)) + " " + constants.getString(course.getColor())));
						rotationBean.setCourseId(course.getId());
						
						ch.unibas.medizin.osce.server.service.Oscedata.Rotations.Rotation.Stations stationsBean = factory.createOscedataRotationsRotationStations();
						rotationBean.setStations(stationsBean);
						
						List<OscePostRoom> oscePostRoomList = OscePostRoom.findOscePostRoomByCourseID(course.getId());
						
						for (OscePostRoom oscePostRoom : oscePostRoomList)
						{
							ch.unibas.medizin.osce.server.service.Oscedata.Rotations.Rotation.Stations.Station stationBean = factory.createOscedataRotationsRotationStationsStation();
							stationsBean.getStation().add(stationBean);
							
							stationBean.setId(oscePostRoom.getId());
							
							List<Assignment> assignmentlist = Assignment.findAssignmentByOscePostRoom(oscePostRoom.getId(), osceId, l);
							
							List<Assignment> examinerAssList = new ArrayList<Assignment>();
							
							if (assignmentlist.size() > 0)
							{
								Date timestart = assignmentlist.get(0).getTimeStart();
								Date timeend = assignmentlist.get(assignmentlist.size()-1).getTimeEnd();
								examinerAssList = Assignment.findAssignmentExamnierByOscePostRoom(oscePostRoom.getId(), osceId, timestart, timeend);
							}
							
							for (Assignment examinerAss : examinerAssList) 
							{
								if (examinerAss.getExaminer() != null)
								{
									stationBean.setExaminerId(examinerAss.getExaminer().getId());
									break;
								}
							}
							
							for (Assignment studAss : assignmentlist) 
							{	
								if(studAss.getStudent() != null) {
									stationBean.setFirstCandidateId(studAss.getStudent().getId());
								} 
								
								break;
							}
						}
						
						List<Assignment> logicalBreakAssignment = Assignment.findAssignmentOfLogicalBreakPostPerRotation(osceDayList.get(j).getId(), course.getId(), l);
						
						if (logicalBreakAssignment != null && logicalBreakAssignment.size() > 0)
						{
							ch.unibas.medizin.osce.server.service.Oscedata.Rotations.Rotation.Stations.Station stationBean = factory.createOscedataRotationsRotationStationsStation();
							stationsBean.getStation().add(stationBean);
							
							for (Assignment assignment : logicalBreakAssignment)  
							{
								if(assignment.getStudent()  != null) {
									stationBean.setFirstCandidateId(assignment.getStudent().getId());
								}
								
								break;
							}
						}
					}
				}
			}
		}
	}
	
	private Long getPhoneNumber(String telephone) {
		telephone = telephone.replaceAll("\\+", "");
		telephone = telephone.replaceAll(" ", "");
		try {
			return Long.parseLong(telephone,10);	
		}catch (Exception e) {
			Log.error(e);
		}
		
		return null;
	}
}
