package ch.unibas.medizin.osce.server.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.log4j.Logger;

import ch.unibas.medizin.osce.domain.Answer;
import ch.unibas.medizin.osce.domain.ChecklistOption;
import ch.unibas.medizin.osce.domain.ChecklistQuestion;
import ch.unibas.medizin.osce.domain.ChecklistTopic;
import ch.unibas.medizin.osce.domain.ItemAnalysis;
import ch.unibas.medizin.osce.domain.Osce;
import ch.unibas.medizin.osce.domain.OsceDay;
import ch.unibas.medizin.osce.domain.OscePost;
import ch.unibas.medizin.osce.domain.OsceSequence;
import ch.unibas.medizin.osce.domain.PostAnalysis;
import ch.unibas.medizin.osce.server.CalculateCronbachValue;
import ch.unibas.medizin.osce.server.OsMaFilePathConstant;

public class ExportStatisticData extends HttpServlet{

	
	private static Logger Log = Logger.getLogger(UploadServlet.class);
	
	
	  @Override
	  protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException 
	  {
		  String osceId=request.getParameter("osceId");
		  List<String> fileNameList = new ArrayList<String>();
		  String fileName=createCSV(new Long(osceId),request, getServletConfig().getServletContext(), true,fileNameList);
		  
		  ByteArrayOutputStream os = new ByteArrayOutputStream();
		  createZipFile("OsceStatisticData.zip", fileNameList,os);
		  try{
				
				
				
			  resp.setContentType("application/x-download");
				
			  resp.setHeader("Content-Disposition", "attachment; filename=" + "OsceStatisticData.zip");
				
				   

				Log.info("path :" + fileName);
				//String file=OsMaFilePathConstant.ROLE_IMAGE_FILEPATH+path;
				Log.info(" file :" + fileName);
				
				OutputStream out = resp.getOutputStream();
				
				byte[] in = os.toByteArray();
				if (in.length > 0){
				    out.write(in);
				}
				
				File htmlFile=new File(fileName);
				htmlFile.delete();
				out.flush();
			
			}
			catch (Exception e) {
				Log.error(e.getMessage(),e);
			}finally {
				os.close();
			}
	  }
	  
	  
	  /*public String createCSV(Long osceId,HttpServletRequest request)
	  {
		  String path=getServletConfig().getServletContext().getRealPath(OsMaFilePathConstant.assignmentHTML);
	        String fileName=path+System.currentTimeMillis()+".csv";
		  Osce osce=Osce.findOsce(osceId);
			
			List<OsceDay> osceDays=osce.getOsce_days();
			
			for(int i=0;i<osceDays.size();i++)
			{
				OsceDay osceDay=osceDays.get(i);
				List<OsceSequence> osceSequences=osceDay.getOsceSequences();
				
				for(int a=0;a<osceSequences.size();a++)
				{
					OsceSequence osceSeq=osceSequences.get(a);
					
				}
			}
			
			try
			{
				
			    FileWriter writer = new FileWriter(fileName);
			    
			    //column header[start
			    writer.append("examiners");
			    writer.append('|');
			    writer.append("students");
			    writer.append('|');
			    
			    //retrieve distinc Item
			    List<ChecklistQuestion> questions=Answer.retrieveDistinctItems(osceId);
			    
			    for(int i=0;i<questions.size();i++)
			    {
			    	 writer.append("item "+questions.get(i).getId());
			    	 writer.append('|');
			    }
			    writer.append("impression ");
			    writer.append('\n');
		 
			    //column header end]
		 
			    //generate whatever data you want[Start
			    
			    //retrieve data
			    Long lastCandidateId = 0l;
			    
			    List<Answer> csvData=Answer.retrieveExportCsvData(osceId);
			    
			    System.out.println("CSVDATA SIZE : " + csvData.size());
			    for(int i=0;i<csvData.size();i++)
			    {
			    	Answer answer=csvData.get(i);
			    	
			    	if (!lastCandidateId.equals(answer.getStudent().getId()))
			    	{
			    		if (!lastCandidateId.equals(0))
			    		{
			    			//impression item
					    	String impressionItemString=request.getParameter("p"+answer.getOscePostRoom().getOscePost().getId().toString());
					    	if(impressionItemString==null)
					    	 writer.append('0');//impression
					    	else
					    	{
					    		if(impressionItemString.equals("0"))
					    			writer.append('0');
					    		else
					    		{
					    			Answer impressionItem=Answer.findAnswer(answer.getStudent().getId(), new Long(impressionItemString), osceId);
					    			if(impressionItem !=null)
					    				writer.append(impressionItem.getChecklistOption().getValue());
					    			else
					    				writer.append('0');
					    		}
					    	}
			    		}
			    		
			    		writer.append('\n');
			    		
			    		writer.append(answer.getDoctor().getPreName() + " "+ answer.getDoctor().getName());
			    		writer.append('|');
			    		writer.append(answer.getStudent().getPreName() + " "+ answer.getStudent().getName());
			    		writer.append('|');
			    		
			    		lastCandidateId = answer.getStudent().getId();
			    	}
			    	
			    	writer.append(answer.getChecklistOption().getValue());
		    		writer.append('|');
			    	
			    	for(int j=i;j<csvData.size();j++)
			    	{
			    		
			    		writer.append(csvData.get(j).getChecklistOption().getValue());
			    		writer.append('|');
			    		
			    		if((j!=csvData.size()-1 && csvData.get(j).getStudent().getId()!=csvData.get(j+1).getStudent().getId() ))
			    		{
			    			i=j;
			    			 writer.append('0');//impression
			    			 writer.append('\n');
			    			break;
			    		}
			    	}
			    	
			    	for(int j=0;j<questions.size();j++)
			    	{
			    		Answer item=Answer.findAnswer(answer.getStudent().getId(), questions.get(j).getId(), osceId);
			    		if(item==null)//missing
			    		{
			    			writer.append('0');
			    			writer.append('|');
			    		}
			    		else
			    		{
			    			writer.append(item.getChecklistOption().getValue());
				    		writer.append('|');
			    		}
			    	}
			    	
			    	
			    	//impression item
			    	String impressionItemString=request.getParameter("p"+answer.getOscePostRoom().getOscePost().getId().toString());
			    	if(impressionItemString==null)
			    	 writer.append('0');//impression
			    	else
			    	{
			    		if(impressionItemString.equals("0"))
			    			writer.append('0');
			    		else
			    		{
			    			Answer impressionItem=Answer.findAnswer(answer.getStudent().getId(), new Long(impressionItemString), osceId);
			    			if(impressionItem !=null)
			    				writer.append(impressionItem.getChecklistOption().getValue());
			    			else
			    				writer.append('0');
			    		}
			    	}
	    			 
			    	
			    }
			    
			  //generate whatever data you want end]
		 
			    writer.flush();
			    writer.close();
			}
			catch(IOException e)
			{
			     e.printStackTrace();
			} 
			
		  return fileName;
	  }*/
	  
