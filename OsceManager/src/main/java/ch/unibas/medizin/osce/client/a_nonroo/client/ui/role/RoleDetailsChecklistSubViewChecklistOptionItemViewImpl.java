package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.util.HashMap;
import java.util.Map;

import ch.unibas.medizin.osce.client.a_nonroo.client.Validator;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.managed.request.ChecklistOptionProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class RoleDetailsChecklistSubViewChecklistOptionItemViewImpl extends Composite implements RoleDetailsChecklistSubViewChecklistOptionItemView{

	private static final Binder BINDER = GWT.create(Binder.class);
	
	private Delegate delegate;
	
	private RoleDetailsChecklistSubViewChecklistOptionItemViewImpl roleDetailsChecklistSubViewChecklistOptionItemViewImpl;
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	// SPEC Change
	private MessageConfirmationDialogBox confirmationDialogBox;
	
	@UiField
	Label optionLbl;
	
	public Label getOptionLbl() {
		return optionLbl;
	}

	public void setOptionLbl(Label optionLbl) {
		this.optionLbl = optionLbl;
	}

	/*public Label getOptionValueLbl() {
		return optionValueLbl;
	}

	public void setOptionValueLbl(Label optionValueLbl) {
		this.optionValueLbl = optionValueLbl;
	}*/

	/*@UiField
	Label optionValueLbl;*/
	
	@UiField
	IconButton deleteBtn;
	
	@UiField
	IconButton editBtn;	
	
	//SPEC Change
	@Override
	public IconButton getDeleteBtn() {
		return deleteBtn;
	}

	@Override
	public IconButton getEditBtn() {
		return editBtn;
	}
	//SPEC Change

	ChecklistOptionProxy proxy;
	
	public CriteriaPopupView criteriaPopup;
	
	public CheckListTopicPopupView optionPopup;
	
	Map<String, Widget> checklistOptionMap;

	public Map<String, Widget> getChecklistOptionMap() {
		return checklistOptionMap;
	}

	public void setChecklistOptionMap(Map<String, Widget> checklistOptionMap) {
		this.checklistOptionMap = checklistOptionMap;
	}

	public ChecklistOptionProxy getProxy() {
		return proxy;
	}

	public void setProxy(ChecklistOptionProxy proxy) {
		this.proxy = proxy;
	}

	public RoleDetailsChecklistSubViewChecklistOptionItemViewImpl() {
		initWidget(BINDER.createAndBindUi(this));
		optionLbl.getElement().getStyle().setProperty("wordWrap", "break-word");
		this.roleDetailsChecklistSubViewChecklistOptionItemViewImpl=this;
	}
	
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	
	interface Binder extends UiBinder<Widget, RoleDetailsChecklistSubViewChecklistOptionItemViewImpl> {
	}
	
	@UiHandler("editBtn")
	public void editOption(ClickEvent event)
	{
		optionPopup=new CheckListTopicPopupViewImpl(false);			
			
		((CheckListTopicPopupViewImpl)optionPopup).setAnimationEnabled(true);
		
		optionPopup.getDescriptionLbl().setText(constants.roleOptionValue());
				
		optionPopup.getTopicLbl().setText(constants.roleOptionName());
			
		optionPopup.getTopicTxtBox().setValue(roleDetailsChecklistSubViewChecklistOptionItemViewImpl.getProxy().getOptionName());
			
		optionPopup.getDescriptionTxtBox().setValue(roleDetailsChecklistSubViewChecklistOptionItemViewImpl.getProxy().getValue());
		
		if (roleDetailsChecklistSubViewChecklistOptionItemViewImpl.getProxy().getDescription() != null)
			optionPopup.getOptionDescTextArea().setValue(roleDetailsChecklistSubViewChecklistOptionItemViewImpl.getProxy().getDescription());
		
		if(roleDetailsChecklistSubViewChecklistOptionItemViewImpl.getProxy().getCriteriaCount() != null)
			optionPopup.getCriteriaCountLstBox().setSelectedIndex(roleDetailsChecklistSubViewChecklistOptionItemViewImpl.getProxy().getCriteriaCount().intValue());
			
		((CheckListTopicPopupViewImpl)optionPopup).setWidth("160px");	
			
		RootPanel.get().add(((CheckListTopicPopupViewImpl)optionPopup));

			// Highlight onViolation
		checklistOptionMap=new HashMap<String, Widget>();
		checklistOptionMap.put("optionName", optionPopup.getTopicTxtBox());
		checklistOptionMap.put("name", optionPopup.getTopicTxtBox());
		checklistOptionMap.put("value", optionPopup.getDescriptionTxtBox());
			// E Highlight onViolation
				
		optionPopup.getOkBtn().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
					
				// SPEC Change
				
				if(Validator.isNotNull(optionPopup.getTopicTxtBox().getValue(),optionPopup.getDescriptionTxtBox().getValue()))
				{
					if (!optionPopup.getDescriptionTxtBox().getValue().matches("[0-9]+"))
					{
						confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
						confirmationDialogBox.showConfirmationDialog(constants.checklistOptionErr());
					}
					else
					{
						//delegate.saveCheckListTopic(optionPopup.getTopicTxtBox().getValue(),optionPopup.getDescriptionTxtBox().getValue());
						delegate.updateOption(optionPopup.getTopicTxtBox().getValue(), optionPopup.getDescriptionTxtBox().getValue(), optionPopup.getOptionDescTextArea().getValue(), roleDetailsChecklistSubViewChecklistOptionItemViewImpl);
						((CheckListTopicPopupViewImpl)optionPopup).hide(true);
						
						optionPopup.getTopicTxtBox().setValue("");
						optionPopup.getDescriptionTxtBox().setValue("");
					}
					
				}
				else
				{
					confirmationDialogBox = new MessageConfirmationDialogBox(constants.warning());
					confirmationDialogBox.showConfirmationDialog(constants.warningFillRequiredFields());
				}
			}
		});

		// Issue Role V1 
		optionPopup.getCancelBtn().addClickHandler(new ClickHandler() 
		{				
			@Override
			public void onClick(ClickEvent event) 
			{
				((CheckListTopicPopupViewImpl)optionPopup).hide(true);					
				optionPopup.getTopicTxtBox().setValue("");
				optionPopup.getDescriptionTxtBox().setValue("");
			}
		});	
		// E: Issue Role V1
		
		((CheckListTopicPopupViewImpl)optionPopup).setPopupPosition(editBtn.getAbsoluteLeft()-205, editBtn.getAbsoluteTop()-280); // SPEC Change+
		((CheckListTopicPopupViewImpl)optionPopup).show();
	}
	
	@UiHandler("deleteBtn")
	public void deleteOption(ClickEvent event)
	{
	/*	if(Window.confirm("are you sure you want to delete this option?"))*/
		// Issue Role
				 final MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.warning());
				 dialogBox.showYesNoDialog("are you sure you want to delete this option?");
				 dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							dialogBox.hide();
							Log.info("Yes click");
							delegate.deleteOption(roleDetailsChecklistSubViewChecklistOptionItemViewImpl);
							return;
							
						}
					});
					
						dialogBox.getNoBtnl().addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							dialogBox.hide();
							Log.info("no click");
							return;
							
						}
					});
				// E: Issue Role
	}
	
}
