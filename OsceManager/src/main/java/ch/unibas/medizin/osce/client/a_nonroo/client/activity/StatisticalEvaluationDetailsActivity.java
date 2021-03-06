package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.MapEnvelopProxy;
import ch.unibas.medizin.osce.client.a_nonroo.client.OscePostWiseQuestionProxy;
import ch.unibas.medizin.osce.client.a_nonroo.client.StatisticalEvaluationQuestionProxy;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StatisticalEvaluationDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.GraphTemplatePopupViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.StatisticalEvaluation.StatisticalEvaluationDetailPostView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.StatisticalEvaluation.StatisticalEvaluationDetailPostViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.StatisticalEvaluation.StatisticalEvaluationDetailSequenceView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.StatisticalEvaluation.StatisticalEvaluationDetailSequenceViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.StatisticalEvaluation.StatisticalEvaluationDetailsItemView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.StatisticalEvaluation.StatisticalEvaluationDetailsItemViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.StatisticalEvaluation.StatisticalEvaluationDetailsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.StatisticalEvaluation.StatisticalEvaluationDetailsViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenHandler;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickEvent;
import ch.unibas.medizin.osce.client.managed.request.AnswerProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistItemProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistOptionProxy;
import ch.unibas.medizin.osce.client.managed.request.CourseProxy;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceSequenceProxy;
import ch.unibas.medizin.osce.client.style.widgets.FocusableValueListBox;
import ch.unibas.medizin.osce.client.style.widgets.NumberSpinner;
import ch.unibas.medizin.osce.shared.AnalysisType;
import ch.unibas.medizin.osce.shared.PostType;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.google.web.bindery.requestfactory.shared.Violation;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;



