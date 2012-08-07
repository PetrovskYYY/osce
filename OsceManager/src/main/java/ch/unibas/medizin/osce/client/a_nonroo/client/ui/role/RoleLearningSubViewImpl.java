package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaConstant;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.managed.request.MainSkillProxy;
import ch.unibas.medizin.osce.client.managed.request.MinorSkillProxy;
import ch.unibas.medizin.osce.client.style.resources.MyCellTableResources;
import ch.unibas.medizin.osce.client.style.resources.MySimplePagerResources;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.HasDirection.Direction;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.Widget;

public class RoleLearningSubViewImpl extends Composite implements RoleLearningSubView {

	private static RoleLearningSubViewUiBinder uiBinder = GWT
			.create(RoleLearningSubViewUiBinder.class);

	interface RoleLearningSubViewUiBinder extends
			UiBinder<Widget, RoleLearningSubViewImpl> {
	}

	Map<String, Widget> mainSkillMap;
	
	private List<AbstractEditableCell<?, ?>> editableCells;
	
	protected List<String> paths = new ArrayList<String>();

	private Delegate delegate;
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	@UiField (provided = true)
	public CellTable<MainSkillProxy> majorTable;
	
	@UiField (provided = true)
	public CellTable<MinorSkillProxy> minorTable;
	
	
	@UiField (provided = true)
	public SimplePager pagerMajor;
	
	@UiField (provided = true)
	public SimplePager pagerMinor;
		
	@UiField
	public IconButton btnAddMajor;
	
	@UiField
	public IconButton btnAddMinor;
	
	public RoleLearningPopUpView popUpView;
			
