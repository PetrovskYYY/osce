package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ch.unibas.medizin.osce.client.IndividualScheduleService;
import ch.unibas.medizin.osce.client.IndividualScheduleServiceAsync;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.IndividualSchedulesDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.IndividualSchedulesDetailsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.IndividualSchedulesDetailsViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.PrintTemplatePopupViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenHandler;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickEvent;
import ch.unibas.medizin.osce.client.managed.request.AssignmentProxy;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.PatientInRoleProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.managed.request.StudentProxy;
import ch.unibas.medizin.osce.shared.TemplateTypes;
import ch.unibas.medizin.osce.shared.util;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.CheckBox;



@SuppressWarnings("deprecation")
public class IndividualSchedulesDetailsActivity extends AbstractActivity implements
IndividualSchedulesDetailsView.Presenter, 
IndividualSchedulesDetailsView.Delegate
{
	private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
		private IndividualSchedulesDetailsView view;
		//private SummoningsServiceAsync summoningsServiceAsync = 
		
		private final OsceConstants constants;
		private OsceProxy osceProxy;
		OsceProxy osceProxyforTemplate;
		List<CheckBox> lstChkStud;
		List<CheckBox> lstChkSp;
		List<CheckBox> lstChkExaminor;
		CheckBox chkAllSP;
		CheckBox chkAllStud;
		CheckBox chkAllExaminor;
		List<StandardizedPatientProxy> lstStandPatProxy;
		List<Long> spId;
		List<Long> studId;
		List<Long> examinorId;
		List<String> lstSPStandPatName;
		List<String> lstSPStandPatPreName;
		List<String> lstSPOsce;
		List<Long> lstSPTempPatientInRole;
		List<List<Long>> lstSPPatientInRole;
		/*List<String> lstSPTempStartTime;
		List<List<String>> lstSPStartTime;
		List<String> lstSPTempEndTime;
		List<List<String>> lstSPEndTime;*/
		List<AssignmentProxy> lstAssignment;
		//SummoningsPopupViewImpl popupStud;
		
		List<DoctorProxy> lstExaminerProxy;
		List<StudentProxy> lstStudentProxy;
		IndividualSchedulesDetailsActivity individualSchedulesDetailsActivity;		
		Map templateSPVariables = null;		
		
		private IndividualSchedulesDetailsPlace place;
		private IndividualSchedulesDetailsActivity activity;
		private final IndividualScheduleServiceAsync spSummoningsServiceAsync = GWT.create(IndividualScheduleService.class);

		public IndividualSchedulesDetailsActivity(IndividualSchedulesDetailsPlace place, OsMaRequestFactory requests, PlaceController placeController) 
		{
			Log.info("Call IndividualSchedulesDetailsActivity(3arg)");
			this.place = place;
	    	this.requests = requests;
	    	this.placeController = placeController;
	    	this.activity=this;
	    	this.individualSchedulesDetailsActivity=this;
	    	constants = GWT.create(OsceConstants.class);		    	
	    }
		
		public void onStop()
		{
			
		}

		@Override
		public void start(AcceptsOneWidget panel, EventBus eventBus) 
		{
			Log.info("IndividualSchedulesDetailsActivity.start()");
			final IndividualSchedulesDetailsView individualSchedulesDetailsView = new IndividualSchedulesDetailsViewImpl();			
			individualSchedulesDetailsView.setPresenter(this);			
			this.widget = panel;
			this.view = individualSchedulesDetailsView;
			widget.setWidget(individualSchedulesDetailsView.asWidget());			
			view.setDelegate(this);			
			individualSchedulesDetailsView.setDelegate(this);	
			
			MenuClickEvent.register(requests.getEventBus(), (IndividualSchedulesDetailsViewImpl)view);
			
			ApplicationLoadingScreenEvent.initialCounter();
			ApplicationLoadingScreenEvent.register(requests.getEventBus(),
					new ApplicationLoadingScreenHandler() {
						@Override
						public void onEventReceived(
								ApplicationLoadingScreenEvent event) {
							Log.info("ApplicationLoadingScreenEvent onEventReceived Called");
							event.display();
						}
					});						
			
			init();
			
		}
	
				
		private void init() 
		{
			Log.info("Call Init()");
			requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
			
			requests.find(place.getProxyId()).fire(new OSCEReceiver<Object>() {
				@Override
				public void onSuccess(Object response) {
						if(response instanceof OsceProxy && response != null)
						{						
							osceProxy=(OsceProxy)response;
							Log.info("Get Response in Detail with Size: " + osceProxy.getId());
							
							initSP(osceProxy); // Initialize View for SP
							initStudent(osceProxy.getId()); // Initialize View for Student
							initExaminor(osceProxy.getId()); // Initialize View for Doctor		
							requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
						}
				}

			});
			
			Log.info("Osce Id: " + place.getProxyId());				
			
			view.getExaminerRb().setChecked(true);
			view.getSpRb().setChecked(true);
			view.getStudRb().setChecked(true);
			
			view.getDisclosureSPPanel().getHeaderTextAccessor().setText(constants.standardizedPatient());
			view.getDisclosureStudentPanel().getHeaderTextAccessor().setText(constants.student());
			view.getDisclosureExaminerPanel().getHeaderTextAccessor().setText(constants.examiner());
		
			
			
			
			view.getSpRb().addClickHandler(new ClickHandler() 
			{				
				@Override
				public void onClick(ClickEvent event) 
				{
					//Log.info("~~~Total Widget view.getVpSP().getWidgetCount()"+ view.getVpSP().getWidgetCount());
					if(view.getVpSP().getWidgetCount()>0)
					{
						Log.info("True");
						if(chkAllSP.isChecked()==true)
						{
							
							view.getSpAllRb().setChecked(true);
						}
					}
					else
					{
						Log.info("False");
					}
				}
			});
			
			view.getStudRb().addClickHandler(new ClickHandler() 
			{
				@Override
				public void onClick(ClickEvent event) 
				{
					//Log.info("~~~Total Widget view.getVpSP().getWidgetCount()"+ view.getVpStudent().getWidgetCount());
					if(view.getVpStudent().getWidgetCount()>0)
					{
						Log.info("True");
						if(chkAllStud.isChecked()==true )
						{
							view.getStudAllRb().setChecked(true);
						}
					}
					else
					{
						Log.info("False");
					}
				}
			});
			
			view.getExaminerRb().addClickHandler(new ClickHandler() 
			{
				@Override
				public void onClick(ClickEvent event) 
				{
					//Log.info("~~~Total Widget view.getVpSP().getWidgetCount()"+ view.getVpExaminor().getWidgetCount());
					if(view.getVpExaminor().getWidgetCount()>0)
					{
						Log.info("True");
						if(chkAllExaminor.isChecked()==true)
						{
							view.getExaminerAllRb().setChecked(true);
						}
					}
					else
					{
						Log.info("False");
					}
										
				}
			});
		}
		
		private void initSP(final OsceProxy osceProxy) 
		{			
			Log.info("Call initSP for Osce Id: " + osceProxy.getId());
			lstChkSp=new ArrayList<CheckBox>();
			lstStandPatProxy=new ArrayList<StandardizedPatientProxy>();			
			/*lstSPTempStartTime=new ArrayList<String>();
			lstSPStartTime=new ArrayList<List<String>>();
			lstSPTempEndTime=new ArrayList<String>();
			lstSPEndTime=new ArrayList<List<String>>();*/
			
			requests.standardizedPatientRequestNonRoo().findPatientsByOsceId(osceProxy.getId()).with("patientInSemester","patientInSemester.patientInRole","patientInSemester.patientInRole.assignments","patientInSemester.patientInRole.oscePost","patientInSemester.patientInRole.oscePost.standardizedRole").fire(new OSCEReceiver<List<StandardizedPatientProxy>>() {

				@Override
				public void onSuccess(List<StandardizedPatientProxy> response) 
				{
					Log.info("Get List of SP with Size: " + response.size());		
					
						Iterator<StandardizedPatientProxy> iteratorSP=response.iterator();												
						if(response.size()>0)
						{
							Log.info("asd");
							chkAllSP=new CheckBox(constants.all());									
							view.getDataVPSP().addStyleName("schedulePanelStyle");
							view.getDisclosureSPPanel().getHeaderTextAccessor().setText(constants.standardizedPatient());
							view.getVpSP().insert(chkAllSP, view.getVpSP().getWidgetCount());
														
							chkAllSP.addClickHandler(new ClickHandler() 
							{
								@Override
								public void onClick(ClickEvent event) 
								{
									Log.info("Select/Deselect All SP");									
									checkedUncheckedAll(lstChkSp,chkAllSP);
								}								
							});
						}
						while(iteratorSP.hasNext())
						{
							Log.info("Panel Widget: " + view.getVpSP().getWidgetCount());
							
							final CheckBox chkSP=new CheckBox();																					
							
							final StandardizedPatientProxy sp =iteratorSP.next();
																												
							lstStandPatProxy.add(sp);
							//lstSPStandPatName.add(util.getEmptyIfNull(sp.getName()));
							//lstSPStandPatPreName.add(util.getEmptyIfNull(sp.getPreName()));								
							
							
							/*requests.osceRequest().findOsce(osceProxy.getId()).with("semester").fire(new OSCEReceiver<Object>() {

								@Override
								public void onSuccess(Object response) 
								{
									Log.info("Success....");
									lstSPOsce.add(util.getEmptyIfNull(osceProxy.getStudyYear())+" " + util.getEmptyIfNull(((OsceProxy)response).getSemester().getSemester().name()));									
								}
							});
							
							requests.patientInRoleRequestNonRoo().findPatientsInRoleForAssignmentBySPIdandSemesterId(sp.getId(),IndividualSchedulesActivity.semesterProxyForDetail.getId()).fire(new OSCEReceiver<List<PatientInRoleProxy>>() 
							{

								@Override
								public void onSuccess(List<PatientInRoleProxy> response) 
								{
									Log.info("Success..");
									Log.info("findPatientsInRoleForAssignmentBySPIdandSemesterId");
									Log.info("Response Size: " + response.size() + "for Id: " + sp.getId());
									if(response.size()>0)
									{										
										lstSPTempPatientInRole=new ArrayList<Long>();
										for(int i=0;i<response.size();i++)
										{									
											Log.info("PatientInRoleId: " + response.get(i).getId());
											lstSPTempPatientInRole.add(response.get(i).getId());
										}
										Iterator<PatientInRoleProxy> iteratorPatientInRole=response.iterator();
										while(iteratorPatientInRole.hasNext())
										{
											PatientInRoleProxy pir=iteratorPatientInRole.next();
											Log.info("PatientInRoleId: " + pir.getId());
											lstSPTempPatientInRole.add(pir.getId().toString());
										}
										Log.info("PatientInRole List Size: " + lstSPTempPatientInRole.size());
										lstSPPatientInRole.add(lstSPTempPatientInRole);
										
									}									
								}
							});*/
							
							/*requests.assignmentRequestNonRoo().findAssignmentsBySPIdandSemesterId(sp.getId(),IndividualSchedulesActivity.semesterProxyForDetail.getId()).fire(new OSCEReceiver<List<AssignmentProxy>>() 
							{
								@Override
								public void onSuccess(List<AssignmentProxy> response) 
								{																
										Log.info("Success Call");										
										Log.info("Assignment Id: " + response.size());	
										if(response.size()>0)
										{
											Iterator<AssignmentProxy> assignmentToSP=response.iterator();
											while(assignmentToSP.hasNext())
											{
												AssignmentProxy ap=assignmentToSP.next();
												Log.info("Loop Assignment Id: " + ap.getId());
												Log.info("Loop Assignment Start Time: " + ap.getTimeStart());												
												Log.info("Loop Assignment End Time: " + ap.getTimeEnd());
												
												lstSPTempStartTime.add(ap.getTimeStart().toString());
												lstSPTempEndTime.add(ap.getTimeStart().toString());
												
											}
										}
								}
							});*/
							
							/*lstSPStartTime.add(lstSPTempStartTime);
							lstSPEndTime.add(lstSPTempEndTime);*/
							
							lstChkSp.add(chkSP);
							
							chkSP.setText(util.getEmptyIfNull(sp.getName())+" " +util.getEmptyIfNull(sp.getPreName()));							
							
							view.getVpSP().insert(chkSP, view.getVpSP().getWidgetCount());	
							chkSP.addClickHandler(new ClickHandler() {
								
								@Override
								public void onClick(ClickEvent event) 
								{
									if(chkSP.isChecked()==false && chkAllSP.isChecked()==true)
									{
										chkAllSP.setChecked(false);	
										view.getSpRb().setChecked(true);
									}
								}
							});
						}
						
				}
			});
		}
		
		private void initStudent(Long id) 
		{
			Log.info("Call initStudent for Osce Id: " + id);
			lstChkStud=new ArrayList<CheckBox>();
			lstStudentProxy=new ArrayList<StudentProxy>();	
			
			requests.studentRequestNonRoo().findStudentByOsceId(id).fire(new OSCEReceiver<List<StudentProxy>>() {
				
				@Override
				public void onSuccess(List<StudentProxy> response) 
				{
					Log.info("Get List of Student with Size: " + response.size());		
					
					Iterator<StudentProxy> iteratorStud=response.iterator();		
					if(response.size()>0)
					{
						chkAllStud=new CheckBox(constants.all());							
						view.getDataVPStud().addStyleName("schedulePanelStyle");
						view.getDisclosureStudentPanel().getHeaderTextAccessor().setText(constants.student());
						
						view.getVpStudent().insert(chkAllStud, view.getVpStudent().getWidgetCount());
						chkAllStud.addClickHandler(new ClickHandler() 
						{
							@Override
							public void onClick(ClickEvent event) 
							{
								Log.info("Select/Deselect All Stud");
								//checkedAllStud();
								checkedUncheckedAll(lstChkStud,chkAllStud);
							}
						});
					}
					
					while(iteratorStud.hasNext())
					{
						Log.info("Panel Widget: " + view.getVpStudent().getWidgetCount());
						
						final CheckBox chkStud=new CheckBox();								
						StudentProxy s =iteratorStud.next();						
						
						chkStud.setText(util.getEmptyIfNull(s.getName())+" " +util.getEmptyIfNull(s.getPreName()));
						chkStud.setWidth("320px");
						lstChkStud.add(chkStud);
						lstStudentProxy.add(s);
												
						view.getVpStudent().insert(chkStud, view.getVpStudent().getWidgetCount());	
						
						chkStud.addClickHandler(new ClickHandler() {
							
							@Override
							public void onClick(ClickEvent event) 
							{
								if(chkStud.isChecked()==false && chkAllStud.isChecked()==true)
								{
									chkAllStud.setChecked(false);	
									view.getStudRb().setChecked(true);
								}
							}
						});
					}
				}
			});
		}
		
		private void initExaminor(Long id) 
		{
			Log.info("Call initExaminor for Osce Id: " + id);
			lstChkExaminor=new ArrayList<CheckBox>();
			lstExaminerProxy=new ArrayList<DoctorProxy>();	
			requests.doctorRequestNonRoo().findDoctorByOsceId(id).fire(new OSCEReceiver<List<DoctorProxy>>() 
			{
				@Override
				public void onSuccess(List<DoctorProxy> response) 
				{
						Log.info("Get List of Examinor(Doctor) with Size: " + response.size());		

						Iterator<DoctorProxy> iteratorExaminor=response.iterator();		
						if(response.size()>0)
						{
							chkAllExaminor=new CheckBox(constants.all());							
							view.getDataVPExaminer().addStyleName("schedulePanelStyle");
							view.getDisclosureExaminerPanel().getHeaderTextAccessor().setText(constants.examiner());
							view.getVpExaminor().insert(chkAllExaminor, view.getVpExaminor().getWidgetCount());
							chkAllExaminor.addClickHandler(new ClickHandler() 
							{
								@Override
								public void onClick(ClickEvent event) 
								{
									Log.info("Select/Deselect All Examinor");
									//checkedAllExaminor();
									checkedUncheckedAll(lstChkExaminor,chkAllExaminor);
								}

								
							});
						}
						while(iteratorExaminor.hasNext())
						{
							Log.info("Panel Widget: " + view.getVpExaminor().getWidgetCount());
							
							final CheckBox chkExaminor=new CheckBox();							
							DoctorProxy s =iteratorExaminor.next();						
							
							chkExaminor.setText(util.getEmptyIfNull(s.getName())+" " +util.getEmptyIfNull(s.getPreName()));
							
							lstChkExaminor.add(chkExaminor);
							lstExaminerProxy.add(s);
							
							view.getVpExaminor().insert(chkExaminor, view.getVpExaminor().getWidgetCount());
							chkExaminor.addClickHandler(new ClickHandler() {
								
								@Override
								public void onClick(ClickEvent event) 
								{
									if(chkExaminor.isChecked()==false && chkAllExaminor.isChecked()==true)
									{
										chkAllExaminor.setChecked(false);	
										view.getExaminerRb().setChecked(true);
									}
								}
							});
						}
				}
			});
		}	
		
		private void checkedUncheckedAll(List<CheckBox> list, CheckBox chkbox) 
		{
			Log.info("Call checkedUncheckedAll");		
			if(list.size()>0)
			{
					if(chkbox.isChecked()==true)
					{			
						Log.info("Select All");
						
							if(chkbox.equals(chkAllSP))
							{							
								view.getSpAllRb().setChecked(true);
							}
							else if(chkbox.equals(chkAllExaminor))
							{							
								view.getExaminerAllRb().setChecked(true);
							}
							else if(chkbox.equals(chkAllStud))
							{								
								view.getStudAllRb().setChecked(true);
							}
						
						Iterator<CheckBox> tempIterator=list.iterator();
						while(tempIterator.hasNext())
						{																
							tempIterator.next().setChecked(true);
						}						
					}
					else if(chkbox.isChecked()==false)
					{
						Log.info("Deselect All");
						
						if(chkbox.equals(chkAllSP))
						{						
							view.getSpRb().setChecked(true);
						}
						else if(chkbox.equals(chkAllExaminor))
						{						
							view.getExaminerRb().setChecked(true);
						}
						else if(chkbox.equals(chkAllStud))
						{							
							view.getStudRb().setChecked(true);
						}
						
						Iterator<CheckBox> tempIterator=list.iterator();
						while(tempIterator.hasNext())
						{																
							tempIterator.next().setChecked(false);
						}
					}
			}			
		}
		
		@Override
		public void goTo(Place place) 
		{
			placeController.goTo(place);
			
		}
			
		@Override
		public void clickAllSP() 
		{
			Log.info("Call printAllSP.");
			if(lstChkSp.size()>0)
			{
				chkAllSP.setChecked(true);
				Iterator<CheckBox> tempIterator=lstChkSp.iterator();
				while(tempIterator.hasNext())
				{																
					tempIterator.next().setChecked(true);
				}
			}									
		}

		@Override
		public void clickAllStud() 
		{
			Log.info("Call printAllStud.");
			if(lstChkStud.size()>0)
			{
				chkAllStud.setChecked(true);
				Iterator<CheckBox> tempIterator=lstChkStud.iterator();
				while(tempIterator.hasNext())
				{																
					tempIterator.next().setChecked(true);
				}
			}					
			
		}

		
		@Override
		public void clickAllExaminor() 
		{
			Log.info("Call printAllExaminor.");
			if(lstChkExaminor.size()>0)
			{
				chkAllExaminor.setChecked(true);
				Iterator<CheckBox> tempIterator=lstChkExaminor.iterator();
				while(tempIterator.hasNext())
				{																
					tempIterator.next().setChecked(true);
				}
			}			
			
		}
		
		@Override
		public void clickSelectedSP() 
		{
						
		}
		
		@Override
		public void clickSelectedStud() 
		{
			
		}

		@Override
		public void clickSelectedExaminor() 
		{
			
		}
		
		@Override
		public void printCopyforStud(ClickEvent event) 
		{
			Log.info("Call printCopyforStud()");			
						
			studId=new ArrayList<Long>();
			if(lstChkStud.size()>0 && lstStudentProxy.size()>0) // Check that SP is Checked Or Not
			{
				Log.info("Total Stud : " + lstChkStud.size());										
				int i=0;
				for(Iterator<CheckBox> iterator=lstChkStud.iterator();iterator.hasNext();i++)
				{					
					if(iterator.next().isChecked()==true)
					{
						Log.info("~~Selected Checkbox Id: " + i);
						Log.info("~~Print Plan for Stud: " + lstStudentProxy.get(i).getName());
						
						studId.add(lstStudentProxy.get(i).getId());												
					}
				}
				
				if(studId.size()>0)
				{
					initStudPopup();	
				}
				else
				{
					MessageConfirmationDialogBox dialogStud=new MessageConfirmationDialogBox(constants.warning());
					dialogStud.showConfirmationDialog("Please Select atleast one Student");
				}
				
			}
			
			else
			{
				MessageConfirmationDialogBox dialog=new MessageConfirmationDialogBox("Warning");
				dialog.showConfirmationDialog("Please Select Student..");
			}
			
		}
		
		private void initStudPopup() 
		{
			
			Log.info("Call initStudPopup");
			
			final PrintTemplatePopupViewImpl popupStud=new PrintTemplatePopupViewImpl();							
			Log.info("get Stud Template Content.");
			
			requests.osceRequestNonRoo( ).findAllOsceBySemster(IndividualSchedulesActivity.semesterProxyForDetail.getId()).with("semester").fire(new OSCEReceiver<List<OsceProxy>>() 
			{
						@Override
						public void onSuccess(List<OsceProxy> response) 
						{
								Log.info("All OSce For Semester : " + response.size());
								Iterator osceProxyIterator=response.iterator();
								while(osceProxyIterator.hasNext())
								{
									osceProxyforTemplate=(OsceProxy) osceProxyIterator.next();
									popupStud.getOsceList().addItem(osceProxyforTemplate.getName());
								}
								//popupSP.getOsceList().addItem(response.get(0).getName());
						}
			});
			
			Log.info("Selected Osce: " + osceProxy.getName());
			//Feature : 154
//			final String templateName="UpdatedTemplateStud"+osceProxy.getId()+".txt";
			
			//spSummoningsServiceAsync.getStudTemplateContent("UpdatedTemplateStud.txt",new AsyncCallback<String>() 
			spSummoningsServiceAsync.getStudTemplateContent(osceProxy.getId().toString(),TemplateTypes.STUDENT,new AsyncCallback<String[]>()
					//Feature : 154
			{
					@Override
					public void onSuccess(String[] html) 
					{
						Log.info("Go to Success..");
						if(!"".equals(html[0]))
						{
							popupStud.setMessageContent(html[1]);	
						}
						else
						{
							MessageConfirmationDialogBox dialog=new MessageConfirmationDialogBox(constants.warning());
							dialog.showConfirmationDialog("Template Not Found");
							
						}
						
						
					}
						
					@Override
					public void onFailure(Throwable arg0) 
					{
						Log.info("Go to Failure.." + arg0.getMessage());											
					}
			});

				popupStud.center();
				popupStud.getLoadTemplateButton().addClickHandler(new ClickHandler() 
				{
					@Override
					public void onClick(ClickEvent event) 
					{
						Log.info("Student Click on Load Template Osce Selected: " + popupStud.getOsceList().getValue(popupStud.getOsceList().getSelectedIndex()));
						
						requests.osceRequestNonRoo().findOsceIdByOsceName(popupStud.getOsceList().getValue(popupStud.getOsceList().getSelectedIndex())).fire(new OSCEReceiver<Long>() 
						{

							@Override
							public void onSuccess(Long response) 
							{
									Log.info("Osce Id For name: " + response);
									//Feature : 154
//									final String importTemplateName="UpdatedTemplateStud"+response+".txt";

									spSummoningsServiceAsync.getStudTemplateContent(response.toString(),TemplateTypes.STUDENT ,new AsyncCallback<String[]>()
											//Feature : 154
									{
													@Override
													public void onSuccess(String[] html) 
													{
														if(!"".equals(html[0]))
														{
															popupStud.setMessageContent(html[1]);	
														}
														else
														{
															MessageConfirmationDialogBox dialog=new MessageConfirmationDialogBox(constants.warning());
															dialog.showConfirmationDialog("Template Not Found");
															
														}														
													}
																
													@Override
													public void onFailure(Throwable arg0) 
													{
																		
													}
											});
									
									/*spSummoningsServiceAsync.deleteTemplate(templateName, new AsyncCallback<Boolean>()
									{
											@Override
											public void onSuccess(Boolean arg0) 
											{
															//spSummoningsServiceAsync.getTemplateContent("DefaultTemplateStud.txt",new AsyncCallback<String>() 
												spSummoningsServiceAsync.getStudTemplateContent(importTemplateName,new AsyncCallback<String>()
												{
														@Override
														public void onSuccess(String html) 
														{
															popupStud.setMessageContent(html);
														}
																	
														@Override
														public void onFailure(Throwable arg0) 
														{
																			
														}
												});
											}
																	
											@Override
											public void onFailure(Throwable arg0) 
											{
														
											}
										});*/
										
									}
								});
					}
				});
				//popupStud.setPopupPosition(196,83);
			
				popupStud.getSaveTemplateButton().addClickHandler(new ClickHandler() 
				{
					@Override
					public void onClick(ClickEvent event)
					{
						Log.info("Click Stud Save Button");
						//Feature : 154
						//spSummoningsServiceAsync.saveTemplate("UpdatedTemplateStud.txt", popupStud.getMessageContent(),new AsyncCallback<Boolean>()
						spSummoningsServiceAsync.deleteTemplate(osceProxy.getId().toString(),TemplateTypes.STUDENT , new AsyncCallback<Boolean>()
								//Feature : 154
								{
										@Override
										public void onSuccess(Boolean arg0) 
										{
											Log.info("Delete Success..");					
											//Feature : 154
											spSummoningsServiceAsync.saveTemplate(osceProxy.getId().toString(),TemplateTypes.STUDENT, popupStud.getMessageContent(),new AsyncCallback<Boolean>()
													//Feature : 154
											{

													@Override
													public void onFailure(Throwable caught) 
													{
														Log.info("Go to Failure.");
													}

													@Override
													public void onSuccess(Boolean result) 
													{
														Log.info("Go to Success.Save Template");														
														MessageConfirmationDialogBox dialog=new MessageConfirmationDialogBox(constants.success());
														dialog.showConfirmationDialog("Template Saved Successfully");
													}
													
												});			
										}																
										@Override
										public void onFailure(Throwable arg0) 
										{
													
										}
									});
						
													
					}
				});
				
				popupStud.getPrintTemplateButton().addClickHandler(new ClickHandler() 
				{
					@Override
					public void onClick(ClickEvent event)
					{
						Log.info("Click Stud Save Button");
						
						//spSummoningsServiceAsync.saveTemplate("UpdatedTemplateStud.txt", popupStud.getMessageContent(),new AsyncCallback<Boolean>()
						//Feature : 154
						spSummoningsServiceAsync.saveTemplate(osceProxy.getId().toString(),TemplateTypes.STUDENT, popupStud.getMessageContent(),new AsyncCallback<Boolean>()
								{

									@Override
									public void onFailure(Throwable caught) 
									{
										Log.info("Go to Failure.");
									}

									@Override
									public void onSuccess(Boolean result) 
									{
										Log.info("Go to Success.Save Template");
										//spSummoningsServiceAsync.generateMailPDFUsingTemplate("UpdatedTemplateSP.txt",templateSPVariables, new AsyncCallback<String>() {
										//spSummoningsServiceAsync.generateMailPDFUsingTemplate("UpdatedTemplateSP.txt",templateSPVariables,spId,1L,new AsyncCallback<String>() {
										Log.info("Semester Proxy: " + IndividualSchedulesActivity.semesterProxyForDetail.getId());	
										
										//spSummoningsServiceAsync.generateStudentPDFUsingTemplate("UpdatedTemplateStud.txt",studId,IndividualSchedulesActivity.semesterProxyForDetail.getId(),new AsyncCallback<String>()
										//Feature : 154
										spSummoningsServiceAsync.generateStudentPDFUsingTemplate(osceProxy.getId().toString(),TemplateTypes.STUDENT,studId,IndividualSchedulesActivity.semesterProxyForDetail.getId(),new AsyncCallback<String>()
										{

											@Override
											public void onFailure(Throwable caught) 
											{				
													Log.info("Go to Failure.");
											}

											@Override
											public void onSuccess(String response) 
											{
													Log.info("Call Stud Success... Generate PDF");													
													popupStud.hide();								
													Window.open(response, "_blank", "enabled");
											}
										});	
										
									}
									
								});										
					}
				});
				
				popupStud.getRestoreTemplateButton().addClickHandler(new ClickHandler() 
				{
					
					@Override
					public void onClick(ClickEvent event) 
					{
						Log.info("Click on Restore Button");
						//spSummoningsServiceAsync.deleteTemplate("UpdatedTemplateStud.txt", new AsyncCallback<Boolean>() 
						//Feature : 154
						spSummoningsServiceAsync.deleteTemplate(osceProxy.getId().toString(),TemplateTypes.STUDENT, new AsyncCallback<Boolean>()
						{
								@Override
								public void onSuccess(Boolean arg0) 
								{
									//spSummoningsServiceAsync.getTemplateContent("DefaultTemplateStud.txt",new AsyncCallback<String>()
									//Feature : 154
									spSummoningsServiceAsync.getStudTemplateContent("00", TemplateTypes.STUDENT,new AsyncCallback<String[]>()
									{
												@Override
												public void onSuccess(String[] html) 
												{
													if(!"".equals(html[0]))
													{
														popupStud.setMessageContent(html[1]);	
													}
													else
													{
														MessageConfirmationDialogBox dialog=new MessageConfirmationDialogBox(constants.warning());
														dialog.showConfirmationDialog("Template Not Found");
														
													}
													
												}
													
												@Override
												public void onFailure(Throwable arg0) 
												{
													
												}
									});
								}
									
								@Override
								public void onFailure(Throwable arg0) 
								{
										
								}
						});
				}
			});
				
		}

		@Override
		public void printCopyforSP(ClickEvent event) 
		{  
			Log.info("Call printCopyforSP()");
			lstSPStandPatName=new ArrayList<String>();
			lstSPStandPatPreName=new ArrayList<String>();
			lstSPOsce=new ArrayList<String>();						
			lstAssignment=new ArrayList<AssignmentProxy>();			
			lstSPPatientInRole=new ArrayList<List<Long>>();
			
			spId=new ArrayList<Long>();
				if(lstChkSp.size()>0 && lstStandPatProxy.size()>0) // Check that SP is Checked Or Not
				{
					Log.info("Total SP : " + lstChkSp.size());										
					int i=0;
					for(Iterator<CheckBox> iterator=lstChkSp.iterator();iterator.hasNext();i++)
					{					
						if(iterator.next().isChecked()==true)
						{
							Log.info("~~Selected Checkbox Id: " + i);
							Log.info("~~Print Plan for SP: " + lstStandPatProxy.get(i).getName());							
							
							spId.add(lstStandPatProxy.get(i).getId());		
							
						/*	lstSPStandPatName.add(util.getEmptyIfNull(lstStandPatProxy.get(i).getName()));
							lstSPStandPatPreName.add(util.getEmptyIfNull(lstStandPatProxy.get(i).getPreName()));
							
							requests.osceRequest().findOsce(osceProxy.getId()).with("semester").fire(new OSCEReceiver<Object>() {
							
								@Override
								public void onSuccess(Object response) 
								{
									Log.info("Success....");
									lstSPOsce.add(util.getEmptyIfNull(osceProxy.getStudyYear())+" " + util.getEmptyIfNull(((OsceProxy)response).getSemester().getSemester().name()));									
								}
							});*/
														
							
							requests.patientInRoleRequestNonRoo().findPatientsInRoleForAssignmentBySPIdandSemesterId(lstStandPatProxy.get(i).getId(),IndividualSchedulesActivity.semesterProxyForDetail.getId()).fire(new OSCEReceiver<List<PatientInRoleProxy>>() 
							{

								@Override
								public void onSuccess(List<PatientInRoleProxy> response) 
								{
									Log.info("Success..");
									Log.info("findPatientsInRoleForAssignmentBySPIdandSemesterId");
									//Log.info("Response Size: " + response.size() + "for Id: " + lstStandPatProxy.get(i).getId());
									if(response.size()>0)
									{										
										lstSPTempPatientInRole=new ArrayList<Long>();
										for(int roleIdIndex=0;roleIdIndex<response.size();roleIdIndex++)
										{									
											Log.info("PatientInRoleId: " + response.get(roleIdIndex).getId());
											lstSPTempPatientInRole.add(response.get(roleIdIndex).getId());
										}
										/*Iterator<PatientInRoleProxy> iteratorPatientInRole=response.iterator();
										while(iteratorPatientInRole.hasNext())
										{
											PatientInRoleProxy pir=iteratorPatientInRole.next();
											Log.info("PatientInRoleId: " + pir.getId());
											lstSPTempPatientInRole.add(pir.getId().toString());
										}*/
										Log.info("PatientInRole List Size: " + lstSPTempPatientInRole.size());
										lstSPPatientInRole.add(lstSPTempPatientInRole);
										
									}									
								}
							});
						}
					}
					Log.info("Total Number of Plans to Print " + spId.size());
					
					if(spId.size()>0)
					{
						initSPPopup();		
					}
					else
					{
						MessageConfirmationDialogBox dialogsp=new MessageConfirmationDialogBox(constants.warning());
						dialogsp.showConfirmationDialog("Please Select atleast one Standardized Patient");
					}
					
															
				}
				else
				{
					MessageConfirmationDialogBox dialog=new MessageConfirmationDialogBox("Warning");
					dialog.showConfirmationDialog("Please Select Standardized Patient..");
				}
		}

		private void initSPPopup() 
		{			
			Log.info("Call initSPPopup");
						
			Log.info("Standardized Patient Name List Size: " + lstSPStandPatName.size());
			Log.info("Standardized Patient Pre Name Size: " + lstSPStandPatPreName.size());
			Log.info("Standardized Patient Osce Size: " + lstSPOsce.size());
			//Log.info("Standardized Patient Role Size: " + lstSPRole.size());
			
	//		final String templateName="UpdatedTemplateSP"+osceProxy.getId()+".txt";
						
			final PrintTemplatePopupViewImpl popupSP=new PrintTemplatePopupViewImpl();							
			Log.info("getTemplate Content.");
			
			requests.osceRequestNonRoo( ).findAllOsceBySemster(IndividualSchedulesActivity.semesterProxyForDetail.getId()).with("semester").fire(new OSCEReceiver<List<OsceProxy>>() 
			{
						@Override
						public void onSuccess(List<OsceProxy> response) 
						{
								Log.info("All OSce For Semester : " + response.size());
								Iterator osceProxyIterator=response.iterator();
								while(osceProxyIterator.hasNext())
								{
									osceProxyforTemplate=(OsceProxy) osceProxyIterator.next();
									popupSP.getOsceList().addItem(osceProxyforTemplate.getName());
								}
								//popupSP.getOsceList().addItem(response.get(0).getName());
						}
			});
									
			//Log.info("Selected Value: " + popupSP.getOsceList().getValue(index));
			//spSummoningsServiceAsync.getTemplateContent("UpdatedTemplateSP.txt",new AsyncCallback<String>()
			//Feature : 154
			spSummoningsServiceAsync.getTemplateContent( osceProxy.getId().toString(),TemplateTypes.STANDARDIZED_PATIENT,new AsyncCallback<String[]>()
			{
				@Override
				public void onSuccess(String[] html) 
				{
					Log.info("Go to Success..");
					if(!"".equals(html[0]))
					{
						popupSP.setMessageContent(html[1]);	
					}
					else
					{
						MessageConfirmationDialogBox dialog=new MessageConfirmationDialogBox(constants.warning());
						dialog.showConfirmationDialog("Template Not Found");
						
					}
					
				}
				
				@Override
				public void onFailure(Throwable arg0) 
				{
					Log.info("Go to Failure.." + arg0.getMessage());											
				}
			});

			popupSP.center();
			
			popupSP.getLoadTemplateButton().addClickHandler(new ClickHandler() 
			{
				@Override
				public void onClick(ClickEvent event) 
				{
					Log.info("Click on Load Template Osce Selected: " + popupSP.getOsceList().getValue(popupSP.getOsceList().getSelectedIndex()));
					
					requests.osceRequestNonRoo().findOsceIdByOsceName(popupSP.getOsceList().getValue(popupSP.getOsceList().getSelectedIndex())).fire(new OSCEReceiver<Long>() 
					{

						@Override
						public void onSuccess(Long response) 
						{
							Log.info("Osce Id For name: " + response);
//							final String importTemplateName="UpdatedTemplateSP"+response+".txt";
							//Feature : 154
							spSummoningsServiceAsync.getTemplateContent(response.toString(),TemplateTypes.STANDARDIZED_PATIENT,new AsyncCallback<String[]>()
									{
												@Override
												public void onSuccess(String[] html) 
												{
													if(!"".equals(html[0]))
													{
														popupSP.setMessageContent(html[1]);	
													}
													else
													{
														MessageConfirmationDialogBox dialog=new MessageConfirmationDialogBox(constants.warning());
														dialog.showConfirmationDialog("Template Not Found");
														
													}													
												}
															
												@Override
												public void onFailure(Throwable arg0) 
												{
															
												}
									});
						}
					});
					
				}
			});
			

			
			popupSP.getSaveTemplateButton().addClickHandler(new ClickHandler() 
			{
				@Override
				public void onClick(ClickEvent event)
				{
					Log.info("Click Save Button");
					
					//Feature : 154
					spSummoningsServiceAsync.deleteTemplate(osceProxy.getId().toString(),TemplateTypes.STANDARDIZED_PATIENT, new AsyncCallback<Boolean>()
							{
									@Override
									public void onSuccess(Boolean arg0) 
									{
										Log.info("Delete SP Template");
										//Feature : 154
										spSummoningsServiceAsync.saveTemplate(osceProxy.getId().toString(),TemplateTypes.STANDARDIZED_PATIENT, popupSP.getMessageContent(),new AsyncCallback<Boolean>() 
												{

													@Override
													public void onFailure(Throwable caught) 
													{
														Log.info("Go to Failure.");
													}

													@Override
													public void onSuccess(Boolean result) 
													{
														Log.info("Go to Success.Save Template");														
														MessageConfirmationDialogBox dialog=new MessageConfirmationDialogBox(constants.success());
														dialog.showConfirmationDialog("Template Saved Successfully");
																	
													}
													
												});
									}
														
									@Override
									public void onFailure(Throwable arg0) 
									{
											
									}
							});
					
				}
			});
			
			popupSP.getPrintTemplateButton().addClickHandler(new ClickHandler() 
			{
				@Override
				public void onClick(ClickEvent event)
				{
					Log.info("Click Save Button");
					
					//Feature : 154
					spSummoningsServiceAsync.saveTemplate(osceProxy.getId().toString(),TemplateTypes.STANDARDIZED_PATIENT, popupSP.getMessageContent(),new AsyncCallback<Boolean>() 
					{

						@Override
						public void onFailure(Throwable caught) 
						{
							Log.info("Go to Failure.");
						}

						@Override
						public void onSuccess(Boolean result) 
						{
							Log.info("Go to Success.Save Template");
							//spSummoningsServiceAsync.generateMailPDFUsingTemplate("UpdatedTemplateSP.txt",templateSPVariables, new AsyncCallback<String>() {
							//spSummoningsServiceAsync.generateMailPDFUsingTemplate("UpdatedTemplateSP.txt",templateSPVariables,spId,1L,new AsyncCallback<String>() {
							Log.info("Semester Proxy: " + IndividualSchedulesActivity.semesterProxyForDetail.getId());
							//Feature : 154
							spSummoningsServiceAsync.generateSPPDFUsingTemplate(osceProxy.getId().toString(),TemplateTypes.STANDARDIZED_PATIENT,templateSPVariables,spId,IndividualSchedulesActivity.semesterProxyForDetail.getId(),new AsyncCallback<String>() 
							{
								@Override
								public void onFailure(Throwable caught) 
								{				
									Log.info("Go to Failure.");
								}

								@Override
								public void onSuccess(String response) 
								{
									Log.info("Call Success... Generate PDF");	
									popupSP.hide();
									Window.open(response, "_blank", "enabled");										
								}
							});							
						}
						
					});
				}
			});
			
			
			popupSP.getRestoreTemplateButton().addClickHandler(new ClickHandler() 
			{
				
				@Override
				public void onClick(ClickEvent event) 
				{
					Log.info("Click on Restore Button");
					//Feature : 154
					spSummoningsServiceAsync.deleteTemplate(osceProxy.getId().toString(),TemplateTypes.STANDARDIZED_PATIENT, new AsyncCallback<Boolean>() 
					{
								
								@Override
								public void onSuccess(Boolean arg0) 
								{
									//Feature : 154
									spSummoningsServiceAsync.getTemplateContent("00",TemplateTypes.STANDARDIZED_PATIENT,new AsyncCallback<String[]>() 
									{
												@Override
												public void onSuccess(String[] html) 
												{
													if(!"".equals(html[0]))
													{
														popupSP.setMessageContent(html[1]);	
													}
													else
													{
														MessageConfirmationDialogBox dialog=new MessageConfirmationDialogBox(constants.warning());
														dialog.showConfirmationDialog("Template Not Found");
														
													}
												}
												
												@Override
												public void onFailure(Throwable arg0) 
												{
													
												}
									});
									
								}
								
								@Override
								public void onFailure(Throwable arg0) 
								{
									
								}
							});
				}
			});
			
			
		}

		@Override
		public void printCopyforExaminor(ClickEvent event)
		{
			Log.info("Call printCopyforExaminor()");	
			examinorId=new ArrayList<Long>();
			if(lstChkExaminor.size()>0 && lstExaminerProxy.size()>0) // Check that SP is Checked Or Not
			{
				Log.info("Total Examiner : " + lstChkExaminor.size());										
				int i=0;
				for(Iterator<CheckBox> iterator=lstChkExaminor.iterator();iterator.hasNext();i++)
				{					
					if(iterator.next().isChecked()==true)
					{
						Log.info("~~Selected Checkbox Id: " + i);
						Log.info("~~Print Plan for Examiner: " + lstExaminerProxy.get(i).getName());
						examinorId.add(lstExaminerProxy.get(i).getId());		
					}
				}
				if(examinorId.size()>0)
				{
					initExaminerPopup();
				}
				else
				{
					MessageConfirmationDialogBox dialogExaminer=new MessageConfirmationDialogBox(constants.warning());
					dialogExaminer.showConfirmationDialog("Please Select atleast one Examiner");
				}
			}
			
			else
			{
				MessageConfirmationDialogBox dialog=new MessageConfirmationDialogBox("Warning");
				dialog.showConfirmationDialog("Please Select Examiner..");
			}
			
		}

		private void initExaminerPopup() 
		{
			Log.info("Call initExaminerPopup");
			
//			final String templateName="UpdatedTemplateExaminor"+osceProxy.getId()+".txt";
			
			final PrintTemplatePopupViewImpl popupExaminer=new PrintTemplatePopupViewImpl();							
			Log.info("get Stud Template Content.");
			
			requests.osceRequestNonRoo( ).findAllOsceBySemster(IndividualSchedulesActivity.semesterProxyForDetail.getId()).with("semester").fire(new OSCEReceiver<List<OsceProxy>>() 
			{
						@Override
						public void onSuccess(List<OsceProxy> response) 
						{
								Log.info("All OSce For Semester : " + response.size());
								Iterator osceProxyIterator=response.iterator();
								while(osceProxyIterator.hasNext())
								{
									osceProxyforTemplate=(OsceProxy) osceProxyIterator.next();
									popupExaminer.getOsceList().addItem(osceProxyforTemplate.getName());
								}
								//popupSP.getOsceList().addItem(response.get(0).getName());
						}
			});
			
			
			//Feature : 154
					spSummoningsServiceAsync.getExaminerTemplateContent(osceProxy.getId().toString(),TemplateTypes.EXAMINER,new AsyncCallback<String[]>() 
					{
						@Override
						public void onSuccess(String[] html) 
						{
							Log.info("Go to Success..");
							if(!"".equals(html[0]))
							{
								popupExaminer.setMessageContent(html[1]);	
							}
							else
							{
								MessageConfirmationDialogBox dialog=new MessageConfirmationDialogBox(constants.warning());
								dialog.showConfirmationDialog("Template Not Found");
								
							}
						}
						
						@Override
						public void onFailure(Throwable arg0) 
						{
							Log.info("Go to Failure.." + arg0.getMessage());											
						}
					});

			popupExaminer.center();
			
			popupExaminer.getLoadTemplateButton().addClickHandler(new ClickHandler() 
			{
				@Override
				public void onClick(ClickEvent event) 
				{
					Log.info("Examiner Click on Load Template Osce Selected: " + popupExaminer.getOsceList().getValue(popupExaminer.getOsceList().getSelectedIndex()));
					requests.osceRequestNonRoo().findOsceIdByOsceName(popupExaminer.getOsceList().getValue(popupExaminer.getOsceList().getSelectedIndex())).fire(new OSCEReceiver<Long>() 
							{

								@Override
								public void onSuccess(Long response) 
								{
									Log.info("Osce Id For name: " + response);
//									final String importTemplateName="UpdatedTemplateExaminor"+response+".txt";
									//Feature : 154
									spSummoningsServiceAsync.getExaminerTemplateContent(response.toString(),TemplateTypes.EXAMINER,new AsyncCallback<String[]>()
											{
														@Override
														public void onSuccess(String[] html) 
														{
															if(!"".equals(html[0]))
															{
																popupExaminer.setMessageContent(html[1]);	
															}
															else
															{
																MessageConfirmationDialogBox dialog=new MessageConfirmationDialogBox(constants.warning());
																dialog.showConfirmationDialog("Template Not Found");
																
															}
														}
																	
														@Override
														public void onFailure(Throwable arg0) 
														{
																	
														}
											});
									
									
								}
							});
				}
			});
			
			//popupExaminer.setPopupPosition(196,83);
			
			popupExaminer.getSaveTemplateButton().addClickHandler(new ClickHandler() 
			{
				@Override
				public void onClick(ClickEvent event)
				{
					Log.info("Click Examinor Save Button");
					//Feature : 154
					spSummoningsServiceAsync.deleteTemplate(osceProxy.getId().toString(),TemplateTypes.EXAMINER, new AsyncCallback<Boolean>()
							{
								@Override
									public void onSuccess(Boolean arg0) 
									{
									//Feature : 154
									Log.info("Call Delete Template");
									spSummoningsServiceAsync.saveTemplate(osceProxy.getId().toString(),TemplateTypes.EXAMINER, popupExaminer.getMessageContent(),new AsyncCallback<Boolean>() 
											{

												@Override
												public void onFailure(Throwable caught) 
												{
													Log.info("Go to Failure.");
												}

												@Override
												public void onSuccess(Boolean result) 
												{
													Log.info("Go to Success.Save Template");
													//spSummoningsServiceAsync.generateMailPDFUsingTemplate("UpdatedTemplateSP.txt",templateSPVariables, new AsyncCallback<String>() {
													//spSummoningsServiceAsync.generateMailPDFUsingTemplate("UpdatedTemplateSP.txt",templateSPVariables,spId,1L,new AsyncCallback<String>() {													
													MessageConfirmationDialogBox dialog=new MessageConfirmationDialogBox(constants.success());
													dialog.showConfirmationDialog("Template Saved Successfully");
												}
												
											});				
									}
														
									@Override
									public void onFailure(Throwable arg0) 
									{
											
									}
							});
							
					
											
				}
			});
			
			popupExaminer.getPrintTemplateButton().addClickHandler(new ClickHandler() 
			{
				@Override
				public void onClick(ClickEvent event)
				{
					Log.info("Click Examinor Save Button");
					
					//Feature : 154
					spSummoningsServiceAsync.saveTemplate(osceProxy.getId().toString(),TemplateTypes.EXAMINER, popupExaminer.getMessageContent(),new AsyncCallback<Boolean>() 
							{

								@Override
								public void onFailure(Throwable caught) 
								{
									Log.info("Go to Failure.");
								}

								@Override
								public void onSuccess(Boolean result) 
								{
									Log.info("Go to Success.Save Template");
									//spSummoningsServiceAsync.generateMailPDFUsingTemplate("UpdatedTemplateSP.txt",templateSPVariables, new AsyncCallback<String>() {
									//spSummoningsServiceAsync.generateMailPDFUsingTemplate("UpdatedTemplateSP.txt",templateSPVariables,spId,1L,new AsyncCallback<String>() {
									Log.info("Semester Proxy: " + IndividualSchedulesActivity.semesterProxyForDetail.getId());	
									
									//Feature : 154
									spSummoningsServiceAsync.generateExaminerPDFUsingTemplate(osceProxy.getId().toString(),TemplateTypes.EXAMINER,examinorId,IndividualSchedulesActivity.semesterProxyForDetail.getId(),new AsyncCallback<String>() 
											{

												@Override
												public void onFailure(Throwable caught) 
												{				
														Log.info("Go to Failure.");
												}

												@Override
												public void onSuccess(String response) 
												{
														Log.info("Call Stud Success... Generate PDF");	
														popupExaminer.hide();
														Window.open(response, "_blank", "enabled");
												}
											});	
																		
								}
								
							});										
				}
			});
			
			popupExaminer.getRestoreTemplateButton().addClickHandler(new ClickHandler() 
			{
				
				@Override
				public void onClick(ClickEvent event) 
				{			//Feature : 154
					Log.info("Click on Restore Button");
					spSummoningsServiceAsync.deleteTemplate(osceProxy.getId().toString(),TemplateTypes.EXAMINER, new AsyncCallback<Boolean>() 
					{
								@Override
								public void onSuccess(Boolean arg0) 
								{			//Feature : 154
									spSummoningsServiceAsync.getExaminerTemplateContent("00",TemplateTypes.EXAMINER ,new AsyncCallback<String[]>() 
									{
												@Override
												public void onSuccess(String[] html) 
												{
													if(!"".equals(html[0]))
													{
														popupExaminer.setMessageContent(html[1]);	
													}
													else
													{
														MessageConfirmationDialogBox dialog=new MessageConfirmationDialogBox(constants.warning());
														dialog.showConfirmationDialog("Template Not Found");
														
													}
												}
												
												@Override
												public void onFailure(Throwable arg0) 
												{
													
												}
									});
									
								}
								
								@Override
								public void onFailure(Throwable arg0) 
								{
									
								}
					});
				}
			});
			
		}
		
		
}