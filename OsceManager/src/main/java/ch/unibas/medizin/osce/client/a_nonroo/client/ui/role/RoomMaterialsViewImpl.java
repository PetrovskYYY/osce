package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.util.ArrayList;
import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaMainNav;
import ch.unibas.medizin.osce.client.a_nonroo.client.ResolutionSettings;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickHandler;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeHandler;
import ch.unibas.medizin.osce.client.managed.request.MaterialListProxy;
import ch.unibas.medizin.osce.client.style.resources.AdvanceCellTable;
import ch.unibas.medizin.osce.client.style.resources.MyCellTableResourcesNoSortArrow;
import ch.unibas.medizin.osce.client.style.resources.MySimplePagerResources;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.QuickSearchBox;
import ch.unibas.medizin.osce.shared.MaterialType;
import ch.unibas.medizin.osce.shared.OsMaConstant;
import ch.unibas.medizin.osce.shared.PriceType;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
import com.google.gwt.user.client.ui.Widget;

public class RoomMaterialsViewImpl extends Composite implements
		RoomMaterialsView, RecordChangeHandler, MenuClickHandler {

	private static RoomMaterialsViewImplUiBinder uiBinder = GWT
			.create(RoomMaterialsViewImplUiBinder.class);

	interface RoomMaterialsViewImplUiBinder extends
			UiBinder<Widget, RoomMaterialsViewImpl> {
	}

	// private final OsceConstants constants = GWT.create(OsceConstants.class);

	private Delegate delegate;
	@UiField
	public SplitLayoutPanel splitLayoutPanel;
	@UiField
	public SimplePanel detailsPanel;

	@UiField(provided = true)
	public QuickSearchBox searchBox;
	// @UiField
	// public IconButton filterButton;

	//cell table changes
	/*@UiField(provided = true)
	CellTable<MaterialListProxy> table;*/
	@UiField(provided = true)
	AdvanceCellTable<MaterialListProxy> table;
	//cell table changes end

	protected ArrayList<String> paths = new ArrayList<String>();

	@UiField
	IconButton AddButton;

	@UiField(provided = true)
	SimplePager Pager;

	@UiField
	HTMLPanel westPanel;
	
	@UiField
	ScrollPanel scrollPanel;
	
	int widthSize=1225,decreaseSize=0;
	Timer timer;
	
	
	@UiHandler("AddButton")
	public void newButtonClicked(ClickEvent event) {
		delegate.editClicked();

	}

	private Presenter presenter;
	private final OsceConstants constants = GWT.create(OsceConstants.class);

	public RoomMaterialsViewImpl() {
		/*CellTable.Resources tableResources = GWT
				.create(MyCellTableResources.class);*/
		//cell tbale changes 
		CellTable.Resources tableResources = GWT
				.create(MyCellTableResourcesNoSortArrow.class);
		table = new AdvanceCellTable<MaterialListProxy>(OsMaConstant.TABLE_PAGE_SIZE,
				tableResources);
//cell table chages
		SimplePager.Resources pagerResources = GWT
				.create(MySimplePagerResources.class);
		Pager = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources,
				true, OsMaConstant.TABLE_JUMP_SIZE, true);

		searchBox = new QuickSearchBox(new QuickSearchBox.Delegate() {
			@Override
			public void performAction() {
				delegate.performSearch(searchBox.getValue());
			}
		});

		initWidget(uiBinder.createAndBindUi(this));
		init();
		splitLayoutPanel.setWidgetMinSize(splitLayoutPanel.getWidget(0),
				OsMaConstant.SPLIT_PANEL_MINWIDTH);

		AddButton.setText(constants.addMaterial());
	}

	// @UiHandler("filterButton")
	// public void filterButtonHover(MouseOverEvent event) {
	// delegate.performSearch(searchBox.getValue());
	// }

	public String[] getPaths() {
		return paths.toArray(new String[paths.size()]);
	}

	public void init() {

		ResolutionSettings.setSplitLayoutPanelPosition(splitLayoutPanel,true);
//		int splitLeft = (OsMaMainNav.getMenuStatus() == 0) ? 40 : 225;
//
//		DOM.setElementAttribute(splitLayoutPanel.getElement(), "style",
//				"position: absolute; left: "+splitLeft+"px; top: 30px; right: 5px; bottom: 0px;");
//		
//		if(OsMaMainNav.getMenuStatus() == 0)
//			splitLayoutPanel.setWidgetSize(splitLayoutPanel.getWidget(0), 1412);
//		else
//			splitLayoutPanel.setWidgetSize(splitLayoutPanel.getWidget(0), 1220);

		// @SPEC

		paths.add("name");
		paths.add("");
		table.addColumn(new TextColumn<MaterialListProxy>() {
			{
				this.setSortable(true);
			}

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(MaterialListProxy materialListProxy) {
				return renderer.render((materialListProxy.getName() == null) ? ""
						: materialListProxy.getName());

			}
		}, constants.roomMaterialName());

		paths.add("type");
		paths.add("");
		table.addColumn(new TextColumn<MaterialListProxy>() {
			{
				this.setSortable(true);
			}

			Renderer<MaterialType> renderer = new EnumRenderer<MaterialType>();

			@Override
			public String getValue(MaterialListProxy materialListProxy) {
				return renderer.render(materialListProxy.getType());

			}
		}, constants.roomMaterialType());

		paths.add("price");
		paths.add("");
		table.addColumn(new TextColumn<MaterialListProxy>() {
			{
				this.setSortable(true);
			}

			Renderer<String> renderer = new AbstractRenderer<String>() {

				public String render(String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(MaterialListProxy materialListProxy) {
				return renderer.render((materialListProxy.getPrice() == null) ? ""
						: materialListProxy.getPrice().toString());

			}
		}, constants.roomMaterialPrice());

		paths.add("priceType");
		paths.add("");
		table.addColumn(new TextColumn<MaterialListProxy>() {
			{
				this.setSortable(true);
			}

			Renderer<PriceType> renderer = new EnumRenderer<PriceType>();

			@Override
			public String getValue(MaterialListProxy materialListProxy) {
				return renderer.render(materialListProxy.getPriceType());

			}
		}, constants.roomMaterialPriceType());

		// // Edit Button
		// addColumn(new ActionCell<MaterialListProxy>("Edit",
		// new ActionCell.Delegate<MaterialListProxy>() {
		// public void execute(MaterialListProxy specialization) {
		// // Window.alert("You clicked " +
		// // institution.getInstitutionName());
		// if (Window.confirm(constants.reallyDelete()))
		// delegate.
		// deleteClicked(specialization);
		// }
		// }), "", new GetValue<MaterialListProxy>() {
		// public MaterialListProxy getValue(MaterialListProxy specialization) {
		// return specialization;
		// }
		// }, null);
		// table.addColumnStyleName(1, "iconCol");

		// Delete button
		addColumn(new ActionCell<MaterialListProxy>(OsMaConstant.DELETE_ICON,
				new ActionCell.Delegate<MaterialListProxy>() {
					public void execute(final MaterialListProxy materialListProxy) {
						// Window.alert("You clicked " +
						// institution.getInstitutionName());
						/*if (Window.confirm(constants.reallyDelete()))
							delegate.deleteClicked(materialListProxy);*/
						// Issue Role
						 final MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.success());
						 dialogBox.showYesNoDialog(constants.reallyDelete());
						 dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
								
								@Override
								public void onClick(ClickEvent event) {
									dialogBox.hide();									
									Log.info("yes click");	
									delegate.deleteClicked(materialListProxy);
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
				}), "", new GetValue<MaterialListProxy>() {
			public MaterialListProxy getValue(
					MaterialListProxy materialListProxy) {
				return materialListProxy;
			}
		}, null);
		table.addColumnStyleName(1, "iconCol");
		table.addColumnStyleName(3, "iconCol");
		table.addColumnStyleName(5, "iconCol");
		table.addColumnStyleName(7, "iconCol");
		table.addColumnStyleName(8, "iconCol");
	}

	private <C> void addColumn(Cell<C> cell, String headerText,
			final GetValue<C> getter,
			FieldUpdater<MaterialListProxy, C> fieldUpdater) {
		Column<MaterialListProxy, C> column = new Column<MaterialListProxy, C>(
				cell) {
			@Override
			public C getValue(MaterialListProxy object) {
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
	public CellTable<MaterialListProxy> getTable() {
		return table;
	}

	private static interface GetValue<C> {
		C getValue(MaterialListProxy contact);
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

	@Override
	public String getQuery() {
		return searchBox.getValue();
	}

	@Override
	public void updateSearch() {
		String q = searchBox.getValue();
		delegate.performSearch(q);
	}

	// @UiHandler("showSubview")
	// public void showSubviewClicked(ClickEvent event) {
	// delegate.showSubviewClicked();
	// }
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

}