	  /*
	   * flag is used to decide for which this file will generated
	   * If flag is true then it generated for Export and
	   * If flag is false then it generated for Rscript
	  */
	  public static String createCSV(Long osceId,HttpServletRequest request, ServletContext servletContext, Boolean flag,final List<String> fileNameList)
	  {
		  String fileName = "";
		  String zipFileName = "";
		  char alphaSeq = 'A';
		  
		  Long impressionQueId = null;
		  
		  try
		  {
			  String path = servletContext.getRealPath(OsMaFilePathConstant.assignmentHTML);
			  //System.out.println("PATH : " + path);
			  
			  zipFileName = path + "OsceStatisticData.zip";
			  
			  Osce osce=Osce.findOsce(osceId);
				
				List<OsceDay> osceDays=osce.getOsce_days();
				
				CalculateCronbachValue calculateCronbachValue = new CalculateCronbachValue();
				
				for(int i=0;i<osceDays.size();i++)
				{
					OsceDay osceDay=osceDays.get(i);
					List<OsceSequence> osceSequences=osceDay.getOsceSequences();
					
					for(int a=0;a<osceSequences.size();a++)
					{
						OsceSequence osceSeq=osceSequences.get(a);
						
						List<OscePost> oscePostList = osceSeq.getOscePosts();
						
						for (OscePost oscePost : oscePostList)
						{
							fileName = "Day"+ (i+1) + "_" + oscePost.getStandardizedRole().getShortName() + "_" + osceSeq.getLabel() +".csv";
							fileName = path + fileName;
							fileNameList.add(fileName);
							
							//System.out.println("FILE PATH : " + fileName);
							
							FileWriter writer = new FileWriter(fileName);
							writer.append("examiners");
							writer.append('|');
							writer.append("students");
							writer.append('|');
							
							List<Long> postMissingQueList = ItemAnalysis.findDeactivatedItemByOscePostAndOsceSeq(oscePost.getId(), osceSeq.getId());
							String missingQue = "";
							
							List<ChecklistTopic> checklistTopicList = oscePost.getStandardizedRole().getCheckList().getCheckListTopics();
							alphaSeq = 'A';
							
							impressionQueId = null;
							
							String impressionItemString=request.getParameter("p"+oscePost.getId().toString());							
							if (impressionItemString != null)
								impressionQueId=Long.parseLong(impressionItemString);
							
							for (ChecklistTopic checklistTopic : checklistTopicList)
							{
								List<ChecklistQuestion> questionList = ChecklistQuestion.findCheckListQuestionByTopic(checklistTopic.getId());
								
								int count = 1;
								for (ChecklistQuestion question : questionList)
								{
									if ((impressionQueId == null || impressionQueId == 0)  && question.getIsOveralQuestion() != null && question.getIsOveralQuestion())
									{
										impressionQueId = question.getId();
									}
									
									if (flag)
										writer.append(String.valueOf(alphaSeq) + count);
									else
										writer.append("Q" + String.valueOf(question.getId()));
									
									if (flag == true && postMissingQueList.contains(question.getId()))
									{
										if (missingQue == null || missingQue.isEmpty())
											missingQue = String.valueOf(alphaSeq) + count;
										else
											missingQue = missingQue + "," + String.valueOf(alphaSeq) + count;
									}
									
									count += 1;
									//writer.append(question.getId().toString());
									writer.append('|');
								}
								alphaSeq++;
							}
							writer.append("AddPoint");
							writer.append('|');
							writer.append("impression ");
						    //writer.append('\n');
						    
						    List<Answer> answerList = Answer.retrieveExportCsvDataByOscePost(osceDay.getId(), oscePost.getId());
						    Long lastCandidateId = null;
						    Answer answer = null;
						    for (int j=0; j<answerList.size(); j++)
						    {
						    	answer = answerList.get(j);
						    	if (lastCandidateId == null || (!lastCandidateId.equals(answer.getStudent().getId())))
						    	{
						    		if (lastCandidateId != null)
						    		{
						    			if (flag)
						    			{	
						    				Integer addPoint = PostAnalysis.findAddPointByExaminerAndOscePost(oscePost.getId(), answerList.get(j-1).getDoctor().getId());
						    				writer.append(addPoint.toString());
						    				writer.append('|');
						    			}
						    			
						    			if (impressionQueId != null && impressionQueId != 0)
						    			{
						    				Answer impressionItem1=Answer.findAnswer(lastCandidateId, impressionQueId, osceDay.getId());
						    				writer.append(impressionItem1.getChecklistOption().getValue());
						    			}
							    		else
							    		{
							    			writer.append('0');//impression
							    		}
						    		}
						    		
						    		writer.append('\n');
						    		
					    			writer.append("\"" + answer.getDoctor().getPreName() + " " + answer.getDoctor().getName() + "\"");
						    		writer.append('|');
						    		writer.append("\"" + answer.getStudent().getPreName() + " " + answer.getStudent().getName() + "\"");
						    		writer.append('|');
						    		
						    		/*else	
						    		{
						    			writer.append(answer.getDoctor().getPreName() + " " + answer.getDoctor().getName());
						    			writer.append('|');
							    		writer.append(answer.getStudent().getPreName() + " " + answer.getStudent().getName());
							    		writer.append('|');
						    		}	*/	    		
						    		
						    		lastCandidateId = answer.getStudent().getId();
						    	}
						    	
						    	writer.append(answer.getChecklistOption().getValue());
						    	//writer.append(answer.getChecklistQuestion().getId().toString());
					    		writer.append('|');
						    }
						    
						    if (answerList.size() > 0)
						    {
						    	if (flag)
				    			{	
				    				Integer addPoint = PostAnalysis.findAddPointByExaminerAndOscePost(oscePost.getId(), answerList.get(answerList.size()-1).getDoctor().getId());
				    				writer.append(addPoint.toString());
				    				writer.append('|');
				    			}
						    	
						    	if (impressionQueId != null)
				    			{
				    				Answer impressionItem1=Answer.findAnswer(lastCandidateId, impressionQueId, osceDay.getId());
				    				writer.append(impressionItem1.getChecklistOption().getValue());
				    			}
					    		else
					    		{
					    			writer.append('0');//impression
					    		}
						    }
					    	
						    writer.flush();
						    writer.close();
						    
						    if (flag)
						    	calculateCronbachValue.calculatePassFail(fileName, missingQue);
						}
					}
				}
				
//				createZipFile(zipFileName, fileNameList, path);
		  }
		  catch(Exception e)
		  {
			  Log.error(e.getMessage(),e);
		  }
		 
		  return zipFileName;
	  }
	  
