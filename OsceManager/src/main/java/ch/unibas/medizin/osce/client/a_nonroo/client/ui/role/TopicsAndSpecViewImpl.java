package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaMainNav;
import ch.unibas.medizin.osce.client.a_nonroo.client.ResolutionSettings;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickHandler;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeHandler;
import ch.unibas.medizin.osce.client.managed.request.SpecialisationProxy;
import ch.unibas.medizin.osce.client.style.resources.AdvanceCellTable;
import ch.unibas.medizin.osce.client.style.resources.MyCellTableResourcesNoSortArrow;
import ch.unibas.medizin.osce.client.style.resources.MySimplePagerResources;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.QuickSearchBox;
import ch.unibas.medizin.osce.shared.OsMaConstant;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class TopicsAndSpecViewImpl extends Composite implements  TopicsAndSpecView, RecordChangeHandler, MenuClickHandler {
	
	private static TopicsAndSpecViewUiBinder uiBinder = GWT
			.create(TopicsAndSpecViewUiBinder.class);

	interface TopicsAndSpecViewUiBinder extends UiBinder<Widget, TopicsAndSpecViewImpl> {
	}
	
	// Violation Changes Highlight
	Map<String, Widget> viewMap;
	// E Violation Changes Highlight
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);

	private Delegate delegate;
	// Issue Role
		int left=0,top=0;
	
	@UiField
	public SplitLayoutPanel splitLayoutPanel;
	
	@UiField
	public SimplePanel detailsPanel;
	
	@UiField (provided = true)
	public QuickSearchBox searchBox;
	
//	@UiField
//	Button FilterButton;
	

//cell table chages
	/*@UiField(provided=true)
	CellTable<SpecialisationProxy> table;*/
	@UiField(provided=true)
	AdvanceCellTable<SpecialisationProxy> table;
	//cell table changes end
	protected List<String> paths = new ArrayList<String>();
	
//	protected Set<String> paths = new HashSet<String>();
	@UiField
	TextBox	AddTextBox;
	
	@UiField 
	IconButton AddButton;
	
	@UiField (provided = true)
	SimplePager Pager;
		
	@UiField
	HTMLPanel westPanel;
	
	@UiField
	ScrollPanel scrollPanel;
	
	int widthSize=1225,decreaseSize=0;
	Timer timer;
	
	
	@UiHandler ("AddButton")
	public void newButtonClicked(ClickEvent event) {
		delegate.newClicked(AddTextBox.getValue());
		AddTextBox.setValue("");
	}
	private Presenter presenter;
	
	public TopicsAndSpecViewImpl() {
		// TODO Auto-generated constructor stub
	//	CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
	//	table = new CellTable<SpecialisationProxy>(OsMaConstant.TABLE_PAGE_SIZE, tableResources);
		
				
		//cell table changes start
		/*CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		table = new CellTable<SpecialisationProxy>(OsMaConstant.TABLE_PAGE_SIZE, tableResources);*/
		CellTable.Resources tableResources = GWT.create(MyCellTableResourcesNoSortArrow.class);
		table = new AdvanceCellTable<SpecialisationProxy>(OsMaConstant.TABLE_PAGE_SIZE, tableResources);
				//cell table chnages end
		SimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		Pager = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources, true, OsMaConstant.TABLE_JUMP_SIZE, true);
		
		searchBox = new QuickSearchBox(new QuickSearchBox.Delegate() {
			@Override
			public void performAction() {
				delegate.performSearch(searchBox.getValue());
			}
		});
				
				initWidget(uiBinder.createAndBindUi(this));
				init();
				splitLayoutPanel.setWidgetMinSize(splitLayoutPanel.getWidget(0), OsMaConstant.SPLIT_PANEL_MINWIDTH);		