	public RoleLearningSubViewImpl() 
	{
		SimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		pagerMajor = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources, true, OsMaConstant.TABLE_JUMP_SIZE, true);
		pagerMinor = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources, true, OsMaConstant.TABLE_JUMP_SIZE, true);
		
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		majorTable = new CellTable<MainSkillProxy>(OsMaConstant.TABLE_PAGE_SIZE, tableResources);
		minorTable = new CellTable<MinorSkillProxy>(OsMaConstant.TABLE_PAGE_SIZE, tableResources);
		
		initWidget(uiBinder.createAndBindUi(this));
	
		btnAddMajor.setText(constants.majorBtnLbl());
		btnAddMinor.setText(constants.minorBtnLbl());
		
		majorTable.addStyleName("skillTable");
		minorTable.addStyleName("skillTable");
		
		initMajorTable();
		
		initMinorTable();				
	}
	
	private void initMajorTable() 
	{
		paths.add("mainClassi");
		TextColumn<MainSkillProxy> mainClassiCol = new TextColumn<MainSkillProxy>() 
		{
			{ 
				this.setSortable(true); 
			}

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() 
			{
				public String render(java.lang.String obj) 
				{
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(MainSkillProxy object) 
			{
				return renderer.render(object.getSkill().getTopic().getClassificationTopic().getMainClassification().getShortcut());
			}
		};
		
		majorTable.addColumn(mainClassiCol, constants.mainClassi());		
		majorTable.setColumnWidth(mainClassiCol, "130px");
				
		paths.add("classificatonTopic");
		TextColumn<MainSkillProxy> classificationTopicCol = new TextColumn<MainSkillProxy>() 
		{
			{ 
				this.setSortable(true); 
			}

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() 
			{
				public String render(java.lang.String obj) 
				{
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(MainSkillProxy object) 
			{
				return renderer.render(object.getSkill().getTopic().getClassificationTopic().getShortcut());
			}
		};
		
		majorTable.addColumn(classificationTopicCol, constants.classiTopic());
		majorTable.setColumnWidth(classificationTopicCol, "135px");
		
		paths.add("topic");
		TextColumn<MainSkillProxy> topicCol = new TextColumn<MainSkillProxy>() {
			{ 
				this.setSortable(true); 
			}

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() 
			{
				public String render(java.lang.String obj) 
				{
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(MainSkillProxy object) 
			{
				return renderer.render(object.getSkill().getTopic().getTopicDesc());
			}
		};
		majorTable.setTableLayoutFixed(true);
		majorTable.addColumn(topicCol, constants.topicLbl());
			
		paths.add("skillLevel");
		TextColumn<MainSkillProxy> skillLevelCol = new TextColumn<MainSkillProxy>() 
		{
			{ 
				this.setSortable(true); 
			}

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() 
			{
				public String render(java.lang.String obj) 
				{
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(MainSkillProxy object) 
			{
				if (object.getSkill().getSkillLevel() == null)
					return renderer.render("");
				else
					return renderer.render(String.valueOf(object.getSkill().getSkillLevel().getLevelNumber()));
			}
		};
		
		majorTable.setColumnWidth(skillLevelCol, "75px");		
		majorTable.addColumn(skillLevelCol, constants.skillLevel());		
		//majorTable.addColumnStyleName(2, "topicCol");
		
		
		addColumnMajor(new ActionCell<MainSkillProxy>(
				OsMaConstant.DELETE_ICON, new ActionCell.Delegate<MainSkillProxy>() {
					public void execute(final MainSkillProxy mainSkill) {
						
						 final MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.success());
						 dialogBox.showYesNoDialog(constants.reallyDelete());
						 dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
								
								@Override
								public void onClick(ClickEvent event) {
									dialogBox.hide();
									//delegate.deleteDoctorClicked(roleParticipantProxy,1);
									delegate.majorDeleteClicked(mainSkill);
									return;

										}
									});

							dialogBox.getNoBtnl().addClickHandler(new ClickHandler() {
								
								@Override
								public void onClick(ClickEvent event) {
									dialogBox.hide();
									return;
									
								}
							});
					}
				}), "", new GetValueMajor<MainSkillProxy>() {
			public MainSkillProxy getValue(MainSkillProxy mainskill) {
				return mainskill;
			}
		}, null);

		majorTable.addColumnStyleName(4, "iconCol");
			
	}
	
	public void initMinorTable()
	{
		paths.add("mainClassi");
		TextColumn<MinorSkillProxy> mainClassiCol = new TextColumn<MinorSkillProxy>() 
		{
			{ 
				this.setSortable(true); 
			}

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() 
			{
				public String render(java.lang.String obj) 
				{
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(MinorSkillProxy object) 
			{
				return renderer.render(object.getSkill().getTopic().getClassificationTopic().getMainClassification().getShortcut());
			}
		};
		minorTable.addColumn(mainClassiCol, constants.mainClassi());
		minorTable.setColumnWidth(mainClassiCol, "130px");
			
		paths.add("classificatonTopic");
		TextColumn<MinorSkillProxy> classificationTopicCol = new TextColumn<MinorSkillProxy>() 
		{
			{ 
				this.setSortable(true); 
			}

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() 
			{
				public String render(java.lang.String obj) 
				{
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(MinorSkillProxy object) 
			{
				return renderer.render(object.getSkill().getTopic().getClassificationTopic().getShortcut());
			}
		};
		minorTable.addColumn(classificationTopicCol, constants.classiTopic());
		minorTable.setColumnWidth(classificationTopicCol, "135px");
		
		paths.add("topic");
		TextColumn<MinorSkillProxy> topicCol = new TextColumn<MinorSkillProxy>() 
		{
			{ 
				this.setSortable(true); 
			}

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() 
			{
				public String render(java.lang.String obj) 
				{
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(MinorSkillProxy object) 
			{
				return renderer.render(object.getSkill().getTopic().getTopicDesc());
			}
		};
		minorTable.setTableLayoutFixed(true);
		minorTable.addColumn(topicCol, constants.topicLbl());
			
		paths.add("skillLevel");
		TextColumn<MinorSkillProxy> skillLevelCol = new TextColumn<MinorSkillProxy>() 
		{
			{ 
				this.setSortable(true); 
			}

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() 
			{
				public String render(java.lang.String obj) 
				{
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(MinorSkillProxy object) 
			{
				if (object.getSkill().getSkillLevel() == null)
					return renderer.render("");
				else
					return renderer.render(String.valueOf(object.getSkill().getSkillLevel().getLevelNumber()));
			}
		};
		minorTable.addColumn(skillLevelCol, constants.skillLevel());
		minorTable.setColumnWidth(skillLevelCol, "75px");
		
		addColumnMinor(new ActionCell<MinorSkillProxy>(
				OsMaConstant.DELETE_ICON, new ActionCell.Delegate<MinorSkillProxy>() {
					public void execute(final MinorSkillProxy minorSkill) {
						
						 final MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.success());
						 dialogBox.showYesNoDialog(constants.reallyDelete());
						 dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
								
								@Override
								public void onClick(ClickEvent event) {
									dialogBox.hide();
									delegate.minorDeleteClicked(minorSkill);
									return;

										}
									});

							dialogBox.getNoBtnl().addClickHandler(new ClickHandler() {
								
								@Override
								public void onClick(ClickEvent event) {
									dialogBox.hide();
									return;
									
								}
							});
					}
				}), "", new GetValueMinor<MinorSkillProxy>() {
			public MinorSkillProxy getValue(MinorSkillProxy mainskill) {
				return mainskill;
			}
		}, null);

		minorTable.addColumnStyleName(4, "iconCol");
	}
	
	private <C> void addColumnMajor(Cell<C> cell, String headerText,final GetValueMajor<C> getter, FieldUpdater<MainSkillProxy, C> fieldUpdater) 
	{
		Column<MainSkillProxy, C> column = new Column<MainSkillProxy, C>(cell) 
		{
			@Override
			public C getValue(MainSkillProxy object) 
			{				
				return getter.getValue(object);
			}
		};
		column.setFieldUpdater(fieldUpdater);
		if (cell instanceof AbstractEditableCell<?, ?>) 
		{
			editableCells.add((AbstractEditableCell<?, ?>) cell);
		}
		majorTable.addColumn(column, headerText);
	}
	
	private static interface GetValueMajor<C> {
		C getValue(MainSkillProxy proxyvalue);
	}
	
	private <C> void addColumnMinor(Cell<C> cell, String headerText,final GetValueMinor<C> getter, FieldUpdater<MinorSkillProxy, C> fieldUpdater) 
	{
		Column<MinorSkillProxy, C> column = new Column<MinorSkillProxy, C>(cell) 
		{
			@Override
			public C getValue(MinorSkillProxy object) 
			{				
				return getter.getValue(object);
			}
		};
		column.setFieldUpdater(fieldUpdater);
		if (cell instanceof AbstractEditableCell<?, ?>) 
		{
			editableCells.add((AbstractEditableCell<?, ?>) cell);
		}
		minorTable.addColumn(column, headerText);
	}
	
	private static interface GetValueMinor<C> {
		C getValue(MinorSkillProxy proxyvalue);
	}
			
	@UiHandler("btnAddMajor")
	public void btnAddAuthorClicked(ClickEvent event)
	{		
		popUpView = new RoleLearningPopUpViewImpl();
		
		((RoleLearningPopUpViewImpl)popUpView).setAnimationEnabled(true);
		delegate.setMainClassiPopUpListBox(popUpView);
		delegate.setSkillLevelPopupListBox(popUpView);
		((RoleLearningPopUpViewImpl)popUpView).setWidth("150px");		
		
		mainSkillMap=new HashMap<String, Widget>();
		mainSkillMap.put("role", popUpView.getTopicListBox());
		mainSkillMap.put("skill", popUpView.getTopicListBox());
		mainSkillMap.put("role",  popUpView.getLevelListBox());
		mainSkillMap.put("skill",  popUpView.getLevelListBox());
		
		
		popUpView.getOkBtn().addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				
				if (popUpView.getTopicListBox().getValue() == null)
				{
					MessageConfirmationDialogBox dialogBox = new MessageConfirmationDialogBox(constants.warning());
					dialogBox.showConfirmationDialog("Topic is Not Selected");
				}	
				else
				{
					delegate.addMainSkillClicked(popUpView.getTopicListBox().getValue(), popUpView.getLevelListBox().getValue());
					((RoleLearningPopUpViewImpl)popUpView).hide();
				}
			}
		});
		
		popUpView.getCancelBtn().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				((RoleLearningPopUpViewImpl)popUpView).hide();
			}
		});
		
		((RoleLearningPopUpViewImpl)popUpView).setPopupPosition(event.getClientX(), event.getClientY() - 175);
		((RoleLearningPopUpViewImpl)popUpView).show();		
	}
	
	@UiHandler("btnAddMinor")
	public void btnAddReviewerClicked(ClickEvent event)
	{
		popUpView = new RoleLearningPopUpViewImpl();
		
		((RoleLearningPopUpViewImpl)popUpView).setAnimationEnabled(true);
		delegate.setMainClassiPopUpListBox(popUpView);
		delegate.setSkillLevelPopupListBox(popUpView);
		((RoleLearningPopUpViewImpl)popUpView).setWidth("150px");		
		
		mainSkillMap=new HashMap<String, Widget>();
		mainSkillMap.put("role", popUpView.getTopicListBox());
		mainSkillMap.put("skill", popUpView.getTopicListBox());
		mainSkillMap.put("role",  popUpView.getLevelListBox());
		mainSkillMap.put("skill",  popUpView.getLevelListBox());
		
		
		popUpView.getOkBtn().addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				if (popUpView.getTopicListBox().getValue() == null)
				{
					MessageConfirmationDialogBox dialogBox = new MessageConfirmationDialogBox(constants.warning());
					dialogBox.showConfirmationDialog("Topic is Not Selected");
				}	
				else
				{
					delegate.addMinorSkillClicked(popUpView.getTopicListBox().getValue(), popUpView.getLevelListBox().getValue());
					((RoleLearningPopUpViewImpl)popUpView).hide();
				}
			}
		});
		
		popUpView.getCancelBtn().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				((RoleLearningPopUpViewImpl)popUpView).hide();
			}
		});
		
		((RoleLearningPopUpViewImpl)popUpView).setPopupPosition(event.getClientX(), event.getClientY() - 175);
		((RoleLearningPopUpViewImpl)popUpView).show();		

	}


	@Override
	public void setDelegate(Delegate delegate) 
	{
		this.delegate=delegate;			
	}
	
	public String[] getPaths() {
		return paths.toArray(new String[paths.size()]);
	}

	@Override
	public Map getMainSkillMap()
	{
		return this.mainSkillMap;
	}
}