	 public static void createZipFile(String zipFilePath, List<String> fileNameList,OutputStream os)
	 {
				       
		    try {
		      ZipOutputStream zipOut = new ZipOutputStream(os);
		      
		      for (int i = 0 ; i < fileNameList.size() ; i ++) {
		        
					byte[] buf = new byte[1024];
					int len;
					File file = new File(fileNameList.get(i));
					FileInputStream in = new FileInputStream(file);
					zipOut.putNextEntry(new ZipEntry(file.getName()));
					
					while ((len = in.read(buf)) > 0) {
						zipOut.write(buf, 0, len);
					}
					
					in.close();
					
		            zipOut.closeEntry();	            
		            
		            if(file.exists())
		            {
		            	file.delete();
		            }
		      }
		  
		      zipOut.close();
		      
		      Log.info("Done...");
		      
		    } catch (FileNotFoundException e) {
		    	Log.error(e.getMessage(),e);
		    } catch (IOException e) {
		    	Log.error(e.getMessage(),e);
		    }
	 }

	 public static String createExaminerCSV(HttpServletRequest request, ServletContext servletContext, Long oscePostId, Long examinerId, String fileName, Integer addPoint)
	  {
		  
		  Long impressionQueId = null;
		  
		  try
		  {
  				String path = servletContext.getRealPath(OsMaFilePathConstant.assignmentHTML);

				fileName = path + fileName;
				
				//System.out.println("FILE PATH : " + fileName);
				
				FileWriter writer = new FileWriter(fileName);
				writer.append("examiners");
				writer.append('|');
				writer.append("students");
				writer.append('|');
				
				OscePost oscePost = OscePost.findOscePost(oscePostId);
				
				List<ChecklistTopic> checklistTopicList = oscePost.getStandardizedRole().getCheckList().getCheckListTopics();
				
				impressionQueId = null;
				
				String impressionItemString=request.getParameter("p"+oscePost.getId().toString());							
				
				if (impressionItemString != null)
					impressionQueId=Long.parseLong(impressionItemString);
				
				for (ChecklistTopic checklistTopic : checklistTopicList)
				{
					List<ChecklistQuestion> questionList = ChecklistQuestion.findCheckListQuestionByTopic(checklistTopic.getId());
					
					int count = 1;
					for (ChecklistQuestion question : questionList)
					{
						if ((impressionQueId == null || impressionQueId == 0)  && question.getIsOveralQuestion() != null && question.getIsOveralQuestion())
						{
							impressionQueId = question.getId();
						}
						
						writer.append("Q" + String.valueOf(question.getId()));
						
						writer.append('|');
					}
				}
				writer.append("AddPoint");
				writer.append('|');
				writer.append("impression ");
			    
			    List<Answer> answerList = Answer.retrieveExportCsvDataByOscePostAndExaminer(oscePostId, examinerId);
			    Long lastCandidateId = null;
			    Answer answer = null;
			    for (int j=0; j<answerList.size(); j++)
			    {
			    	answer = answerList.get(j);
			    	if (lastCandidateId == null || (!lastCandidateId.equals(answer.getStudent().getId())))
			    	{
			    		if (lastCandidateId != null)
			    		{
			    			if (impressionQueId != null && impressionQueId != 0)
			    			{
			    				writer.append(addPoint.toString());
			    				writer.append('|');
			    				
			    				Answer impressionItem1=Answer.findAnswer(lastCandidateId, impressionQueId, oscePost.getOsceSequence().getOsceDay().getId());
			    				writer.append(impressionItem1.getChecklistOption().getValue());
			    			}
				    		else
				    		{
				    			writer.append(addPoint.toString());
			    				writer.append('|');
				    			writer.append('0');//impression
				    		}
			    		}
			    		
			    		writer.append('\n');
		    			writer.append("\"" + answer.getDoctor().getPreName() + " " + answer.getDoctor().getName() + "\"");
			    		writer.append('|');
			    		writer.append("\"" + answer.getStudent().getPreName() + " " + answer.getStudent().getName() + "\"");
			    		writer.append('|');
			    			    		
			    		
			    		lastCandidateId = answer.getStudent().getId();
			    	}
			    	
			    	writer.append(answer.getChecklistOption().getValue());
			    	writer.append('|');
			    }
			    
			    if (answerList.size() > 0)
			    {
			    	if (impressionQueId != null)
	    			{
			    		writer.append(addPoint.toString());
	    				writer.append('|');
	    				Answer impressionItem1=Answer.findAnswer(lastCandidateId, impressionQueId, oscePost.getOsceSequence().getOsceDay().getId());
	    				writer.append(impressionItem1.getChecklistOption().getValue());
	    			}
		    		else
		    		{
		    			writer.append(addPoint.toString());
	    				writer.append('|');
		    			writer.append('0');//impression
		    		}
			    }
		    	
			    writer.flush();
			    writer.close();
							
			    return fileName;			    
		  }
		  catch(Exception e)
		  {
			  Log.error(e.getMessage(),e);
		  }
		 
		  return fileName;
	  }
	 