@SuppressWarnings("deprecation")
public class StatisticalEvaluationDetailsActivity extends AbstractActivity implements
StatisticalEvaluationDetailsView.Presenter, 
StatisticalEvaluationDetailsView.Delegate,StatisticalEvaluationDetailSequenceView.Delegate,StatisticalEvaluationDetailPostView.Delegate,StatisticalEvaluationDetailsItemView.Delegate
{
	private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
		private StatisticalEvaluationDetailsView view;
		
		private final OsceConstants constants;
		
		StatisticalEvaluationDetailsActivity statisticalEvaluationDetailsActivity;
			
		private int numOfPosts=0;
		private StatisticalEvaluationDetailsPlace place;
		private StatisticalEvaluationDetailsActivity activity;
		private OsceProxy osceProxy;
		
		private  List<String> itemAnalysisColumnName=new ArrayList<String>();
		private List<String> postAnalysisColumnName=new ArrayList<String>();
		private AnalysisType analysisType=null;
		private Set<Long> missingItemId=new HashSet<Long>();
		
		private List<MapEnvelopProxy> itemAnalysisData=null;
		private List<MapEnvelopProxy> postAnalysisData=null;
		private List<String> examinerId=new ArrayList<String>();
		private List<Integer> addPoint=new ArrayList<Integer>();
		
		//by spec
		private StatisticalEvaluationDetailSequenceViewImpl postStatisticalEvaluationDetailSequenceViewImpl;
		private List<String> postId = new ArrayList<String>();
		private List<Long> impressionQueId = new ArrayList<Long>();
		List<String> postValues = new ArrayList<String>();
		//by spec
		
		public StatisticalEvaluationDetailsActivity(StatisticalEvaluationDetailsPlace place, OsMaRequestFactory requests, PlaceController placeController) 
		{
			Log.info("Call StatisticalEvaluationDetailsPlace(3arg)");
			this.place = place;
	    	this.requests = requests;
	    	this.placeController = placeController;
	    	this.activity=this;
	    	this.statisticalEvaluationDetailsActivity=this;
	    	constants = GWT.create(OsceConstants.class);
	    	
	    	Log.info("place Proxy id " + place.getProxyId());
	    	
	    	itemAnalysisColumnName.add(constants.missings());
	    	itemAnalysisColumnName.add(constants.average());
	    	itemAnalysisColumnName.add(constants.stdDeviation());
	    	itemAnalysisColumnName.add(constants.points());
	    	itemAnalysisColumnName.add(constants.frequency());
	    	itemAnalysisColumnName.add(constants.cronbachsAlpha());
	    	
	    	postAnalysisColumnName.add(constants.studentCount());
	    	postAnalysisColumnName.add(constants.pass());
	    	postAnalysisColumnName.add(constants.fail());
	    	postAnalysisColumnName.add(constants.passingGrade());
	    	postAnalysisColumnName.add(constants.average());
	    	postAnalysisColumnName.add(constants.stdDeviation());
	    	postAnalysisColumnName.add(constants.minimum());
	    	postAnalysisColumnName.add(constants.maximum());
	    }
		
		public void onStop()
		{
			
		}

		@Override
		public void start(AcceptsOneWidget panel, EventBus eventBus) 
		{
			Log.info("StatisticalEvaluationDetailsActivity.start()");
			//create main View
			final StatisticalEvaluationDetailsView statisticalEvaluationDetailsView = new StatisticalEvaluationDetailsViewImpl(this);			
			statisticalEvaluationDetailsView.setPresenter(this);			
			this.widget = panel;
			this.view = statisticalEvaluationDetailsView;
			widget.setWidget(statisticalEvaluationDetailsView.asWidget());			
			view.setDelegate(this);			
			statisticalEvaluationDetailsView.setDelegate(this);	
			
			MenuClickEvent.register(requests.getEventBus(), (StatisticalEvaluationDetailsViewImpl)view);
			
			initLoadingScreen();		
			
			init();
			
		}
	
		private void initLoadingScreen()
		{
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
		}
		private void init() 
		{
			Log.info("Init Call.");
			//retrieveForSum();
		}
		
		public void retrieveForSum()
		{
			Log.info("retrieveForSum");
			retrieveSequences();
		}
		
		public void retrieveSequences()
		{
			Log.info("retrieveSequences");
			if(analysisType.equals(AnalysisType.item_analysis) || analysisType.equals(AnalysisType.post_analysys))
			{
				requests.find(place.getProxyId()).with("osce_days","osce_days.osceSequences").fire(new OSCEReceiver<Object>() {

					@Override
					public void onSuccess(Object response) {
						
						Log.info("retrieveSequences success");
						if(response !=null && response instanceof OsceProxy)
						{
							osceProxy=(OsceProxy)response;
							//retrieve calculated data from database if exist
							if(analysisType.equals(AnalysisType.item_analysis))
							{
								
								showApplicationLoading(true);
								requests.answerRequest().retrieveCalulatedData(osceProxy.getId(),0).fire(new OSCEReceiver<List<MapEnvelopProxy>>() {

									@Override
									public void onSuccess(List<MapEnvelopProxy> response) {
										
										itemAnalysisData=response;
										refreshAllSequences();
										
										showApplicationLoading(false);
									}
								});
							}
							else if(analysisType.equals(AnalysisType.post_analysys))
							{
								showApplicationLoading(true);
								requests.answerRequest().retrieveCalulatedData(osceProxy.getId(),1).fire(new OSCEReceiver<List<MapEnvelopProxy>>() {

									@Override
									public void onSuccess(List<MapEnvelopProxy> response) {
										
										if (response == null)
										{
											showApplicationLoading(false);
											return;
										}
										
										if(response.size() !=0)											
										postAnalysisData=response;
										
										refreshAllSequences();
										
										showApplicationLoading(false);
									}
								});
							}
							
							
						}
						
					}
				});
			}
			/* For Sum
			 * requests.find(place.getProxyId()).with("osce_days","osce_days.osceSequences","osce_days.osceSequences.oscePosts").fire(new OSCEReceiver<Object>() {

				@Override
				public void onSuccess(Object response) {
					
					Log.info("retrieveSequences success");
					if(response !=null && response instanceof OsceProxy)
					{
						osceProxy=(OsceProxy)response;
						refreshAllSequences();
						
					}
					
				}
			});*/
		}
		//retrive data and create view for sum value in combox box
		public void refreshAllSequences()
		{
			Log.info("refreshAllSequences");
			
			view.getSequenceVP().clear();
			
			if(analysisType.equals(AnalysisType.item_analysis))
			{
				view.getExportBtn().setVisible(false);
				view.getNewexportBtn().setVisible(false);
			}
			else if(analysisType.equals(AnalysisType.post_analysys))
			{
				view.getExportBtn().setVisible(true);
				view.getNewexportBtn().setVisible(true);
				view.getExportBtn().setText(constants.export());
				view.getNewexportBtn().setText(constants.newExport());
			}
			
			Iterator<OsceDayProxy> osceDayProxyIterator=osceProxy.getOsce_days().iterator();
			while(osceDayProxyIterator.hasNext())
			{
				OsceDayProxy osceDayProxy=osceDayProxyIterator.next();
				Iterator<OsceSequenceProxy> osceSequenceProxyIterator=osceDayProxy.getOsceSequences().iterator();
				
				while(osceSequenceProxyIterator.hasNext())
				{
					OsceSequenceProxy osceSequenceProxy=osceSequenceProxyIterator.next();
					createSequence(osceSequenceProxy,osceDayProxy);
				}
			}
			
			
		}
		
		public void createSequence(OsceSequenceProxy osceSequenceProxy,OsceDayProxy osceDayProxy)
		{
			Log.info("createSequence");
			
			StatisticalEvaluationDetailSequenceViewImpl sequenceView=new StatisticalEvaluationDetailSequenceViewImpl();
			
			sequenceView.setDelegate(this);
			sequenceView.setOsceSequenceProxy(osceSequenceProxy);
			sequenceView.setOsceDayProxy(osceDayProxy);
			sequenceView.getSequenceLbl().setText(constants.sequence()+" "+osceSequenceProxy.getLabel());
			//sequenceView.getSumPerSequenceLbl().setText(constants.sumPerSequence());
			sequenceView.setSequencePanel(true);
			view.getSequenceVP().add(sequenceView.asWidget());
			
			//create post views and post data
			sequenceView.getPostViewHP().clear();
			sequenceView.getPostDataHP().clear();
			
			if(analysisType.equals(AnalysisType.post_analysys))
			sequenceView.getSequenceHeader().getTBodies().getItem(0).getElementsByTagName("td").getItem(3).setPropertyString("width", "28%");
			
			
			if(analysisType.equals(AnalysisType.item_analysis))
			{
				createColumnHeader(sequenceView);
				for(int i=0;i<itemAnalysisColumnName.size();i++)
				{
					createPostDataLabel(sequenceView, "-");
				}
				
			}
			else if(analysisType.equals(AnalysisType.post_analysys))
			{
				sequenceView.getSequenceLbl().getElement().getParentElement().getParentElement().getParentElement().getParentElement().getParentElement().getStyle().setWidth(337, Unit.PX);
				sequenceView.getSequenceLbl().getElement().getStyle().setWidth(337, Unit.PX);
				//sequenceView.getGraphBtn().setVisible(true);
				
				createColumnHeader(sequenceView);
				for(int i=0;i<postAnalysisColumnName.size();i++)
				{
					createPostDataLabel(sequenceView, "-");
				}
			}
			
			if(analysisType.equals(AnalysisType.item_analysis)  && itemAnalysisData != null)
			{
				String seqKey="s"+sequenceView.getOsceSequenceProxy().getId();
				
				Log.info("key :" +seqKey);
				
				List<String> seqValues=getValue(itemAnalysisData, seqKey);
				
				((Label)sequenceView.getPostDataHP().getWidget(2)).setText(seqValues.get(0));
			}
			
			if(analysisType.equals(AnalysisType.post_analysys)  && postAnalysisData != null)
			{
				String seqKey="s"+sequenceView.getOsceSequenceProxy().getId();
				
				Log.info("key :" +seqKey);
				
				List<String> seqValues=getValue(postAnalysisData, seqKey);
				if(seqValues != null)
				((Label)sequenceView.getPostDataHP().getWidget(2)).setText(seqValues.get(0));
			}
			//For sum
			/*
			 Iterator<OscePostProxy> postIterator=osceSequenceProxy.getOscePosts().iterator();
			numOfPosts=osceSequenceProxy.getOscePosts().size();
			  while(postIterator.hasNext())
			{
				OscePostProxy postProxy=postIterator.next();
				createPostDataLabel(sequenceView, 0);
				createOscePost(sequenceView,postProxy);
				
			}*/
		
			
			
			
		}
		public void createColumnHeader(StatisticalEvaluationDetailSequenceViewImpl sequenceView)
		{
			Log.info("createItemAnalysisColumnHeader");
			if(analysisType.equals(AnalysisType.item_analysis))
			{
				sequenceView.getPostViewHP().clear();
				for(String headerName:itemAnalysisColumnName)
				{
					StatisticalEvaluationDetailPostView header=new StatisticalEvaluationDetailPostViewImpl();
					header.setDelegate(this);
					header.getPostNameLbl().setText(headerName);
					if(sequenceView.getPostViewHP().getWidgetCount()==0 || sequenceView.getPostViewHP().getWidgetCount()==3 || sequenceView.getPostViewHP().getWidgetCount()==5)
					{
						//header.asWidget().getElement().getStyle().setProperty("borderLeftWidth", "3px");
					}
					sequenceView.getPostViewHP().add(header.asWidget());
					sequenceView.getPostViewHP().getElement().getStyle().setLeft(444, Unit.PX);
				}
			}
			else if(analysisType.equals(AnalysisType.post_analysys))
			{
				sequenceView.getPostViewHP().clear();
				for(String headerName:postAnalysisColumnName)
				{
					StatisticalEvaluationDetailPostView header=new StatisticalEvaluationDetailPostViewImpl();
					header.setDelegate(this);
					header.getPostNameLbl().setText(headerName);
					header.getPostNameHP().setWidth("63px");
					header.getPostNameLbl().getElement().getStyle().setProperty("maxWidth", "61px");
					sequenceView.getPostViewHP().add(header.asWidget());
					sequenceView.getPostViewHP().getElement().getStyle().setLeft(371, Unit.PX);
				}
			}
			
		
		}
		
		
		public void createOscePost(StatisticalEvaluationDetailSequenceViewImpl sequenceView,OscePostProxy postProxy)
		{
			Log.info("createOscePost");
			
			//create post
			StatisticalEvaluationDetailPostView postView=new StatisticalEvaluationDetailPostViewImpl();
			postView.setDelegate(this);
			postView.setOscePostProxy(postProxy);
			postView.getPostNameLbl().setText(constants.post()+postProxy.getSequenceNumber());
			
			//decrease width of last column by 93px
			Log.info("Sequence width :"+sequenceView.getSumPerSequenceLbl().getElement().getOffsetWidth());
			sequenceView.getPostViewHP().add(postView.asWidget());
		
		}
		
		public void createPostDataLabel(Object object,String data)
		{
			Log.info("createPostDataLabel");
			
			if(object instanceof StatisticalEvaluationDetailSequenceViewImpl)
			{
				StatisticalEvaluationDetailSequenceViewImpl sequenceView=(StatisticalEvaluationDetailSequenceViewImpl)object;
				Label label=sequenceView.createPostDataLabel();
				if(analysisType.equals(AnalysisType.post_analysys))
				{
					label.setWidth("52px");
				}
				label.setText(data.toString());
				
				if(analysisType.equals(AnalysisType.item_analysis))
				{
					if(sequenceView.getPostDataHP().getWidgetCount() ==0)
					{
						//label.getElement().getStyle().setProperty("borderLeftWidth", "3px");
						//label.getElement().getStyle().setProperty("borderColor", "#9EB7BE");
					}
				}
				sequenceView.getPostDataHP().add(label);
				
			}
			else if(object instanceof StatisticalEvaluationDetailsItemViewImpl)
			{
				StatisticalEvaluationDetailsItemViewImpl itemView=(StatisticalEvaluationDetailsItemViewImpl)object;
				Label label=itemView.createPostDataLabel();
				if(analysisType.equals(AnalysisType.post_analysys))
				{
					label.setWidth("52px");
				}
				label.setText(data.toString());
				itemView.getPostDataHP().add(label);
				
			}
			
			
		}
		
		@Override
		public void goTo(Place place) 
		{
			placeController.goTo(place);
			
		}
		
		
		public void showApplicationLoading(Boolean show) {
			requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(show));

		}

		@Override
		public void sequenceDisclosurePanelOpen(final StatisticalEvaluationDetailSequenceViewImpl statisticalEvaluationDetailSequenceViewImpl) {
			Log.info("sequenceDisclosurePanelOpen");
			OsceSequenceProxy sequenceProxy=statisticalEvaluationDetailSequenceViewImpl.getOsceSequenceProxy();
			
			if(analysisType.equals(AnalysisType.item_analysis) && statisticalEvaluationDetailSequenceViewImpl.isSequencePanel())
			{
				Log.info("create all post panel");
				requests.osceSequenceRequest().findOsceSequence(sequenceProxy.getId()).with("oscePosts","oscePosts.oscePostBlueprint").fire(new OSCEReceiver<OsceSequenceProxy>() {

					@Override
					public void onSuccess(OsceSequenceProxy response) {
						Log.info("sequenceDisclosurePanelOpen success courses size" + response.getOscePosts().size());
						List<OscePostProxy> OscePostProxies=response.getOscePosts();
						statisticalEvaluationDetailSequenceViewImpl.getDisclosureVP().clear();
						for(OscePostProxy oscePostProxy:OscePostProxies)
						{
							if (!oscePostProxy.getOscePostBlueprint().getPostType().equals(PostType.BREAK))
								createPostPanel(oscePostProxy, statisticalEvaluationDetailSequenceViewImpl, null);
						}
						
					}
				});
			}
			else if(analysisType.equals(AnalysisType.item_analysis) && statisticalEvaluationDetailSequenceViewImpl.isPostPanel())
			{
				Log.info("create all item panel");
				/*requests.answerRequest().retrieveDistinctQuestion(statisticalEvaluationDetailSequenceViewImpl.getOscePostProxy().getId()).with("checkListOptions").fire(new OSCEReceiver<List<ChecklistQuestionProxy>>() {

					@Override
					public void onSuccess(List<ChecklistQuestionProxy> response) {
						Log.info("create all item panel success :" +response.size());
						statisticalEvaluationDetailSequenceViewImpl.getDisclosureVP().clear();
						for(ChecklistQuestionProxy answerProxy:response)
						{
							createQuestionPanel(answerProxy, statisticalEvaluationDetailSequenceViewImpl);
						}
					}
				});*/
				
				requests.answerRequest().retrieveDistinctQuestionItem(statisticalEvaluationDetailSequenceViewImpl.getOscePostProxy().getId()).with("checkListOptions").fire(new OSCEReceiver<List<ChecklistItemProxy>>() {

					@Override
					public void onSuccess(List<ChecklistItemProxy> response) {
						Log.info("create all item panel success :" +response.size());
						statisticalEvaluationDetailSequenceViewImpl.getDisclosureVP().clear();
						for(ChecklistItemProxy answerProxy:response)
						{
							createItemQuestionPanel(answerProxy, statisticalEvaluationDetailSequenceViewImpl);
						}
					}
				});
			}
			else if(analysisType.equals(AnalysisType.post_analysys) && statisticalEvaluationDetailSequenceViewImpl.isSequencePanel())
			{
				Log.info("create all post panel");
				//requests.osceSequenceRequest().findOsceSequence(sequenceProxy.getId()).with("oscePosts","oscePosts.oscePostBlueprint","oscePosts.standardizedRole","oscePosts.standardizedRole","oscePosts.standardizedRole","oscePosts.standardizedRole.checkList","oscePosts.standardizedRole.checkList.checkListTopics","oscePosts.standardizedRole.checkList.checkListTopics.checkListQuestions").fire(new OSCEReceiver<OsceSequenceProxy>() {
				requests.osceSequenceRequest().findOsceSequence(sequenceProxy.getId()).with("oscePosts","oscePosts.oscePostBlueprint","oscePosts.standardizedRole","oscePosts.standardizedRole","oscePosts.standardizedRole","oscePosts.standardizedRole.checkList").fire(new OSCEReceiver<OsceSequenceProxy>() {

					@Override
					public void onSuccess(OsceSequenceProxy response) {
						Log.info("sequenceDisclosurePanelOpen success courses size" + response.getOscePosts().size());
						final List<OscePostProxy> OscePostProxies=response.getOscePosts();
						statisticalEvaluationDetailSequenceViewImpl.getDisclosureVP().clear();
						
						requests.checklistItemRequest().findChecklistQuestionByOscePost(response.getId()).fire(new OSCEReceiver<List<OscePostWiseQuestionProxy>>() {

							@Override
							public void onSuccess(List<OscePostWiseQuestionProxy> questionProxyList) {
							
								for(final OscePostProxy oscePostProxy : OscePostProxies)
								{
									if (!oscePostProxy.getOscePostBlueprint().getPostType().equals(PostType.BREAK)) {
										for (OscePostWiseQuestionProxy questionProxy : questionProxyList) {
											if (questionProxy.getOscePostId().equals(oscePostProxy.getId())) {
												createPostPanel(oscePostProxy, statisticalEvaluationDetailSequenceViewImpl, questionProxy.getQuestionList());
												break;
											}
										}
									}
								}
							}
						});
						
						
					}
				});
			}
			else if(analysisType.equals(AnalysisType.post_analysys) && statisticalEvaluationDetailSequenceViewImpl.isPostPanel())
			{
				//by spec
				postStatisticalEvaluationDetailSequenceViewImpl = statisticalEvaluationDetailSequenceViewImpl;
				//by spec
				
				Log.info("create all item panel");
				requests.answerRequest().retrieveDistinctExaminerByItem(statisticalEvaluationDetailSequenceViewImpl.getOscePostProxy().getId()).with("checkListOptions").fire(new OSCEReceiver<List<DoctorProxy>>() {

					@Override
					public void onSuccess(List<DoctorProxy> response) {
						statisticalEvaluationDetailSequenceViewImpl.getDisclosureVP().clear();
						for(DoctorProxy doctorProxy:response)
						{	
							createExaminerPanel(doctorProxy, statisticalEvaluationDetailSequenceViewImpl);
						}
					}
					@Override
					public void onFailure(ServerFailure error) {
					}
					@Override
					public void onViolation(Set<Violation> errors) 
					{
					}
				});
			}
			/* For Sum
			requests.osceSequenceRequest().findOsceSequence(sequenceProxy.getId()).with("courses").fire(new OSCEReceiver<OsceSequenceProxy>() {

				@Override
				public void onSuccess(OsceSequenceProxy response) {
					Log.info("sequenceDisclosurePanelOpen success courses size" + response.getCourses().size());
					List<CourseProxy> courses=response.getCourses();
					statisticalEvaluationDetailSequenceViewImpl.getDisclosureVP().clear();
					for(CourseProxy courseProxy:courses)
					{
						createParcour(courseProxy,statisticalEvaluationDetailSequenceViewImpl);
					}
					
				}
			});*/
		}
		
		public void createExaminerPanel(DoctorProxy doctorProxy,StatisticalEvaluationDetailSequenceViewImpl statisticalEvaluationDetailSequenceViewImpl)
		{
			Log.info("createExaminerPanel");
			StatisticalEvaluationDetailsItemView examinerView=new StatisticalEvaluationDetailsItemViewImpl();
			examinerView.setDelegate(this);
			//examinerView.getSequenceLbl().getElement().getParentElement().getStyle().setWidth(22, Unit.PCT);
			examinerView.getSequenceLbl().getElement().getParentElement().getStyle().setWidth(318, Unit.PX);
			examinerView.getSequenceLbl().getElement().getStyle().setFontSize(12, Unit.PX);
			examinerView.getSequenceLbl().setWidth("318px");
			examinerView.getOnOffButton().setVisible(false);
			examinerView.setDoctorProxy(doctorProxy);
			examinerView.setOscePostProxy(statisticalEvaluationDetailSequenceViewImpl.getOscePostProxy());
			examinerView.getSequenceLbl().setText(doctorProxy.getPreName() + " " + doctorProxy.getName());
			examinerView.getSumPerSequenceLbl().setText("");
			examinerView.getFourthColumnHP().clear();
			examinerView.getFourthColumnHP().add(examinerView.createAddPointButton());
			if(analysisType.equals(AnalysisType.post_analysys))
			{
				//examinerView.getSumPerSequenceLbl().getElement().getParentElement().getStyle().setWidth(28, Unit.PCT);
				//examinerView.getFourthColumnHP().getElement().getParentElement().getStyle().setWidth(28, Unit.PCT);
				examinerView.getSumPerSequenceLbl().getElement().getStyle().setWidth(28, Unit.PCT);
				examinerView.getFourthColumnHP().getElement().getStyle().setWidth(28, Unit.PCT);
				//examinerView.getSequenceHeader().getTBodies().getItem(0).getElementsByTagName("td").getItem(3).setPropertyString("width", "28%");
			}
			statisticalEvaluationDetailSequenceViewImpl.getDisclosureVP().add(examinerView.asWidget());
			examinerView.getPostDataHP().clear();
			for(int i=0;i<statisticalEvaluationDetailSequenceViewImpl.getPostDataHP().getWidgetCount();i++)
			{
				createPostDataLabel(examinerView, "-");
			}
			
			if(analysisType.equals(AnalysisType.post_analysys) && postAnalysisData != null)
			{
				String examinerKey="e"+statisticalEvaluationDetailSequenceViewImpl.getOscePostProxy().getId()+examinerView.getDoctorProxy().getId();
				
				Log.info("key :" +examinerKey);
				
				List<String> examinerValues=getValue(postAnalysisData, examinerKey);
				
				for(int l=0;l<examinerValues.size()-1;l++)
				{
					String examinerValue=examinerValues.get(l);
					
					
					((Label)examinerView.getPostDataHP().getWidget(l)).setText(examinerValue);
				}
				
				if (examinerValues.size() > 0 && examinerView.getFourthColumnHP().getWidget(0) != null)
					((NumberSpinner)examinerView.getFourthColumnHP().getWidget(0)).setValue(Integer.parseInt(examinerValues.get(examinerValues.size() -1)));
			}
		}
		
		
		/*//create question/item panel
		public void createQuestionPanel(ChecklistQuestionProxy checklistQuestionProxy,StatisticalEvaluationDetailSequenceViewImpl statisticalEvaluationDetailSequenceViewImpl)
		{
			Log.info("createQuestionPanel");
			
			StatisticalEvaluationDetailsItemView questionView=new StatisticalEvaluationDetailsItemViewImpl();
			questionView.setDelegate(this);
			questionView.getSequenceLbl().getElement().getParentElement().getStyle().setWidth(388, Unit.PX);
			questionView.getSequenceLbl().getElement().getStyle().setFontSize(12, Unit.PX);
			questionView.getSequenceLbl().setWidth("388px");
			
			questionView.setChecklistQuestionProxy(checklistQuestionProxy);
			String question=checklistQuestionProxy.getQuestion();
			if(question.length() > 60)
			{
				question=question.substring(0, 60);
				question=question+"...";
			}
			questionView.getSequenceLbl().setText(question);
			questionView.getSequenceLbl().setTitle(checklistQuestionProxy.getQuestion());
			questionView.getSumPerSequenceLbl().setText("");
			//questionView.getSequenceHeader().addClassName("parcourHeader");
			
			
				if(missingItemId.contains(checklistQuestionProxy.getId()))
				{
					questionView.getOnOffButton().setDown(true);
				}
			
			
			statisticalEvaluationDetailSequenceViewImpl.getDisclosureVP().add(questionView.asWidget());
			
			questionView.getPostDataHP().clear();
			for(int i=0;i<statisticalEvaluationDetailSequenceViewImpl.getPostDataHP().getWidgetCount();i++)
			{
				//points/option values
				if(i==3)
				{
					List<ChecklistOptionProxy> options=questionView.getChecklistQuestionProxy().getCheckListOptions();
					String optionValues="";
					for(ChecklistOptionProxy option:options)
					{
						if(optionValues.equals(""))
							optionValues=option.getValue();
						else
							optionValues=optionValues +" / " + option.getValue();
					}
					createPostDataLabel(questionView, optionValues);
				}
				else
					createPostDataLabel(questionView, "-");
			}
			
			if(analysisType.equals(AnalysisType.item_analysis) && itemAnalysisData != null)
			{
				String itemKey="q"+statisticalEvaluationDetailSequenceViewImpl.getOscePostProxy().getId()+questionView.getChecklistQuestionProxy().getId();
				
				Log.info("key :" +itemKey);
				
				List<String> itemValues=getValue(itemAnalysisData, itemKey);
				
				for(int l=0;l<itemValues.size();l++)
				{
					String itemValue=itemValues.get(l);
					
					if(l==0)//disable item / not
					{
						if(itemValue.equals("true"))
						{
							questionView.getOnOffButton().setDown(true);
							missingItemId.add(questionView.getChecklistQuestionProxy().getId());
						}
						else
						{
							questionView.getOnOffButton().setDown(false);
							missingItemId.remove(questionView.getChecklistQuestionProxy().getId());
						}
						continue;
					}
					
					if(l==1)
					{
						
						String temp[]=itemValue.split("/");
						if(new Double(temp[0]) != 0)
						{
							((Label)questionView.getPostDataHP().getWidget(l-1)).getElement().getStyle().setColor("red");
						}
					}
					
					((Label)questionView.getPostDataHP().getWidget(l-1)).setText(itemValue);
				}
			}
		}*/
		
		//create question/item panel
		public void createItemQuestionPanel(ChecklistItemProxy answerProxy,StatisticalEvaluationDetailSequenceViewImpl statisticalEvaluationDetailSequenceViewImpl)
		{
			Log.info("createQuestionPanel");
			
			StatisticalEvaluationDetailsItemView questionView=new StatisticalEvaluationDetailsItemViewImpl();
			questionView.setDelegate(this);
			questionView.getSequenceLbl().getElement().getParentElement().getStyle().setWidth(388, Unit.PX);
			questionView.getSequenceLbl().getElement().getStyle().setFontSize(12, Unit.PX);
			questionView.getSequenceLbl().setWidth("388px");
			
			//questionView.setChecklistQuestionProxy(answerProxy);
			questionView.setChecklistItemProxy(answerProxy);
			String question=answerProxy.getName();
			if(question.length() > 60)
			{
				question=question.substring(0, 60);
				question=question+"...";
			}
			questionView.getSequenceLbl().setText(question);
			questionView.getSequenceLbl().setTitle(answerProxy.getName());
			questionView.getSumPerSequenceLbl().setText("");
			//questionView.getSequenceHeader().addClassName("parcourHeader");
			
			
				if(missingItemId.contains(answerProxy.getId()))
				{
					questionView.getOnOffButton().setDown(true);
				}
			
			
			statisticalEvaluationDetailSequenceViewImpl.getDisclosureVP().add(questionView.asWidget());
			
			questionView.getPostDataHP().clear();
			for(int i=0;i<statisticalEvaluationDetailSequenceViewImpl.getPostDataHP().getWidgetCount();i++)
			{
				//points/option values
				if(i==3)
				{
					List<ChecklistOptionProxy> options=questionView.getChecklistItemProxy().getCheckListOptions();
					String optionValues="";
					for(ChecklistOptionProxy option:options)
					{
						if(optionValues.equals(""))
							optionValues=option.getValue();
						else
							optionValues=optionValues +" / " + option.getValue();
					}
					createPostDataLabel(questionView, optionValues);
				}
				else
					createPostDataLabel(questionView, "-");
			}
			
			if(analysisType.equals(AnalysisType.item_analysis) && itemAnalysisData != null)
			{
				String itemKey="q"+statisticalEvaluationDetailSequenceViewImpl.getOscePostProxy().getId()+questionView.getChecklistItemProxy().getId();
				
				Log.info("key :" +itemKey);
				
				List<String> itemValues=getValue(itemAnalysisData, itemKey);
				
				for(int l=0;l<itemValues.size();l++)
				{
					String itemValue=itemValues.get(l);
					
					if(l==0)//disable item / not
					{
						if(itemValue.equals("true"))
						{
							questionView.getOnOffButton().setDown(true);
							missingItemId.add(questionView.getChecklistItemProxy().getId());
						}
						else
						{
							questionView.getOnOffButton().setDown(false);
							missingItemId.remove(questionView.getChecklistItemProxy().getId());
						}
						continue;
					}
					
					if(l==1)
					{
						
						String temp[]=itemValue.split("/");
						if(new Double(temp[0]) != 0)
						{
							((Label)questionView.getPostDataHP().getWidget(l-1)).getElement().getStyle().setColor("red");
						}
					}
					
					((Label)questionView.getPostDataHP().getWidget(l-1)).setText(itemValue);
				}
			}
		}
		
		
		//create post panel
		public void createPostPanel(final OscePostProxy oscePostProxy,StatisticalEvaluationDetailSequenceViewImpl statisticalEvaluationDetailSequenceViewImpl, List<StatisticalEvaluationQuestionProxy> questionProxyList)
		{
			Log.info("createPostPanel");
			Map<Long, StatisticalEvaluationQuestionProxy> questionItemMap = new HashMap<Long, StatisticalEvaluationQuestionProxy>();
			final StatisticalEvaluationDetailSequenceView postView=new StatisticalEvaluationDetailSequenceViewImpl();
			
			//if(analysisType.equals(AnalysisType.post_analysys))
		//		postView=new StatisticalEvaluationDetailSequenceViewImpl(true,true);
		//	else
				
			
			Log.info("createPostPanel td"+postView.getSequenceLbl().getElement().getParentElement());
			
			postView.getMainPanel().setWidth("99%");
			
			postView.setPostPanel(true);
			postView.setOscePostProxy(oscePostProxy);
			postView.getSequenceLbl().setText(constants.post()+oscePostProxy.getSequenceNumber());
			postView.getSumPerSequenceLbl().setText("");
			postView.getSequenceHeader().addClassName("parcourHeader");
			postView.getSequenceDisclosurePanel().addStyleName("parcourContent");
			postView.setOsceDayProxy(statisticalEvaluationDetailSequenceViewImpl.getOsceDayProxy());
			postView.setDelegate(this);
			
			if(analysisType.equals(AnalysisType.item_analysis))
			{
				postView.getSequenceLbl().getElement().getParentElement().getParentElement().getParentElement().getParentElement().getParentElement().getStyle().setWidth(395, Unit.PX);
				postView.getSequenceLbl().setWidth("395px");
				
			}
			else if(analysisType.equals(AnalysisType.post_analysys))
			{
				postView.getGraphBtn().setVisible(true);
				
				postView.getSequenceLbl().getElement().getParentElement().getParentElement().getParentElement().getParentElement().getParentElement().getStyle().setWidth(324, Unit.PX);
				postView.getSequenceLbl().setWidth("264px");
				postView.getFourthColumnHP().getElement().getParentElement().getStyle().setWidth(28, Unit.PCT);
				//postView.getSequenceHeader().getTBodies().getItem(0).getElementsByTagName("td").getItem(3).setPropertyString("width", "28%");
				
				//postView.getSequenceHeader().getTBodies().getItem(0).getElementsByTagName("td").getItem(3).getStyle().setWidth(28, Unit.PCT);
				
				((HorizontalPanel)postView.getFourthColumnHP()).clear();
				Button clearBtn=new Button();
				clearBtn.setText(constants.clear());
				
				
				
				final FocusableValueListBox<StatisticalEvaluationQuestionProxy> itemList=new FocusableValueListBox<StatisticalEvaluationQuestionProxy>(new Renderer<StatisticalEvaluationQuestionProxy>() {

					@Override
					public String render(StatisticalEvaluationQuestionProxy object) {
						if(object==null)
							return "";
									
						String question=object.getQuestionText();
						if(question.length() > 40) {
							question=question.substring(0, 40) + "...";
						}
						return question;
					}

					@Override
					public void render(StatisticalEvaluationQuestionProxy object, Appendable appendable) throws IOException {
					}
				});
				
				itemList.addStyleName("impressionItemListBox");
				StatisticalEvaluationQuestionProxy impressionItem=null;
				
				for(StatisticalEvaluationQuestionProxy itemProxy : questionProxyList)
				{
					questionItemMap.put(itemProxy.getQuestionId(), itemProxy);
					if(itemProxy.getIsRegressionItem()) {
						impressionItem = itemProxy;
					}
				}
				
				itemList.setAcceptableValues(questionProxyList);
				
				if (postAnalysisData == null)
				{
					if(impressionItem!=null)
					{
						itemList.setValue(impressionItem);
						addImpressionQuestion(oscePostProxy.getId(), impressionItem.getQuestionId());
					}					
				}
				
				itemList.addValueChangeHandler(new ValueChangeHandler<StatisticalEvaluationQuestionProxy>() {
					
					@Override
					public void onValueChange(ValueChangeEvent<StatisticalEvaluationQuestionProxy> event) {
						if (oscePostProxy != null) {
							addImpressionQuestion(oscePostProxy.getId(), event.getValue().getQuestionId());
						}
					}
				});
				
				clearBtn.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						itemList.setValue(null);
						
					}
				});
				
				itemList.setWidth("268px");
				((HorizontalPanel)postView.getFourthColumnHP()).add(clearBtn);
				((HorizontalPanel)postView.getFourthColumnHP()).add(itemList);
				
			}
			
			statisticalEvaluationDetailSequenceViewImpl.getDisclosureVP().add(postView.asWidget());
			
			postView.getPostDataHP().clear();
			for(int i=0;i<statisticalEvaluationDetailSequenceViewImpl.getPostViewHP().getWidgetCount();i++)
			{
				createPostDataLabel((StatisticalEvaluationDetailSequenceViewImpl)postView, "-");
			}
			
			
			if(analysisType.equals(AnalysisType.item_analysis)  && itemAnalysisData != null)
			{
				String postKey="p"+postView.getOscePostProxy().getId();
				
				Log.info("key :" +postKey);
				
				List<String> postValues=getValue(itemAnalysisData, postKey);
				
				for(int k=0;k<postValues.size();k++)
				{
					((Label)postView.getPostDataHP().getWidget(k)).setText(postValues.get(k));
				}
			}
			
			if(analysisType.equals(AnalysisType.post_analysys)  && postAnalysisData != null)
			{
				String postKey="p"+postView.getOscePostProxy().getId();
				
				Log.info("key :" +postKey);
				
				List<String> postValues=getValue(postAnalysisData, postKey);
				
				for(int k=0;k<postValues.size()-1;k++)
				{
					((Label)postView.getPostDataHP().getWidget(k)).setText(postValues.get(k));
				}
				
				if (postValues.size() > 0 && postView.getFourthColumnHP().getWidget(1) != null && postView.getFourthColumnHP().getWidget(1) instanceof FocusableValueListBox)
				{
					Long questionItemId = Long.parseLong(postValues.get(postValues.size() - 1));
					if (oscePostProxy != null && questionItemMap.containsKey(questionItemId)) {
						StatisticalEvaluationQuestionProxy questionProxy = questionItemMap.get(questionItemId);
						addImpressionQuestion(oscePostProxy.getId(), questionProxy.getQuestionId());
						((FocusableValueListBox)postView.getFourthColumnHP().getWidget(1)).setValue(questionProxy);
					}
				}
			}
		}
		
		@Override
		public void parcourDisclosurePanelOpen(final StatisticalEvaluationDetailSequenceViewImpl statisticalEvaluationDetailSequenceViewImpl) {
			Log.info("parcourDisclosurePanelOpen");
			
			requests.answerRequest().retrieveStudent(statisticalEvaluationDetailSequenceViewImpl.getOsceDayProxy().getId(),  statisticalEvaluationDetailSequenceViewImpl.getCourseProxy().getId()).with("student").fire(new OSCEReceiver<List<AnswerProxy>>() {

				@Override
				public void onSuccess(List<AnswerProxy> response) {
					Log.info("parcourDisclosurePanelOpen success :" + response.size());
					statisticalEvaluationDetailSequenceViewImpl.getDisclosureVP().clear();
					for(AnswerProxy answerProxy:response)
						createStudentView(answerProxy,statisticalEvaluationDetailSequenceViewImpl);
					
				}

				
			});
			
		}
		
		public void createStudentView(AnswerProxy answerProxy,StatisticalEvaluationDetailSequenceViewImpl statisticalEvaluationDetailSequenceViewImpl)
		{
			StatisticalEvaluationDetailSequenceView studentView=new StatisticalEvaluationDetailSequenceViewImpl();
			studentView.getMainPanel().setWidth("99%");
			studentView.getSequenceLbl().setWidth("490px");
			studentView.setAnswerProxy(answerProxy);
			if(answerProxy.getStudent().getName() !=null)
				studentView.getSequenceLbl().setText(answerProxy.getStudent().getName() + "( "+constants.students()+" )");
			else
				studentView.getSequenceLbl().setText("-");
			
			studentView.getSumPerSequenceLbl().setText(constants.sumPerStudent());
			studentView.getSequenceHeader().addClassName("studentHeader");
			studentView.getSequenceDisclosurePanel().addStyleName("studentContent");
			studentView.setOsceDayProxy(statisticalEvaluationDetailSequenceViewImpl.getOsceDayProxy());
			studentView.setDelegate(this);
			statisticalEvaluationDetailSequenceViewImpl.getDisclosureVP().add(studentView.asWidget());
			studentView.getPostDataHP().clear();
			
			for(int i=0;i<numOfPosts;i++)
			{
				createPostDataLabel((StatisticalEvaluationDetailSequenceViewImpl)studentView, "0");
			}
		}
		public void createParcour(CourseProxy courseProxy,StatisticalEvaluationDetailSequenceViewImpl statisticalEvaluationDetailSequenceViewImpl)
		{
			StatisticalEvaluationDetailSequenceView courseView=new StatisticalEvaluationDetailSequenceViewImpl();
			courseView.getMainPanel().setWidth("99%");
			courseView.getSequenceLbl().setWidth("503px");
			courseView.setCourseProxy(courseProxy);
			courseView.getSequenceLbl().setText(constants.circuit());
			courseView.getSumPerSequenceLbl().setText(constants.sumPerParcour());
			courseView.getSequenceHeader().addClassName("parcourHeader");
			courseView.getSequenceDisclosurePanel().addStyleName("parcourContent");
			courseView.setOsceDayProxy(statisticalEvaluationDetailSequenceViewImpl.getOsceDayProxy());
			courseView.setDelegate(this);
			
			statisticalEvaluationDetailSequenceViewImpl.getDisclosureVP().add(courseView.asWidget());
			
			courseView.getPostDataHP().clear();
			for(int i=0;i<statisticalEvaluationDetailSequenceViewImpl.getPostViewHP().getWidgetCount();i++)
			{
				createPostDataLabel((StatisticalEvaluationDetailSequenceViewImpl)courseView, "0");
			}
		}
		
		public void retrieveItemAnalysisData()
		{
			Log.info("retrieveItemAnalysisData");
			retrieveSequences();
		}
		
		@Override
		public void analysisListBoxValueChanged(AnalysisType a) {
			Log.info("analysisListBoxValueChanged");
			analysisType=a;
			retrieveItemAnalysisData();
		}
		
		public void calculate()
		{
			Log.info("calculate");
			
			if(analysisType.equals(AnalysisType.item_analysis))
			{
				showApplicationLoading(true);
				requests.answerRequest().calculate(osceProxy.getId(),0,missingItemId,null,null,null,null).with("").fire(new OSCEReceiver<List<MapEnvelopProxy>>() {
	
					@Override
					public void onSuccess(List<MapEnvelopProxy> response) {
						
						itemAnalysisData=response;
						
						Log.info("calculate success :" +response.size());
					/*	for(String key:response.getKeys())
						{
							Log.info("key :" + key);
							List<List<String>> values=response.getValues();
							for(List<String> valueList:values)
							{
								for(String value:valueList)
								Log.info("value :" + value);
							}
						}
						*/
						VerticalPanel vp=view.getSequenceVP();
						for(int i=0;i<vp.getWidgetCount();i++)
						{
							StatisticalEvaluationDetailSequenceViewImpl seqView=((StatisticalEvaluationDetailSequenceViewImpl)(vp.getWidget(i)));
							//MapEnvelopProxy mapProxy=response.get(i);
							//List<String> standardDevialtion=mapProxy.getValue();
							String seqKey="s"+seqView.getOsceSequenceProxy().getId();
							Log.info("key :" +seqKey);
							List<String> standardDevialtion=getValue(response, seqKey);
							((Label)seqView.getPostDataHP().getWidget(2)).setText(standardDevialtion.get(0));
							
							VerticalPanel postVP=seqView.getDisclosureVP();
							
							if(seqView.getSequenceDisclosurePanel().isOpen())
							{
								for(int j=0;j<postVP.getWidgetCount();j++)
								{
									StatisticalEvaluationDetailSequenceViewImpl postView=((StatisticalEvaluationDetailSequenceViewImpl)(postVP.getWidget(j)));
									String postKey="p"+postView.getOscePostProxy().getId();
									
									Log.info("key :" +postKey);
									
									List<String> postValues=getValue(response, postKey);
									
									for(int k=0;k<postValues.size();k++)
									{
	
											((Label)postView.getPostDataHP().getWidget(k)).setText(postValues.get(k));
									}
									
									VerticalPanel itemVP=postView.getDisclosureVP();
									
									if(postView.getSequenceDisclosurePanel().isOpen())
									{
										for(int k=0;k<itemVP.getWidgetCount();k++)
										{
											StatisticalEvaluationDetailsItemViewImpl itemView=(StatisticalEvaluationDetailsItemViewImpl)itemVP.getWidget(k);
											
											String itemKey="q"+postView.getOscePostProxy().getId()+itemView.getChecklistItemProxy().getId();
											
											Log.info("key :" +itemKey);
											
											List<String> itemValues=getValue(response, itemKey);
											
											for(int l=0;l<itemValues.size();l++)
											{
												String itemValue=itemValues.get(l);
												
												
												if(l==0)//disable item / not
												{
													if(itemValue.equals("true"))
													{
														itemView.getOnOffButton().setDown(true);
														missingItemId.add(itemView.getChecklistItemProxy().getId());
													}
													else
													{
														itemView.getOnOffButton().setDown(false);
														missingItemId.remove(itemView.getChecklistItemProxy().getId());
													}
													continue;
												}
												
												if(l==1)
												{
													
													String temp[]=itemValue.split("/");
													if(new Double(temp[0]) != 0)
													{
														((Label)itemView.getPostDataHP().getWidget(l-1)).getElement().getStyle().setColor("red");
													}
												}
												
												((Label)itemView.getPostDataHP().getWidget(l-1)).setText(itemValue);
											}
										}
									}
								}
							}
							
						}
					
						showApplicationLoading(false);
					}
					
					@Override
					public void onFailure(ServerFailure error) {
						Log.info("on failure called : " + error.getMessage());
						showApplicationLoading(false);
					}
				});
			}
			else if(analysisType.equals(AnalysisType.post_analysys))
			{
				showApplicationLoading(true);
					if (postStatisticalEvaluationDetailSequenceViewImpl != null)
					{
						for(int i=0;i<postStatisticalEvaluationDetailSequenceViewImpl.getDisclosureVP().getWidgetCount();i++)
						{
							if (postStatisticalEvaluationDetailSequenceViewImpl.getDisclosureVP().getWidget(i) != null && postStatisticalEvaluationDetailSequenceViewImpl.getDisclosureVP().getWidget(i) instanceof StatisticalEvaluationDetailsItemViewImpl)
							{
								StatisticalEvaluationDetailsItemViewImpl examinerView = (StatisticalEvaluationDetailsItemViewImpl) postStatisticalEvaluationDetailSequenceViewImpl.getDisclosureVP().getWidget(i);
								
								if (examinerView.getOscePostProxy() != null && examinerView.getDoctorProxy() != null && examinerView.getAddPoint() != null)
								{
									setAddPoint(examinerView.getOscePostProxy(), examinerView.getDoctorProxy(), examinerView.getAddPoint().getValue());
									//System.out.println("ADDPOINT : " + examinerView.getAddPoint().getValue());
								}
							}
						}
					}
				
					requests.answerRequest().calculate(osceProxy.getId(), 1, null, examinerId, addPoint, postId, impressionQueId).fire(new OSCEReceiver<List<MapEnvelopProxy>>() 
					{

						@Override
						public void onSuccess(final List<MapEnvelopProxy> response) {
							
							postAnalysisData=response;
							
							Log.info("calculate success :" +response.size());
						/*	for(String key:response.getKeys())
							{
								Log.info("key :" + key);
								List<List<String>> values=response.getValues();
								for(List<String> valueList:values)
								{
									for(String value:valueList)
									Log.info("value :" + value);
								}
							}*/
							
							VerticalPanel vp=view.getSequenceVP();
							for(int i=0;i<vp.getWidgetCount();i++)
							{
								StatisticalEvaluationDetailSequenceViewImpl seqView=((StatisticalEvaluationDetailSequenceViewImpl)(vp.getWidget(i)));
								//MapEnvelopProxy mapProxy=response.get(i);
								//List<String> standardDevialtion=mapProxy.getValue();
							//	String seqKey="s"+seqView.getOsceSequenceProxy().getId();
							//	Log.info("key :" +seqKey);
							//	List<String> standardDevialtion=getValue(response, seqKey);
							//	((Label)seqView.getPostDataHP().getWidget(2)).setText(standardDevialtion.get(0));
								
								VerticalPanel postVP=seqView.getDisclosureVP();
								
								if(seqView.getSequenceDisclosurePanel().isOpen())
								{
									for(int j=0;j<postVP.getWidgetCount();j++)
									{
										final StatisticalEvaluationDetailSequenceViewImpl postView=((StatisticalEvaluationDetailSequenceViewImpl)(postVP.getWidget(j)));
										String postKey="p"+postView.getOscePostProxy().getId();
										
										Log.info("key :" +postKey);
										
										List<String> postValues=getValue(response, postKey);
										
										for(int k=0;k<postValues.size()-1;k++)
										{
											((Label)postView.getPostDataHP().getWidget(k)).setText(postValues.get(k));
										}
										
										if (postValues.size() > 0 && postView.getFourthColumnHP().getWidget(1) != null && postView.getFourthColumnHP().getWidget(1) instanceof FocusableValueListBox)
										{
											/*requests.checklistItemRequest().findChecklistItem(Long.parseLong(postValues.get(postValues.size() - 1))).fire(new OSCEReceiver<ChecklistItemProxy>() {

												@Override
												public void onSuccess(ChecklistItemProxy response) {
													addImpressionQuestion(postView.getOscePostProxy().getId(), response.getId());
													
													((FocusableValueListBox)postView.getFourthColumnHP().getWidget(1)).setValue(response);
												}
											});*/
											
											Long questionItemId = Long.parseLong(postValues.get(postValues.size() - 1));
											FocusableValueListBox<StatisticalEvaluationQuestionProxy> questionfocusableValueListBox = (FocusableValueListBox)postView.getFourthColumnHP().getWidget(1);
											ListBox questionListBox = questionfocusableValueListBox.getListBox();
											
											for (int k=0; k<questionListBox.getItemCount(); k++) {
												if (questionListBox.getValue(k).equals(questionItemId)) {
													questionListBox.setSelectedIndex(k);
													break;
												}
											}
											
											/*requests.checklistQuestionRequest().findChecklistQuestion(Long.parseLong(postValues.get(postValues.size() - 1))).fire(new OSCEReceiver<ChecklistQuestionProxy>() {
												@Override
												public void onFailure(ServerFailure error) {
													
												}
												
												@Override
												public void onSuccess(ChecklistQuestionProxy question) {													
													if (postView.getOscePostProxy() != null && question != null)
													{
														addImpressionQuestion(postView.getOscePostProxy().getId(), question.getId());
														((FocusableValueListBox)postView.getFourthColumnHP().getWidget(1)).setValue(question);
													}
												}
											});*/
											
										}
										
										VerticalPanel itemVP=postView.getDisclosureVP();
										
										if(postView.getSequenceDisclosurePanel().isOpen())
										{
											for(int k=0;k<itemVP.getWidgetCount();k++)
											{
												StatisticalEvaluationDetailsItemViewImpl itemView=(StatisticalEvaluationDetailsItemViewImpl)itemVP.getWidget(k);
												
												String itemKey="e"+postView.getOscePostProxy().getId()+itemView.getDoctorProxy().getId();
												
												Log.info("key :" +itemKey);
												
												List<String> itemValues=getValue(response, itemKey);
												
												for(int l=0;l<itemValues.size()-1;l++)
												{
													
													((Label)itemView.getPostDataHP().getWidget(l)).setText(itemValues.get(l));
												}
												
												if (itemValues.size() > 0 && itemView.getPostDataHP().getWidget(0) != null)
													((NumberSpinner)itemView.getFourthColumnHP().getWidget(0)).setValue(Integer.parseInt(itemValues.get(itemValues.size() - 1)));
												
											}
										}
									}
								}
								
							}
						
							showApplicationLoading(false);
							
						}
					});
					
					
			}
		}
		
		public void exportStatisticData()
		{
			 String url=GWT.getHostPageBaseURL()+"exportStatisticData?osceId="+osceProxy.getId()+"&export=old";
			
			VerticalPanel vp=view.getSequenceVP();
			for(int i=0;i<vp.getWidgetCount();i++)
			{
				StatisticalEvaluationDetailSequenceViewImpl seqView=((StatisticalEvaluationDetailSequenceViewImpl)(vp.getWidget(i)));
				VerticalPanel postVP=seqView.getDisclosureVP();
				if(seqView.getSequenceDisclosurePanel().isOpen())
				{
					for(int j=0;j<postVP.getWidgetCount();j++)
					{
						StatisticalEvaluationDetailSequenceViewImpl postView=((StatisticalEvaluationDetailSequenceViewImpl)(postVP.getWidget(j)));
						Long postId=postView.getOscePostProxy().getId();
						/*ChecklistQuestionProxy questionProxy=(ChecklistQuestionProxy)((FocusableValueListBox)postView.getFourthColumnHP().getWidget(1)).getValue();*/
						StatisticalEvaluationQuestionProxy questionProxy=(StatisticalEvaluationQuestionProxy)((FocusableValueListBox)postView.getFourthColumnHP().getWidget(1)).getValue();
						
						Long questionId=0l;
						if(questionProxy!=null)
						  questionId=questionProxy.getQuestionId();
						
						url=url+"&p"+postId.toString()+"="+questionId.toString();
						
					}
				}
			}
			Log.info("exportStatisticData url :" + url);
			   Window.open(url, osceProxy.getName(), "enabled");
		}
		
		
		@Override
		public void newexportStatisticData() {
			
			String url=GWT.getHostPageBaseURL()+"exportStatisticData?osceId="+osceProxy.getId()+"&export=new";
			
			VerticalPanel vp=view.getSequenceVP();
			for(int i=0;i<vp.getWidgetCount();i++)
			{
				StatisticalEvaluationDetailSequenceViewImpl seqView=((StatisticalEvaluationDetailSequenceViewImpl)(vp.getWidget(i)));
				VerticalPanel postVP=seqView.getDisclosureVP();
				if(seqView.getSequenceDisclosurePanel().isOpen())
				{
					for(int j=0;j<postVP.getWidgetCount();j++)
					{
						StatisticalEvaluationDetailSequenceViewImpl postView=((StatisticalEvaluationDetailSequenceViewImpl)(postVP.getWidget(j)));
						Long postId=postView.getOscePostProxy().getId();
						/*ChecklistQuestionProxy questionProxy=(ChecklistQuestionProxy)((FocusableValueListBox)postView.getFourthColumnHP().getWidget(1)).getValue();*/
						StatisticalEvaluationQuestionProxy questionProxy=(StatisticalEvaluationQuestionProxy)((FocusableValueListBox)postView.getFourthColumnHP().getWidget(1)).getValue();
						
						Long questionId=0l;
						if(questionProxy!=null)
						  questionId=questionProxy.getQuestionId();
						
						url=url+"&p"+postId.toString()+"="+questionId.toString();
						
					}
				}
			}
			Log.info("exportStatisticData url :" + url);
			   Window.open(url, osceProxy.getName(), "enabled");
		}
		
		
		public static List<String>  getValue(List<MapEnvelopProxy> map,String key)
		{
			
			for(MapEnvelopProxy keyValuePair:map)
			{
				if(keyValuePair.getKey().equalsIgnoreCase(key))
				{
					return keyValuePair.getValue();
				}
			}
			
			return null;
		}
		
		public void onOffButtonClicked(Long id,Boolean missing)
		{
			if(missing)//red
				missingItemId.add(id);
			else//green
				missingItemId.remove(id);
			
			
			/*requests.itemAnalysisNonRoo().deActivateItem(osceProxy.getId(), id, missing).fire(new OSCEReceiver<Boolean>() {

				@Override
				public void onSuccess(Boolean response) {
					Log.info("deActivateItem"+response);
					if(!response)
					{
						//item is not calculated
					}
					
				}
			});*/
		}

		@Override
		public void setAddPoint(OscePostProxy oscePostProxy,DoctorProxy doctorProxy, Integer value) {
			if(value !=null && doctorProxy != null && oscePostProxy != null)
			{
				String key="p"+oscePostProxy.getId()+"e"+doctorProxy.getId();
				if(examinerId.contains(key))
				{
					int index = -1;
					for (int i=0; i<examinerId.size(); i++)
					{
						if (examinerId.get(i).equals(key))
						{
							index = i;
							break;
						}
					}
					if (index >= 0)
						addPoint.set(index, value);
				}
				else
				{
					examinerId.add(key);
					addPoint.add(value);
				}
			}
		}

		@Override
		public void graphBtnClicked(Long oscePostId) {
			showApplicationLoading(true);
			requests.answerRequest().createGraph(oscePostId).fire(new OSCEReceiver<String>() {

				@Override
				public void onSuccess(String response) {
					showApplicationLoading(false);
					final String name = GWT.getHostPageBaseURL() + response.substring(1);
					Log.info("RESPONSE : " + name);
					 
					final GraphTemplatePopupViewImpl graphTemplateView = new GraphTemplatePopupViewImpl();
					
					graphTemplateView.getGraphImage().setUrl(name);
										
					graphTemplateView.addCloseClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							graphTemplateView.hide();
						}
					});
					
					graphTemplateView.getGraphImage().setVisible(true);
					
					graphTemplateView.center();
					graphTemplateView.show();
				}
				
				@Override
				public void onFailure(ServerFailure error) {
					showApplicationLoading(false);
					
					MessageConfirmationDialogBox dialogBox = new MessageConfirmationDialogBox(constants.error());
					dialogBox.showConfirmationDialog(constants.graphError());
				}
				
			});
			
			
		}

		public void addImpressionQuestion(Long oscePostId, Long questionId)
		{
			String key = "p" + oscePostId;
			
			if(postId.contains(key))
			{
				int index = -1;
				
				for (int i=0; i<postId.size(); i++)
				{
					if(postId.get(i).equals(key))
					{
						index = i;
						break;		
					}
				}
				
				if	(index >= 0)
					impressionQueId.set(index, questionId);
			}
			else
			{	
				postId.add(key);
				impressionQueId.add(questionId);
			}
		}
		
}
