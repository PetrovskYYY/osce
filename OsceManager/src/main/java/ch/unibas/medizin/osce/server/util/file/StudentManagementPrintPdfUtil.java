package ch.unibas.medizin.osce.server.util.file;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;

import ch.unibas.medizin.osce.domain.Answer;
import ch.unibas.medizin.osce.domain.CheckList;
import ch.unibas.medizin.osce.domain.ChecklistCriteria;
import ch.unibas.medizin.osce.domain.ChecklistItem;
import ch.unibas.medizin.osce.domain.ChecklistOption;
import ch.unibas.medizin.osce.domain.Doctor;
import ch.unibas.medizin.osce.domain.File;
import ch.unibas.medizin.osce.domain.Notes;
import ch.unibas.medizin.osce.domain.Osce;
import ch.unibas.medizin.osce.domain.Semester;
import ch.unibas.medizin.osce.domain.Signature;
import ch.unibas.medizin.osce.domain.StandardizedRole;
import ch.unibas.medizin.osce.domain.Student;
import ch.unibas.medizin.osce.server.OsMaFilePathConstant;
import ch.unibas.medizin.osce.shared.NoteType;
import ch.unibas.medizin.osce.shared.util;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class StudentManagementPrintPdfUtil extends PdfUtil {

	private StandardizedRole standardizedRole;
	
	private boolean isValueAvailable[];
	
	protected Font tabFont = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD);
	
	public StudentManagementPrintPdfUtil() {
		super();		
	}

	public StudentManagementPrintPdfUtil(String locale){
		super(locale);
		/*
		try {
			constants = GWTI18N.create(OsceConstants.class, locale.toString());
		} catch (IOException e) 
		{
			log.error("Error: ", e);
		}
		*/
	}
	
	public StudentManagementPrintPdfUtil(Locale locale) {
		super(locale);
	}

	
	public void writeStudentChecklistFile(List<StandardizedRole> standardizedRoleList,Long studId,Long osceId, OutputStream out)
	{
		try 
		{
			Iterator<StandardizedRole> standardizedRoleIterator=standardizedRoleList.iterator();
			//title = constants.standardizedRole(); // + " "+ util.getEmptyIfNull(standardizedRole.getLongName());
			title = "";
			Osce osce = Osce.findOsce(osceId);
			Semester semester = Semester.findSemester(osce.getSemester().getId());
			List<String> files= new ArrayList<String>();
			String localPath = OsMaFilePathConstant.IMPORT_AUDIO_NOTE_PATH + semester.getSemester() + semester.getCalYear() + "/";

			ByteArrayOutputStream os = new ByteArrayOutputStream();
			writer = PdfWriter.getInstance(document, os);
			
			addMetaData();
			document.open();
			addHeader();			
			this.isValueAvailable = new boolean[4];
			while(standardizedRoleIterator.hasNext())
			{				
				StandardizedRole standardizedRole=standardizedRoleIterator.next();
				log.info("Standardized Role: " + standardizedRole.getId());
				this.standardizedRole = standardizedRole;			
				//document.add(new Chunk("Hello World"));		
				addStudentName(studId);
				addCheckListDetails(studId,standardizedRole.getCheckList().getId());
				addSignature(standardizedRole, osceId, studId);
				document.newPage();
				files.addAll(findAudioFileNamesForStudent(studId, osceId,standardizedRole,localPath));
			}
			document.close();
			createZipFile(os, out,files);
		}
		catch (Exception e) 
		{
			log.error(e.getMessage(), e);
		}		
	}

	private List<String> findAudioFileNamesForStudent(Long studId, Long osceId, StandardizedRole standardizedRole, String localPath) {
		
			List<String> fileNameList = new ArrayList<String>(); 
 			Doctor examiner = Answer.findExaminerByStandardizedRoleOsceAndStudent(standardizedRole.getId(), osceId, studId);
			List<Notes> audioNotes =  Notes.findNotesByExaminerAndStudentAndNotetype(examiner.getId(), studId, NoteType.STUDENT_AUDIO.ordinal(), standardizedRole.getId());
		
			for (Notes notes : audioNotes) {				
				localPath+= notes.getComment();
				fileNameList.add(localPath);
			}
			return fileNameList;
	}
	
	private void addStudentName(Long studId) {
	
		Student student = Student.findStudent(studId);
		
		Paragraph titleDetails = new Paragraph();
		Font roleTitleFont = new Font(Font.FontFamily.TIMES_ROMAN, 15,Font.BOLD);
		if (student != null && student.getName() != null && student.getPreName() != null)
			titleDetails.add(new Chunk(student.getName() + " " + student.getPreName() , roleTitleFont));
		
		addEmptyLine(titleDetails, 1);		
		try {
			document.add(titleDetails);
		} catch (DocumentException e) {
			log.error(e.getMessage(),e);
		}
	}

	private void addSignature(StandardizedRole standardizedRole, Long osceId, Long studId) {
		try
		{
			Doctor examiner = Answer.findExaminerByStandardizedRoleOsceAndStudent(standardizedRole.getId(), osceId, studId);
			
			if (examiner != null)
			{
				Signature signature = Signature.findSignaureByDoctorOsceAndStandardizedRole(examiner.getId(), osceId, standardizedRole.getId());
				
				if (signature != null)
				{
					Image image = Image.getInstance(signature.getSignatureImage());
					image.setAlignment(2);
					float height = 100f;
					float width = (height * image.getWidth()) / image.getHeight();
					image.scaleToFit(width,height);
					writer.addDirectImageSimple(image);
					document.add(image);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
	}
	
	public void noDataFound(OutputStream os)
	{
		title = "";
		try {
			ByteArrayOutputStream pdfStream=new ByteArrayOutputStream();
			List<String> files= new ArrayList<String>();
			writer = PdfWriter.getInstance(document, pdfStream);
			addMetaData();
			document.open();
			addHeader();			
			document.add(new Chunk(constants.noDataFound()));
			document.close();
			createZipFile(pdfStream, os, files);
		} catch (DocumentException e) {
			log.error(e.getMessage(),e);
		}
		
		
	}
	
	

	private void addMetaData() {
		document.addTitle(title);
		document.addSubject(constants.roleDetail());
		document.addAuthor(System.getProperty("user.name"));
		document.addCreator(System.getProperty("user.name"));
		document.addCreationDate();
	}

	private void addHeader() {
		Paragraph preface = new Paragraph();
		addEmptyLine(preface, 2);
		Paragraph headerParagraph = new Paragraph(title, headerFont);
		
		headerParagraph.setAlignment(Element.ALIGN_CENTER
				| Element.ALIGN_MIDDLE);
		preface.add(headerParagraph);
//		Paragraph confidentialityParagraph = new Paragraph(
//				constants.handledConfidentially(), smallFont);
//		preface.add(confidentialityParagraph);
		addEmptyLine(preface, 1);

		try {
			document.add(preface);
		} catch (Exception e) {
			log.error("In addStatndadizedPatientImage: " + e.getMessage());
		}
	}

	private void addDetails() {
		if (isValueAvailable[0]) 
		{
			Paragraph details = new Paragraph();
			PdfPTable table = createDetailsTable();
			table.setSpacingBefore(titleTableSpacing);
			details.add(new Chunk(constants.roleDetail(), paragraphTitleFont));
			details.add(table);
			addEmptyLine(details, 1);

			try {
				document.add(details);
			} catch (DocumentException e) {
				log.error("in PdfUtil.addDetails(): " + e.getMessage());
			}
		}
	}

	

	
	private void addFileDetails() {
		Paragraph details = new Paragraph();
		PdfPTable table = createFileDetailsTable();
		if (isValueAvailable[1]) {
			table.setSpacingBefore(titleTableSpacing);
			details.add(new Chunk(constants.fileDetail() + " "
					+ constants.details(), paragraphTitleFont));
			details.add(table);
			addEmptyLine(details, 1);

			try {
				document.add(details);
			} catch (DocumentException e) {
				log.error("in PdfUtil.addDetails(): " + e.getMessage());
			}
		}
	}

	private void addCheckListDetails(long studId, Long checklistId) {
		Paragraph titleDetails = new Paragraph();
		Font roleTitleFont = new Font(Font.FontFamily.TIMES_ROMAN, 15,Font.BOLD);
		titleDetails.add(new Chunk(constants.standardizedRole()+": "+ util.getEmptyIfNull(standardizedRole.getLongName()), roleTitleFont));
		addEmptyLine(titleDetails, 1);
		
		
		try {
			document.add(titleDetails);
		} catch (DocumentException e) {
			log.error(e.getMessage(),e);
		}
		
		
		
		if ((standardizedRole.getCheckList() != null) && (standardizedRole.getCheckList().getCheckListTopics() != null) && (standardizedRole.getCheckList().getCheckListTopics().size() > 0)) 
		{
			Paragraph details = new Paragraph();
			
			String checkListTitle = (standardizedRole.getCheckList().getTitle() != null)? " " + standardizedRole.getCheckList().getTitle() : "";					
			details.add(new Chunk(constants.checkList() +": "+ checkListTitle, paragraphTitleFont));
			addEmptyLine(details, 1);
			
			//details.add(new Chunk(checkListTitle, paragraphTitleFont));			
			// addEmptyLine(details, 1);

			try {
				//document.add(titleDetails);
				
				document.add(details);
			} catch (DocumentException e) {
				log.error("in PdfUtil.addDetails(): " + e.getMessage());
			}
			createCheckListTabDetails(studId,checklistId);
			//createCheckListDetailsTable(studId);
		}	
		
	}

	private void createCheckListTabDetails(long studId, Long checklistId) {

		List<ChecklistItem> checklistTabs = ChecklistItem.findAllChecklistTabsByChecklistId(checklistId);
		for(ChecklistItem checklistTab: checklistTabs){
			String tabText=checklistTab.getName();
			Paragraph tabParagraph = new Paragraph(new Chunk(tabText,tabFont));
			try {
				addEmptyLine(tabParagraph, 1);
				document.add(tabParagraph);
			} catch (DocumentException e) {
				e.printStackTrace();
			}
			createCheckListDetailsTable(studId,checklistTab.getId());

		}
	}

	private Paragraph createPara(PdfPTable pdfPTable, String header) {

		Paragraph details = new Paragraph();
		// pdfPTable.setSpacingBefore(titleTableSpacing);
		// addEmptyLine(details, 1);
		if (header.compareTo("") != 0)
			details.add(new Chunk(header, subTitleFont));
		details.add(pdfPTable);
		return details;
	}
	
	private PdfPCell getAnswerCell(String question, List<ChecklistOption> options,List<Long> givenAnswer) {
		Collections.sort(options);
		String[] answers = new String[options.size()];
		
		List<Integer> selectedAnswers=new ArrayList<Integer>();
		for(Long ans:givenAnswer)
		{
			if (ans != null)
				selectedAnswers.add(Integer.parseInt(ans.toString()));
		}
		
		
		for (int i=0; i < options.size(); i++) {
			ChecklistOption option = options.get(i);		
			answers[i] = option.getOptionName() + " (" + option.getValue() + ")";
			
		}	
/*		for(Long ans:givenAnswer) {
			selectedAnswers.add(Integer.valueOf(ans.toString()));
		}
		return getCheckBoxCell(question, answers,selectedAnswers);*/
		
			PdfPCell cell = new PdfPCell();
			//PdfFormField group = null;
			cell.setPadding(0);
			cell.setBorder(Rectangle.NO_BORDER);
			/*if (isRadio) {
				group = PdfFormField.createRadioButton(writer, true);
				String fieldName = question.toLowerCase().replaceAll("[^a-z0-9]+",
						"_");
				group.setFieldName(fieldName);
				cell.setCellEvent(new CheckBoxGroupEvent(writer, group));
			}*/

			//PdfPTable subTable = new PdfPTable(new float[] { 0.05f, 0.95f });
			PdfPTable subTable = new PdfPTable(new float[] { 1f});

			for (int i = 0; i < options.size(); i++) {
				boolean isSelected = false;
				if (selectedAnswers != null) {
					for (Integer nAnswer : selectedAnswers) {
						Long optionId = options.get(i).getId();
						if (nAnswer != null && nAnswer.intValue() == optionId) {
							isSelected = true;							
						}
					}
				}
				/*String answer;
				if(isSelected) 
				{
					answer = options.get(i).getName() + "*";
					
				}else {
					answer = options.get(i).getName();
				}
				
				subTable.addCell(answer);*/
				String answer = options.get(i).getOptionName();
				PdfPCell subCell = new PdfPCell();
				CheckBoxCellEvent event;
				/*if (isRadio) {
					event = new CheckBoxCellEvent(writer, group,possibleAnswers[i], isSelected);
				}
				else 
				{
					event = new CheckBoxCellEvent(writer, options.get(i),isSelected);
				}*/
				//subCell.setCellEvent(event);				
				subCell.setBorder(Rectangle.NO_BORDER);
				subTable.addCell(subCell);
				if(isSelected)
				{
					//System.out.println("Selected Answer Bold: " + answer);
					subTable.addCell(getPdfCell(answer,boldFont,0,0));
				}
				else
				{
					//System.out.println("Selected Answer: " + answer);
					subTable.addCell(getPdfCell(answer));
				}
			
				
			}

			subTable.setWidthPercentage(100.0f);

			cell.addElement(subTable);
			return cell;
		
		
		
	}
	
	private PdfPTable createCheckListQuestionTable(List<ChecklistItem> questions,long studId) {
		if (questions != null && questions.size() > 0) {
		PdfPTable table = new PdfPTable(new float[] { 0.7f, 0.3f });
			
			int i = 0;
			int j=0;
			for (ChecklistItem question: questions) {
				String questionText = question.getName();
				if (questionText != null) {
					Chunk questionChunk = new Chunk(questionText, boldFont);					
					Chunk criteriaChunk = null;
					Chunk instructionChunk = null;
					
					if (question.getDescription() != null) {
						instructionChunk = new Chunk(question.getDescription(), italicFont);						
					}

					if (question.getCheckListCriterias().size() > 0) {
						//StringBuilder sb = new StringBuilder(questionText + "\n\n");
						StringBuilder sb = new StringBuilder("\n\n");
						List<ChecklistCriteria> criteria = question.getCheckListCriterias();
						Collections.sort(criteria);
						for (ChecklistCriteria criterion : criteria) {
							sb.append(criterion.getCriteria() + ", ");
						}
						sb.replace(sb.length() - 2, sb.length(), "");						
						criteriaChunk = new Chunk(sb.toString(), smallItalicFont);						
					}
					
					Paragraph questionParagraph = new Paragraph(questionChunk);											
					if (instructionChunk != null)
					{												
						questionParagraph.add(instructionChunk);						
					}
					if (criteriaChunk != null)
					{																													
						questionParagraph.add(criteriaChunk);						
					}
				
					/*//System.out.println("StudentId: "+studId+ " Question Id: " + question.getId());					
					Iterator<ChecklistOption> checklistAnswerList=question.getCheckListOptions().iterator();
					while(checklistAnswerList.hasNext())
					{
						System.out.println("Answer: " + checklistAnswerList.next().getId());
					}*/
					List<Long> checklistOptionList=Answer.findCheckListOptionsByStudentIdAndQuestionId(studId,question.getId());
					//System.out.println("Answer Selected By User: " + checklistOptionList.size());
					
					PdfPCell questionCell = getPdfCell(questionParagraph, 1, 1);						
					PdfPCell answerCell = getAnswerCell(questionText, question.getCheckListOptions(),checklistOptionList);	
					
					if(j==0)
					{
						questionCell.setBorder(Rectangle.TOP);
						answerCell.setBorder(Rectangle.TOP);
						j++;
					}
					if (i < questions.size()) {
						////questionCell.setBorder(Rectangle.BOTTOM);
						////answerCell.setBorder(Rectangle.BOTTOM);
						questionCell.setPaddingTop(05.0f);
						questionCell.setPaddingBottom(05.0f);						
						questionCell.setBorderWidthBottom(0.5f);
						answerCell.setPaddingTop(05.0f);
						answerCell.setPaddingBottom(05.0f);
						answerCell.setBorderWidthBottom(0.5f);
					}																				
					table.addCell(questionCell);			
					table.addCell(answerCell);
				}
			}			
			table.setWidthPercentage(100.0f);
			return table;
		}
		return null;
	}

	
	private void createCheckListDetailsTable(long studId, Long checklistTabId) {
		isValueAvailable[3] = true;
		CheckList checkList = standardizedRole.getCheckList();

		//List<ChecklistTopic> checklistTopics = checkList.getCheckListTopics();
		List<ChecklistItem> checklistTopics = ChecklistItem.findChecklistTopicByChecklistTab(checklistTabId);

		log.info("CheckList size " + checklistTopics.size());
		
		Paragraph checklistDetails = new Paragraph();

		for (ChecklistItem checklistTopic : checklistTopics) {
			String chkListTitle = checklistTopic.getName();
			if (chkListTitle != null) {
				Paragraph titleParagraph = new Paragraph(new Chunk(chkListTitle, subTitleFont));
				try {
					document.add(titleParagraph);
				} catch (DocumentException e) {
					log.error("in RolePrintPdfUtil.createCheckListDetailsTable(): " + e.getMessage());
				}
			}
			
			String chkListDesc = checklistTopic.getDescription();
			if (chkListDesc != null) {
				Paragraph descriptionParagraph = new Paragraph(new Chunk(chkListDesc, defaultFont));
				////addEmptyLine(descriptionParagraph, 1);				
				try {
					document.add(descriptionParagraph);
				} catch (DocumentException e) {
					log.error("in PdfUtil.addDetails(): " + e.getMessage());
				}
			}
			//select distinct chkque.* from checklist_question chkque,answer ans where chkque.id=ans.checklist_question and  chkque.check_list_topic=1;
			//List<ChecklistQuestion> checkListQuestionList=ChecklistQuestion.findCheckListQuestionByCheckListTopic(checklistTopic.getId());
			//System.out.println("Total Question Size: " + checkListQuestionList.size());
			List<ChecklistItem> checklistQuestionList = ChecklistItem.findChecklistQuestionByChecklistTopic(checklistTopic.getId());
			
			PdfPTable table = createCheckListQuestionTable(checklistQuestionList,studId);			
			//PdfPTable table = createCheckListQuestionTable(checkListQuestionList,studId);
			table.setSpacingBefore(05.0f);
			table.setSpacingAfter(20.0f);
			
			try {
				document.add(table);
			} catch (DocumentException ex) {
				log.error("in RolePrintPdfUtil.createCheckListDetailsTable(): " + ex.getMessage(),ex);
			}
			
		}
	}

	private PdfPTable createFileDetailsTable() {

		PdfPTable fileTable = new PdfPTable(2);

		List<File> files = File.getFilesByStandardizedRoleID(standardizedRole
				.getId());
		log.info("File size " + files.size());
		if (files.size() > 0) {
			isValueAvailable[1] = true;

			fileTable.addCell(getPdfCellBold(constants.filePath()));
			fileTable.addCell(getPdfCellBold(constants.fileDescription()));

			for (File file : files) {

				String fileDesc = (file.getDescription() != null) ? file
						.getDescription() : "-";
				String filePath = (file.getPath() != null) ? file.getPath()
						: "-";
				fileTable.addCell(getPdfCell(fileDesc));
				fileTable.addCell(getPdfCell(filePath, italicFont, 0, 0));
			}

			fileTable.addCell(getPdfCellBold(" "));
			fileTable.addCell(getPdfCell(" "));

			fileTable.setWidthPercentage(100);
		}

		return fileTable;
	}

	private PdfPTable createDetailsTable() {
		PdfPTable table = new PdfPTable(new float[] { 0.2f, 0.3f, 0.2f, 0.3f });

		String shortName = (standardizedRole.getShortName() != null) ? standardizedRole
				.getShortName().toString() : "-";
		String longName = (standardizedRole.getLongName() != null) ? standardizedRole
				.getLongName() : "-";
		String roleType = (standardizedRole.getRoleType() != null) ? enumConstants
				.getString(standardizedRole.getRoleType().toString()) : "-";

		String studyYear = (standardizedRole.getStudyYear() != null) ? enumConstants
				.getString(standardizedRole.getStudyYear().toString()) : "-";

		table.addCell(getPdfCellBold(constants.roleAcronym() + ":"));
		// TODO format date
		table.addCell(getPdfCell(shortName));

		table.addCell(getPdfCellBold(constants.name() + ":"));
		table.addCell(getPdfCell(longName));

		table.addCell(getPdfCellBold(constants.roleType() + ":"));
		table.addCell(getPdfCell(roleType));

		table.addCell(getPdfCellBold(constants.studyYears() + ":"));
		table.addCell(getPdfCell(studyYear));

		table.addCell(getPdfCellBold(" "));
		table.addCell(getPdfCell(" "));
		isValueAvailable[0] = true;

		table.setWidthPercentage(100);
		return table;
	}

	public static void createZipFile(ByteArrayOutputStream pdfOutputStream, OutputStream zipOutputStream, List<String> fileNames)
	 {
		 try {
		      ZipOutputStream zipOut = new ZipOutputStream(zipOutputStream);
		      
		      zipOut.putNextEntry(new ZipEntry(OsMaFilePathConstant.ROLE_FILE_STUDENT_MANAGEMENT_PDF_FORMAT));
			  zipOut.write(pdfOutputStream.toByteArray());	
			  
			 for (String fileName : fileNames) {
				  java.io.File file = new java.io.File(fileName);
				  if(file.exists()){
					  zipOut.putNextEntry(new ZipEntry(file.getName()));
					  zipOut.write(FileUtils.readFileToByteArray(file));
				  }  
			  }
			  zipOut.closeEntry();	            
		      zipOut.close();
		      
		     } catch (FileNotFoundException e) {
		    	 log.info("File does not exists");
		    } catch (IOException e) {
		    	 log.info("IO error");
		 }
		 
	 }
}