	 public static String createOscePostCSV(HttpServletRequest request, ServletContext servletContext, Long oscePostId, String fileName, List<String> examinerId, List<Integer> addPoints)
	  {
		  
		  Long impressionQueId = null;
		  
		  try
		  {
  				String path = servletContext.getRealPath(OsMaFilePathConstant.assignmentHTML);
  				fileName = path + fileName;
				
				//System.out.println("FILE PATH : " + fileName);
				
				FileWriter writer = new FileWriter(fileName);
				writer.append("examiners");
				writer.append('|');
				writer.append("students");
				writer.append('|');
				
				OscePost oscePost = OscePost.findOscePost(oscePostId);
				
				List<ChecklistTopic> checklistTopicList = oscePost.getStandardizedRole().getCheckList().getCheckListTopics();
				
				impressionQueId = null;
				
				String impressionItemString=request.getParameter("p"+oscePost.getId().toString());							
				if (impressionItemString != null)
					impressionQueId=Long.parseLong(impressionItemString);
				
				for (ChecklistTopic checklistTopic : checklistTopicList)
				{
					List<ChecklistQuestion> questionList = ChecklistQuestion.findCheckListQuestionByTopic(checklistTopic.getId());
					
					int count = 1;
					for (ChecklistQuestion question : questionList)
					{
						if ((impressionQueId == null || impressionQueId == 0)  && question.getIsOveralQuestion() != null && question.getIsOveralQuestion())
						{
							impressionQueId = question.getId();
						}
						
						writer.append("Q" + String.valueOf(question.getId()));
						
						writer.append('|');
					}
					
				}
				writer.append("AddPoint");
				writer.append('|');
				writer.append("impression ");
			   
			    List<Answer> answerList = Answer.retrieveExportCsvDataByOscePost(oscePost.getOsceSequence().getOsceDay().getOsce().getId(), oscePost.getId());
			    Long lastCandidateId = null;
			    Answer answer = null;
			    for (int j=0; j<answerList.size(); j++)
			    {
			    	answer = answerList.get(j);
			    	if (lastCandidateId == null || (!lastCandidateId.equals(answer.getStudent().getId())))
			    	{
			    		if (lastCandidateId != null)
			    		{
			    			Integer addPoint = 0;
			    			String key="p"+oscePostId+"e"+answerList.get(j-1).getDoctor().getId();
			    			if (examinerId.contains(key))
			    			{
			    				int index = examinerId.indexOf(key);
			    				addPoint = addPoints.get(index);
			    			}
			    			
			    			if (impressionQueId != null && impressionQueId != 0)
			    			{
			    				writer.append(addPoint.toString());
			    				writer.append('|');
			    				
			    				Answer impressionItem1=Answer.findAnswer(lastCandidateId, impressionQueId, oscePost.getOsceSequence().getOsceDay().getId());
			    				writer.append(impressionItem1.getChecklistOption().getValue());
			    			}
				    		else
				    		{
				    			writer.append(addPoint.toString());
			    				writer.append('|');
			    				
				    			writer.append('0');//impression
				    		}
			    			
			    		}
			    		
			    		writer.append('\n');
			    		
		    			writer.append("\"" + answer.getDoctor().getPreName() + " " + answer.getDoctor().getName() + "\"");
			    		writer.append('|');
			    		writer.append("\"" + answer.getStudent().getPreName() + " " + answer.getStudent().getName() + "\"");
			    		writer.append('|');
			    		
			    		lastCandidateId = answer.getStudent().getId();
			    	}
			    	
			    	writer.append(answer.getChecklistOption().getValue());
			    	writer.append('|');
			    }
			    
			    if (answerList.size() > 0)
			    {
			    	answer = answerList.get(answerList.size() - 1);
			    	Integer addPoint = 0;
	    			String key="p"+oscePostId+"e"+answer.getDoctor().getId();
	    			if (examinerId.contains(key))
	    			{
	    				int index = examinerId.indexOf(key);
	    				addPoint = addPoints.get(index);
	    			}
			    	
			    	if (impressionQueId != null)
	    			{
			    		writer.append(addPoint.toString());
	    				writer.append('|');
	    				
	    				Answer impressionItem1=Answer.findAnswer(lastCandidateId, impressionQueId, oscePost.getOsceSequence().getOsceDay().getId());
	    				writer.append(impressionItem1.getChecklistOption().getValue());
	    			}
		    		else
		    		{
		    			writer.append(addPoint.toString());
	    				writer.append('|');
	    				
		    			writer.append('0');//impression
		    		}
			    	
			    }
		    	
			    writer.flush();
			    writer.close();
		  }
		  catch(Exception e)
		  {
			  Log.error(e.getMessage(),e);
		  }
		 
		  return fileName;
	  }