//				FilterButton.setText("Filter");	
				AddButton.setText(constants.addDiscipline());
				
				// Violation Changes Highlight
				Log.info("Call TopicsAndSpecViewImpl Constructor..");
				viewMap=new HashMap<String, Widget>();				
				viewMap.put("name",AddTextBox);
			// E Violation Changes Highlight
				
				
	}
	
	
	
	public String getQuery() {
		return searchBox.getValue();
	}
	// @Manish
	public List<String> getPaths() {
		return paths;
	}
	
	
	

	public void init()
	{
		ResolutionSettings.setSplitLayoutPanelPosition(splitLayoutPanel,true);
//		int splitLeft = (OsMaMainNav.getMenuStatus() == 0) ? 40 : 225;
//
//		// bugfix to avoid hiding of all panels (maybe there is a better solution...?!)
//		DOM.setElementAttribute(splitLayoutPanel.getElement(), "style", "position: absolute; left: "+splitLeft+"px; top: 30px; right: 5px; bottom: 0px;");
//		
//		if(OsMaMainNav.getMenuStatus() == 0)
//			splitLayoutPanel.setWidgetSize(splitLayoutPanel.getWidget(0), 1412);
//		else
//			splitLayoutPanel.setWidgetSize(splitLayoutPanel.getWidget(0), 1220);
		
		AddTextBox.addKeyDownHandler(new KeyDownHandler() {
		    @Override
		    public void onKeyDown(KeyDownEvent event) {
		        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
		        	newButtonClicked(null);
		    }
		});
		
		
		//By spec for table insert
		
		
		// Issue Role
		
		table.addDomHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) 
			{
				left=event.getClientX();
				top=event.getClientY();
			}
		}, ClickEvent.getType());
		
		// E: Issue Role		
		
		paths.add("name");
		table.addColumn(new TextColumn<SpecialisationProxy>() {
			{ this.setSortable(true); }

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(SpecialisationProxy object) {
				return renderer.render(object.getName());
				
			}
		},constants.clinicalSpecialisation());
				
		//Edit Button
		addColumn(new ActionCell<SpecialisationProxy>(
				OsMaConstant.EDIT_ICON, new ActionCell.Delegate<SpecialisationProxy>() {
					public void execute(final SpecialisationProxy specialization) {
										
						//delegate.editClicked(specialization);
						//Issue Role				
						delegate.editClicked(specialization,left,top);
											
					}
				}), "", new GetValue<SpecialisationProxy>() {
			public SpecialisationProxy getValue(SpecialisationProxy specialization) {
				return specialization;
			}
		}, null);
		table.addColumnStyleName(2, "iconCol");

		// Detete button
		addColumn(new ActionCell<SpecialisationProxy>(
				OsMaConstant.DELETE_ICON, new ActionCell.Delegate<SpecialisationProxy>() {
					public void execute(final SpecialisationProxy specialization) {
						//Window.alert("You clicked " + institution.getInstitutionName());
						
						/*if(Window.confirm(constants.reallyDelete()))
							delegate.deleteClicked(specialization);*/
						
						// Issue Role
						 final MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.warning());
						 dialogBox.showYesNoDialog(constants.reallyDelete());
						 dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
								
								@Override
								public void onClick(ClickEvent event) {
									dialogBox.hide();									
									Log.info("yes click");
									delegate.deleteClicked(specialization);
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
				}), "", new GetValue<SpecialisationProxy>() {
			public SpecialisationProxy getValue(SpecialisationProxy specialization) {
				return specialization;
			}
		}, null);
		table.addColumnStyleName(3, "iconCol");
	}
	
	private <C> void addColumn(Cell<C> cell, String headerText,
			final GetValue<C> getter, FieldUpdater<SpecialisationProxy, C> fieldUpdater) {
		Column<SpecialisationProxy, C> column = new Column<SpecialisationProxy, C>(cell) {
			@Override
			public C getValue(SpecialisationProxy object) {
				return getter.getValue(object);
			}
		};
		column.setFieldUpdater(fieldUpdater);
		if (cell instanceof AbstractEditableCell<?, ?>) {
			editableCells.add((AbstractEditableCell<?, ?>) cell);
		}
		table.addColumn(column);
	}
	
	private List<AbstractEditableCell<?, ?>> editableCells;
	
	@Override
	public CellTable<SpecialisationProxy> getTable() {
		return table;
	}
	
	private static interface GetValue<C> {
		C getValue(SpecialisationProxy contact);
	}
	
	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	
	@Override
	public SimplePanel getDetailsPanel() {
		return detailsPanel;
	}
	public void setDetailPanel(boolean isDetailPlace) {

//		splitLayoutPanel.setWidgetSize(westPanel, OsMaConstant.WIDTH_SIZE - OsMaConstant.WIDTH_MIN );
		ResolutionSettings.setSplitLayoutPanelAnimation(splitLayoutPanel);
		splitLayoutPanel.animate(OsMaConstant.ANIMATION_TIME);	
		/*splitLayoutPanel.animate(150000);
//		widthSize = 1200;
//		decreaseSize = 0;
//		splitLayoutPanel.setWidgetSize(westPanel, widthSize);
		if (isDetailPlace) {

			timer = new Timer() {
				@Override
				public void run() {
					if (decreaseSize <= 705) {
						splitLayoutPanel.setWidgetSize(westPanel, 1225
								- decreaseSize);
						decreaseSize += 5;
					} else {
						timer.cancel();
					}
				}
			};
			timer.schedule(1);
			timer.scheduleRepeating(1);

		} else {
			widthSize = 1225;
			decreaseSize = 0;
			splitLayoutPanel.setWidgetSize(westPanel, widthSize);
		}*/
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
		
	// Violation Changes Highlight

	@Override
	public Map getMap() {
		// TODO Auto-generated method stub
		return this.viewMap;
	}

	@Override
	public TextBox getTextBox() 
	{
		return this.AddTextBox;		
	}
	// E Violation Changes Highlight
	
	// by spec
	@Override
	public void onRecordChange(RecordChangeEvent event) {
		int pagesize = 0;

		if (event.getRecordValue() == "ALL") {
			pagesize = table.getRowCount();
			OsMaConstant.TABLE_PAGE_SIZE = pagesize;
		} else {
			pagesize = Integer.parseInt(event.getRecordValue());
		}

		table.setPageSize(pagesize);
	}
	// by spec

	@Override
	public void onMenuClicked(MenuClickEvent event) {
		
		OsMaMainNav.setMenuStatus(event.getMenuStatus());		
		ResolutionSettings.setSplitLayoutPanelPosition(splitLayoutPanel,false);
//		int left = (OsMaMainNav.getMenuStatus() == 0) ? 40 : 225;
//		
//		DOM.setElementAttribute(splitLayoutPanel.getElement(), "style", "position: absolute; left: "+left+"px; top: 30px; right: 5px; bottom: 0px;");
//		
//		if(splitLayoutPanel.getWidget(0).getOffsetWidth() >= 1220){
//			
//			if(OsMaMainNav.getMenuStatus() == 0)
//				splitLayoutPanel.setWidgetSize(splitLayoutPanel.getWidget(0), 1412);
//			else
//				splitLayoutPanel.setWidgetSize(splitLayoutPanel.getWidget(0), 1220);
//		}
			
	}



	@Override
	public String[] getPathsArray() {
		// TODO Auto-generated method stub
		Set<String> pa = new HashSet<String>();
		pa.addAll(paths);
		
		return pa.toArray(new String[paths.size()]);
		//return null;
		
	}


}
