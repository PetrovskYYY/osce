package ch.unibas.medizin.osce.client.a_nonroo.client.activity;



import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.CircuitDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.CircuitPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.CircuitView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.CircuitViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.SelectChangeEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.SelectChangeHandler;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import ch.unibas.medizin.osce.shared.Operation;
import ch.unibas.medizin.osce.shared.OsceCreationType;
import ch.unibas.medizin.osce.shared.Semesters;
import ch.unibas.medizin.osce.shared.StudyYears;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * @author dk
 *
 */
public class CircuitActivity extends AbstractActivity implements CircuitView.Presenter, CircuitView.Delegate {
	
    private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private CircuitView view;
	private CircuitPlace place;
	private int tabIndex=0; 
	private CircuitDetailsActivityMapper circuitDetailsActivityMapper;
	private ActivityManager activityManager;
	private OsceProxy osceProxy;
	private static SemesterProxy semesterProxy;
	
	private List<OsceProxy> osceProxyList = new ArrayList<OsceProxy>();
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	public static HandlerManager handlerManager;// = new HandlerManager(this);
	private SelectChangeHandler removeHandler;

	
	// G: SPEC START =
			public CircuitActivity(OsMaRequestFactory requests,PlaceController placeController, CircuitPlace circuitPlace) 
			{			
				Log.info("~Call Constructor CircuitActivity <Custom>");		
				this.requests = requests;		
				this.placeController = placeController;
				this.place=circuitPlace;
				this.handlerManager = CircuitPlace.handler;
				this.semesterProxy=CircuitPlace.semesterProxy;
				Log.info("Semester Proxy : " + semesterProxy.getCalYear() + " :in CircuitActivity Constructor.");
				//System.out.println("Proxy: " + place.semesterProxy.getCalYear());
				
				circuitDetailsActivityMapper = new CircuitDetailsActivityMapper(requests,placeController);
				this.activityManager = new ActivityManager(circuitDetailsActivityMapper,requests.getEventBus());
								
				
				this.addSelectChangeHandler(new SelectChangeHandler() 
				{			
					@Override
					public void onSelectionChange(SelectChangeEvent event) 
					{			
						Log.info("Call Role Activity");					
						Log.info("onSelectionChange Get Semester: " + event.getSemesterProxy().getCalYear());		
						semesterProxy= event.getSemesterProxy();
						init();
					}
				});
			}
			public void addSelectChangeHandler(SelectChangeHandler handler) {
				handlerManager.addHandler(SelectChangeEvent.getType(), handler);
				removeHandler=handler;
			}
			// G: SPEC END =
	
	
	public CircuitActivity(OsMaRequestFactory requests, PlaceController placeController) {
    	this.requests = requests;
    	this.placeController = placeController;
    	circuitDetailsActivityMapper = new CircuitDetailsActivityMapper(requests,
				placeController);
		this.activityManager = new ActivityManager(circuitDetailsActivityMapper,
				requests.getEventBus());
    }

	public void onStop(){
		activityManager.setDisplay(null);
		handlerManager.removeHandler(SelectChangeEvent.getType(), removeHandler);
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("SystemStartActivity.start()");
		this.widget = panel;
		init();
				
	}
	
	private void init() {
		Log.info("Init");
		CircuitView systemStartView = new CircuitViewImpl();
		systemStartView.setPresenter(this);		
		this.view = systemStartView;
		widget.setWidget(systemStartView.asWidget());
		view.setDelegate(this);
		
		MenuClickEvent.register(requests.getEventBus(), (CircuitViewImpl)view);
		
		view.getCircuitTabPanel().addSelectionHandler(new SelectionHandler<Integer>() {
			
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				Log.info("New Tab Selected");	
				//view.getCircuitTabPanel().selectTab(view.getCircuitTabPanel().getTabBar().getSelectedTab());				
				activityManager.setDisplay((SimplePanel)view.getCircuitTabPanel().getWidget(event.getSelectedItem()));
				//activityManager.setDisplay(view.getCircuitDetailPanel());				
				goTo(new CircuitDetailsPlace(osceProxyList.get(event.getSelectedItem()).stableId(),Operation.DETAILS));
			}
		});
		
		requests.osceRequest().findAllOsceBySemesterIdAndCreationType(semesterProxy.getId(), OsceCreationType.Automatic).with("semester").fire(new OSCEReceiver<List<OsceProxy>>() {

			@Override
			public void onSuccess(List<OsceProxy> response) {
				tabIndex=0;
				if (response.size() == 0)
					view.getMainHTMLPanel().getElement().getStyle().setDisplay(Display.NONE);
				else
					view.getMainHTMLPanel().getElement().getStyle().clearDisplay();
				
				if(response != null && response.size() > 0){
					osceProxyList=response;
					Log.info("OSce Proxy Size : " + response.size());
					
					Iterator<OsceProxy> osceList = response.iterator();
					
					osceProxy = response.get(0);
					while(osceList.hasNext()){
					
					Log.info("OSce Proxy index : " + tabIndex);
					OsceProxy osceProxy = osceList.next();
					String osceLable = new EnumRenderer<StudyYears>().render(osceProxy.getStudyYear()) 
							+ "." + new EnumRenderer<Semesters>().render(osceProxy.getSemester().getSemester());
					osceLable=osceLable+((osceProxy.getIsRepeOsce()==true)?" ("+constants.repe()+")":"");
					view.getCircuitTabPanel().insert(new SimplePanel(), osceLable,tabIndex);
					tabIndex++;
					
					}
					
					view.getCircuitTabPanel().selectTab(0);
				//activityManager.setDisplay((SimplePanel)view.getCircuitTabPanel().getWidget(0));	
				/*activityManager.setDisplay((SimplePanel)view.getCircuitTabPanel().getWidget(0));
				Log.info("Before Goto");
				goTo(new CircuitDetailsPlace(osceProxy.stableId(),Operation.DETAILS));*/
				
				}
			}
		});
		
				
		}

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
	}
	
}
