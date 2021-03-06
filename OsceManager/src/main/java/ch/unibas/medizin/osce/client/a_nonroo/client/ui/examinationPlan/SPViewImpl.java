package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examinationPlan;

import ch.unibas.medizin.osce.client.managed.request.AssignmentProxy;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.event.dom.client.HasMouseDownHandlers;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class SPViewImpl extends Composite implements SPView, HasMouseDownHandlers{
	
	private static SPViewImplUiBinder uiBinder = GWT
			.create(SPViewImplUiBinder.class);

	interface SPViewImplUiBinder extends UiBinder<Widget, SPViewImpl> {
	}
	
	private final OsceConstants constants=GWT.create(OsceConstants.class);	
	
	private Delegate delegate;
	
	@UiField
	Label spLbl;
	
	@UiField
	FocusPanel spPanel;
	
	PopupView popupView;
	
	PopupView exchangePopupView;
	
	//Added for OMS-153.
	NamePrenamePopupView namePrenamePopupView;
	
	public FocusPanel getSpPanel() {
		return spPanel;
	}

	private AssignmentProxy assignmentProxy;
	
	public AssignmentProxy getAssignmentProxy() {
		return assignmentProxy;
	}

	public void setAssignmentProxy(AssignmentProxy assignmentProxy) {
		this.assignmentProxy = assignmentProxy;
	}	

	public Label getSpLbl() {
		return spLbl;
	}

	public SPViewImpl()
	{
		//spec change[
		RootLayoutPanel.get().addDomHandler(new ContextMenuHandler() {

			@Override
			public void onContextMenu(ContextMenuEvent event) {
				event.preventDefault();
				event.stopPropagation();
			}
		}, ContextMenuEvent.getType());
		//spec change]
		
		initWidget(uiBinder.createAndBindUi(this));
		
		//spec change[
		spPanel.addMouseDownHandler(new MouseDownHandler() {
			
			@Override
			public void onMouseDown(MouseDownEvent event) {
				
				if (event.getNativeButton() == NativeEvent.BUTTON_LEFT)
				{
					Log.info("spPanel Clicked");
					
					if (exchangePopupView != null && ((PopupViewImpl)exchangePopupView).isShowing())
					{
						((PopupViewImpl)exchangePopupView).hide();
					}
					
					if(assignmentProxy == null)
						return;
					
				/*	if( assignmentProxy.getPatientInRole()==null)
					{
						MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.warning());
						dialogBox.showConfirmationDialog(constants.patientNotAssigned());
					}
					else	*/
						showSPPanelPopup();
				}
				
				if (event.getNativeButton() == NativeEvent.BUTTON_RIGHT)
				{
					if (assignmentProxy == null || assignmentProxy.getOscePostRoom() == null)
		        		return;
					
					event.preventDefault();
					showExchangeSpPopup();
				}
				
			}
		});
		//spec change]
		//Added for OMS-153.
		registerAndHandleMouseOverAndMouseOutEvent();
	}
	//Added for OMS-153.
	/**
	 * registering and handling mouse over and mouse out event on sppanel.
	 */
	private void registerAndHandleMouseOverAndMouseOutEvent() {
		Log.info("registering mouse over event");
		spPanel.addMouseOverHandler(new MouseOverHandler() {
			
			@Override
			public void onMouseOver(MouseOverEvent event) {
					Log.info("onMouseOver() called");
					showSpPrenameNamePopup();
			}
		});
		spPanel.addMouseOutHandler(new MouseOutHandler() {
			
			@Override
			public void onMouseOut(MouseOutEvent event) {
				Log.info("onMouseOut() called");
				if(namePrenamePopupView!=null){
					namePrenamePopupView.hidePopup();
				}
			}
		});
	}
	//Added for OMS-153.
	/**
	 * show sp pre-name name popup
	 */
	private void showSpPrenameNamePopup() {
	 Log.info("showing prename name popup");	
	 
	 if(assignmentProxy!=null){
			if(namePrenamePopupView == null)
			{
				namePrenamePopupView=new NamePrenamePopupViewImpl();
				
				RootPanel.get().add(((NamePrenamePopupViewImpl)namePrenamePopupView));
			}
			
			namePrenamePopupView.setPopupPosition(this.getAbsoluteLeft()-45, this.getAbsoluteTop()-86);
			//setDAta
			
			if(assignmentProxy.getPatientInRole()!=null){
				String prename=assignmentProxy.getPatientInRole().getPatientInSemester().getStandardizedPatient().getPreName();
				String name =assignmentProxy.getPatientInRole().getPatientInSemester().getStandardizedPatient().getName();
				namePrenamePopupView.setPreNameAndName(prename!=null ? prename:constants.notAssigned(),name!=null ? name:constants.notAssigned());
			}else{
				namePrenamePopupView.setPrenameValue(constants.notAssigned());
			}
			
			namePrenamePopupView.showPopup();
	 }
		
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	
	/*@UiHandler("spPanel")
	public void spPanelClicked(ClickEvent event)
	{
		Log.info("spPanel Clicked");
		
		if(assignmentProxy == null)
			return;
		
		if( assignmentProxy.getPatientInRole()==null)
		{
			MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.warning());
			dialogBox.showConfirmationDialog(constants.patientNotAssigned());
		}
		else	
			showSPPanelPopup();
	}*/
	public void showSPPanelPopup()
	{
		if(popupView == null)
		{
			popupView=new PopupViewImpl();
			popupView.createSPPopupView();
			
			((PopupViewImpl)popupView).setAnimationEnabled(true);
		
			
			
			
			//((PopupViewImpl)popupView).setWidth("150px");

		
			RootPanel.get().add(((PopupViewImpl)popupView));
			
			
			
			
			popupView.getOkButton().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					//validate Entered Data
					((PopupViewImpl)popupView).hide();
					
				}
			});
			
			
			
		}
		
		((PopupViewImpl)popupView).setPopupPosition(this.getAbsoluteLeft()-45, this.getAbsoluteTop()-205);
		//setDAta
		
		if(assignmentProxy.getPatientInRole()!=null)
			popupView.getNameValue().setText(assignmentProxy.getPatientInRole().getPatientInSemester().getStandardizedPatient().getPreName() + " " + assignmentProxy.getPatientInRole().getPatientInSemester().getStandardizedPatient().getName());
		else
			popupView.getNameValue().setText(constants.notAssigned());
		popupView.getStartTimeValue().setText(DateTimeFormat.getShortDateTimeFormat().format(assignmentProxy.getTimeStart()));
		popupView.getEndTimeValue().setText(DateTimeFormat.getShortDateTimeFormat().format(assignmentProxy.getTimeEnd()));
		
		
		((PopupViewImpl)popupView).show();
		
	}

	//spec change[
	@Override
	public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
		return addDomHandler(handler, MouseDownEvent.getType());
	}
	
	public void showExchangeSpPopup()
	{
		if(exchangePopupView == null)
		{
			exchangePopupView=new PopupViewImpl();
			exchangePopupView.createExchangeSPPopupView();
			
			((PopupViewImpl)exchangePopupView).setAnimationEnabled(true);
	
			//((PopupViewImpl)popupView).setWidth("150px");

			//RootPanel.get().add(((PopupViewImpl)popupView));
			
			exchangePopupView.getOkButton().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					//validate Entered Data
					((PopupViewImpl)exchangePopupView).hide();
					if (exchangePopupView.getExchangeSpListBox().getSelected() == null)
						((PopupViewImpl)exchangePopupView).hide();
					else
						delegate.exchangeSpClicked(assignmentProxy, exchangePopupView.getExchangeSpListBox().getSelected());
				}
			});
		}
		
		((PopupViewImpl)exchangePopupView).setPopupPosition(this.getAbsoluteLeft()-45, this.getAbsoluteTop()-230);
		//setDAta
		
		exchangePopupView.getStartTimeValue().setText(DateTimeFormat.getShortDateTimeFormat().format(assignmentProxy.getTimeStart()));
		exchangePopupView.getEndTimeValue().setText(DateTimeFormat.getShortDateTimeFormat().format(assignmentProxy.getTimeEnd()));
		
		if(assignmentProxy.getPatientInRole()!=null)
		{
			exchangePopupView.getNameValue().setText(assignmentProxy.getPatientInRole().getPatientInSemester().getStandardizedPatient().getPreName() + " " + assignmentProxy.getPatientInRole().getPatientInSemester().getStandardizedPatient().getName());
			delegate.showExchangeSpPopup(exchangePopupView, assignmentProxy);
		}
		else
		{
			exchangePopupView.getNameValue().setText(constants.notAssigned());
			exchangePopupView.getExchangeSpListBox().setEnabled(false);
			((PopupViewImpl)exchangePopupView).show();
		}
	}
	//spec change]
	
}