	 public static String createOscePostGraphCSV(HttpServletRequest request, ServletContext servletContext, String fileName, OscePost oscePost)
	  {
		  
		  char alphaSeq = 'A';
		  Long impressionQueId = null;
		  
		  try
		  {
			  	String path = servletContext.getRealPath(OsMaFilePathConstant.assignmentHTML);
				
				CalculateCronbachValue calculateCronbachValue = new CalculateCronbachValue();
	
				fileName = path + fileName;
				
				FileWriter writer = new FileWriter(fileName);
				writer.append("examiners");
				writer.append('|');
				writer.append("students");
				writer.append('|');
				
				List<Long> postMissingQueList = ItemAnalysis.findDeactivatedItemByOscePostAndOsceSeq(oscePost.getId(), oscePost.getOsceSequence().getId());
				String missingQue = "";
				
				List<ChecklistTopic> checklistTopicList = oscePost.getStandardizedRole().getCheckList().getCheckListTopics();
				alphaSeq = 'A';
				
				impressionQueId = null;
				
				String impressionItemString=request.getParameter("p"+oscePost.getId().toString());							
				if (impressionItemString != null)
					impressionQueId=Long.parseLong(impressionItemString);
				
				for (ChecklistTopic checklistTopic : checklistTopicList)
				{
					List<ChecklistQuestion> questionList = ChecklistQuestion.findCheckListQuestionByTopic(checklistTopic.getId());
					
					int count = 1;
					for (ChecklistQuestion question : questionList)
					{
						if ((impressionQueId == null || impressionQueId == 0)  && question.getIsOveralQuestion() != null && question.getIsOveralQuestion())
						{
							impressionQueId = question.getId();
						}
						
						writer.append("Q" + String.valueOf(question.getId()));
						
						if (postMissingQueList.contains(question.getId()))
						{
							if (missingQue == null || missingQue.isEmpty())
								missingQue = "Q" + String.valueOf(question.getId());
							else
								missingQue = missingQue + "," + "Q" + String.valueOf(question.getId());
						}
						
						count += 1;
						//writer.append(question.getId().toString());
						writer.append('|');
					}
					alphaSeq++;
				}
				writer.append("AddPoint");
				writer.append('|');
				writer.append("impression ");
			    //writer.append('\n');
			    
			    List<Answer> answerList = Answer.retrieveExportCsvDataByOscePost(oscePost.getOsceSequence().getOsceDay().getId(), oscePost.getId());
			    Long lastCandidateId = null;
			    Answer answer = null;
			    for (int j=0; j<answerList.size(); j++)
			    {
			    	answer = answerList.get(j);
			    	if (lastCandidateId == null || (!lastCandidateId.equals(answer.getStudent().getId())))
			    	{
			    		if (lastCandidateId != null)
			    		{
		    				Integer addPoint = PostAnalysis.findAddPointByExaminerAndOscePost(oscePost.getId(), answerList.get(j-1).getDoctor().getId());
		    				writer.append(addPoint.toString());
		    				writer.append('|');
			    			
			    			
			    			if (impressionQueId != null && impressionQueId != 0)
			    			{
			    				Answer impressionItem1=Answer.findAnswer(lastCandidateId, impressionQueId, oscePost.getOsceSequence().getOsceDay().getId());
			    				writer.append(impressionItem1.getChecklistOption().getValue());
			    			}
				    		else
				    		{
				    			writer.append('0');//impression
				    		}
			    		}
			    		
			    		writer.append('\n');
			    		
		    			writer.append("\"" + answer.getDoctor().getPreName() + " " + answer.getDoctor().getName() + "\"");
			    		writer.append('|');
			    		writer.append("\"" + answer.getStudent().getPreName() + " " + answer.getStudent().getName() + "\"");
			    		writer.append('|');
			    		
			    		lastCandidateId = answer.getStudent().getId();
			    	}
			    	
			    	writer.append(answer.getChecklistOption().getValue());
			    	//writer.append(answer.getChecklistQuestion().getId().toString());
		    		writer.append('|');
			    }
			    
			    if (answerList.size() > 0)
			    {
		    		Integer addPoint = PostAnalysis.findAddPointByExaminerAndOscePost(oscePost.getId(), answerList.get(answerList.size()-1).getDoctor().getId());
    				writer.append(addPoint.toString());
    				writer.append('|');
	    			
			    	
			    	if (impressionQueId != null)
	    			{
	    				Answer impressionItem1=Answer.findAnswer(lastCandidateId, impressionQueId, oscePost.getOsceSequence().getOsceDay().getId());
	    				writer.append(impressionItem1.getChecklistOption().getValue());
	    			}
		    		else
		    		{
		    			writer.append('0');//impression
		    		}
			    }
		    	
			    writer.flush();
			    writer.close();
			    
			    List<Long> valueList = new ArrayList<Long>();
			    valueList = ChecklistOption.findCheckListOptionValueByQuestion(impressionQueId);
			    
			    Collections.sort(valueList);
			    
			    if (valueList.size() > 0)
			    	calculateCronbachValue.createGraph(fileName, missingQue, valueList.get(0), valueList.get(valueList.size()-1));
			    else
			    	calculateCronbachValue.createGraph(fileName, missingQue, 0l, 0l);
			   	
			   	fileName = fileName.replace(".csv", ".png");

		  }
		  catch(Exception e)
		  {
			  Log.error(e.getMessage(),e);
		  }
		 
		  return fileName;
	  }
}
