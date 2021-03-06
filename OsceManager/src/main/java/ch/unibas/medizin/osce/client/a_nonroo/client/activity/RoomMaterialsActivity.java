package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoomMaterialsDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoomMaterialsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoomMaterialsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoomMaterialsViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenHandler;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeEvent;
import ch.unibas.medizin.osce.client.managed.request.AdvancedSearchCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.MaterialListProxy;
import ch.unibas.medizin.osce.client.managed.request.MaterialListRequest;
import ch.unibas.medizin.osce.client.style.resources.AdvanceCellTable;
import ch.unibas.medizin.osce.shared.Operation;
import ch.unibas.medizin.osce.shared.Sorting;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.google.web.bindery.requestfactory.shared.Violation;

public class RoomMaterialsActivity extends AbstractActivity implements
		RoomMaterialsView.Presenter, RoomMaterialsView.Delegate {

	/** Holds the applications request factory */
	private OsMaRequestFactory requests;
	/** Holds the applications placeController */
	private PlaceController placeController;
	/** Holds the applications' activityManager */
	private ActivityManager activityManger;
	/** Holds the panel in which the view will be displayed */
	private AcceptsOneWidget widget;
	/** Holds the main view managed by this activity */
	private RoomMaterialsView view;
	/** Holds this activities' activityMapper */
	private RoomMaterialsDetailsActivityMapper RoomMaterialsDetailsActivityMapper;
	private String quickSearchTerm = "";
	private MaterialListRequest requestAdvSeaCritStd;
	/** List of fields that should be searched for the quickSearchTerm */
	private List<String> searchThrough = Arrays.asList("name");

	private HandlerRegistration rangeChangeHandler;
	// @SPEC table to add data and remove
	//cell table changes
	/*private CellTable<MaterialListProxy> table;*/
	private AdvanceCellTable<MaterialListProxy> table;
	int x;
	int y;
	//cell table chnages

	/* @SPEC holds the selection model of the specialisation table */
	private SingleSelectionModel<MaterialListProxy> selectionModel;
	private RoomMaterialsDetailsPlace place;
	private List<AdvancedSearchCriteriaProxy> searchCriteria = new ArrayList<AdvancedSearchCriteriaProxy>();

	public Sorting sortorder = Sorting.ASC;
	public String sortname = "name";

	public RoomMaterialsActivity(OsMaRequestFactory requests,
			PlaceController placeController) {
		this.requests = requests;
		this.placeController = placeController;
		RoomMaterialsDetailsActivityMapper = new RoomMaterialsDetailsActivityMapper(
				requests, placeController);
		this.activityManger = new ActivityManager(
				RoomMaterialsDetailsActivityMapper, requests.getEventBus());
		RoomMaterialsDetailsActivity.roomMaterialsActivity = this;
	}

	public RoomMaterialsActivity(RoomMaterialsDetailsPlace place,
			OsMaRequestFactory requests, PlaceController placeController) {
		this.place = place;
		this.requests = requests;
		this.placeController = placeController;
	}

	/**
	 * Clean up activity on finish (close popups and disable display of current
	 * activities view)
	 */
	public void onStop() {

		activityManger.setDisplay(null);
	}
	
	public void registerLoading() {
		ApplicationLoadingScreenEvent.register(requests.getEventBus(),
				new ApplicationLoadingScreenHandler() {
					@Override
					public void onEventReceived(
							ApplicationLoadingScreenEvent event) {
						Log.info(" ApplicationLoadingScreenEvent onEventReceived Called");
					event.display();
					}
				});
	}

	/**
	 * Initializes the corresponding views and initializes the tables as well as
	 * their corresponding handlers.
	 */

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("RoomMaterialsActivity.start()");

		final RoomMaterialsView roomMaterialsView = new RoomMaterialsViewImpl();

		roomMaterialsView.setPresenter(this);

		this.widget = panel;
		this.view = roomMaterialsView;
		widget.setWidget(roomMaterialsView.asWidget());
		
		//by spec
		RecordChangeEvent.register(requests.getEventBus(), (RoomMaterialsViewImpl)view);
		//by spec
		
		MenuClickEvent.register(requests.getEventBus(), (RoomMaterialsViewImpl)view);
		
		setTable(view.getTable());

		init();
		initSearch();

		activityManger.setDisplay(view.getDetailsPanel());

		ProvidesKey<MaterialListProxy> keyProvider = ((AbstractHasData<MaterialListProxy>) table)
				.getKeyProvider();
		selectionModel = new SingleSelectionModel<MaterialListProxy>(
				keyProvider);
		table.setSelectionModel(selectionModel);

		//celltable changes start
		table.addHandler(new MouseDownHandler() {

			@Override
			public void onMouseDown(MouseDownEvent event) {
				// TODO Auto-generated method stub
				Log.info("mouse down");
				x = event.getClientX();
				y = event.getClientY();

				if(table.getRowCount()>0)
				{
				Log.info(table.getRowElement(0).getAbsoluteTop() + "--"+ event.getClientY());

				
				if (event.getNativeButton() == NativeEvent.BUTTON_RIGHT&& event.getClientY() < table.getRowElement(0).getAbsoluteTop()) {
					
					table.getPopup().setPopupPosition(x, y);
					table.getPopup().show();

					Log.info("right event");
				}
				}
				else
				{
					if(event.getNativeButton() == NativeEvent.BUTTON_RIGHT)
					{
						table.getPopup().setPopupPosition(x, y);
						table.getPopup().show();
					}
				}
			}
		}, MouseDownEvent.getType());
		
		
		table.getPopup().addDomHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				// TODO Auto-generated method stub
				//addColumnOnMouseout();
				table.getPopup().hide();
				
			}
		}, MouseOutEvent.getType());
		
	 table.addColumnSortHandler(new ColumnSortEvent.Handler() {

			@Override
			public void onColumnSort(ColumnSortEvent event) {
				// By SPEC[Start

				Column<MaterialListProxy, String> col = (Column<MaterialListProxy, String>) event.getColumn();
				
				
				int index = table.getColumnIndex(col);
				
				
				/*String[] path =	systemStartView.getPaths();	            			
				sortname = path[index];
				sortorder=(event.isSortAscending())?Sorting.ASC:Sorting.DESC;	*/
				
				if (index % 2 == 1 ) {
					
					table.getPopup().setPopupPosition(x, y);
					table.getPopup().show();

				} else {
					// path = systemStartView.getPaths();
					//String[] path =	systemStartView.getPaths();	
					//int index = table.getColumnIndex(col);
					String[] path = roomMaterialsView.getPaths();
					sortname = path[index];
					sortorder = (event.isSortAscending()) ? Sorting.ASC
							: Sorting.DESC;
					// By SPEC]end
					RoomMaterialsActivity.this.onRangeChanged();
					
					
				}
			}
		});
		
//cell table chanes
	 
	 //Commented below code so when user click on delete button no detail view will be displayed and added code after commented code to handle table click handler
	
	 /*selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					public void onSelectionChange(SelectionChangeEvent event) {
						MaterialListProxy selectedObject = selectionModel
								.getSelectedObject();
						if (selectedObject != null) {
							requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
							view.setDetailPanel(true);
							requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
							Log.debug(selectedObject.getName() + " selected!");
							showDetails(selectedObject);
						}
						else{
							view.setDetailPanel(false);
						}
					}
				});*/

	 table.addCellPreviewHandler(new Handler<MaterialListProxy>() {

			@Override
			public void onCellPreview(CellPreviewEvent<MaterialListProxy> event) {
				
				boolean isClicked="click".equals(event.getNativeEvent().getType());
				if(isClicked){
				//Window.alert("Column Clicked :"+ event.getColumn());
				if(event.getColumn()!=8){
					MaterialListProxy selectedObject = selectionModel.getSelectedObject();
				
				if (selectedObject != null) {
					requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
					view.setDetailPanel(true);
					requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
					
					showDetails(selectedObject);
				}
				else{
					view.setDetailPanel(false);
				 }
				}
				}	
			}
				});

		table.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			public void onRangeChange(RangeChangeEvent event) {
				RoomMaterialsActivity.this.onRangeChanged();
			}
		});

		/*table.addColumnSortHandler(new ColumnSortEvent.Handler() {
			@Override
			public void onColumnSort(ColumnSortEvent event) {
				// By SPEC[Start
				Column<MaterialListProxy, String> col = (Column<MaterialListProxy, String>) event
						.getColumn();
				int index = table.getColumnIndex(col);
				String[] path = roomMaterialsView.getPaths();
				sortname = path[index];
				sortorder = (event.isSortAscending()) ? Sorting.ASC
						: Sorting.DESC;
				// By SPEC]end
				RoomMaterialsActivity.this.onRangeChanged();

			}
		});
*/
		view.setDelegate(this);

	}

	protected void showDetails(MaterialListProxy materialListProxy) {
		Log.debug(materialListProxy.getName());
		goTo(new RoomMaterialsDetailsPlace(materialListProxy.stableId(),
				Operation.DETAILS));
	}

	private void setTable(CellTable<MaterialListProxy> table) {
		//cell table changes
		/*this.table = table;*/
		this.table = (AdvanceCellTable<MaterialListProxy>)table;

	}

	@SuppressWarnings("deprecation")
	private void initSearch() {
		requestAdvSeaCritStd = requests.materialListRequest();

		Range range = table.getVisibleRange();

		requestAdvSeaCritStd.countMaterialListByName(quickSearchTerm,
				searchThrough).fire(new RoomMaterialCountReceiver());

	}

	private class RoomMaterialCountReceiver extends Receiver<Long> {
		@Override
		public void onSuccess(Long response) {
			if (view == null) {
				// This activity is dead
				return;
			}
			Log.debug("Geholte Patienten aus der Datenbank: " + response);
			view.getTable().setRowCount(response.intValue(), true);
			onRangeChanged();
		}
	}

	@SuppressWarnings({ "deprecation" })
	protected void onRangeChanged() {
		// TODO: some bug about request
		requestAdvSeaCritStd = requests.materialListRequest();

		for (AdvancedSearchCriteriaProxy criterion : searchCriteria) {
			Log.info("Criterion: " + criterion.getField().toString() + ": "
					+ criterion.getValue());
		}

		Range range = table.getVisibleRange();
		Log.debug(range.getStart() + ": start : length " + range.getLength());

		requestAdvSeaCritStd.findUsedMaterialByName(sortname, sortorder,
				quickSearchTerm, searchThrough, range.getStart(),
				range.getLength()).fire(new MaterialListReceiver());
		// By SPEC]End
	}

	/**
	 * go to another place
	 * 
	 * @param place
	 *            the place to go to
	 */
	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
	}

	@Override
	public void showSubviewClicked() {
		goTo(new RoomMaterialsDetailsPlace(Operation.DETAILS));
	}

	// @SPEC to handle handler to add user entered data (In TextBox) in table

	public void newClicked(String value) {
		Log.debug("Add new clicked for material List");
		// Sankit[
		MaterialListRequest materialListRequest = requests
				.materialListRequest();
		MaterialListProxy materialListProxy = materialListRequest
				.create(MaterialListProxy.class);
		/*
		 * materialListProxy.setName(value);
		 * 
		 * materialListRequest.persist().using(materialListProxy) .fire(new
		 * Receiver<Void>() {
		 * 
		 * @Override public void onSuccess(Void arg0) {
		 * System.out.println("Value saved successfully"); init(); } });
		 */
		goTo(new RoomMaterialsDetailsPlace(materialListProxy.stableId(),
				Operation.CREATE));
		// Sankit]

	}

	// @SPEC To add data in table
	private void init() {
		//System.out.println("Inside INIT()");
		init2("");
	}

	// @SPEC To add data in table
	public void init2(final String q) {

		//System.out.println("Inside INIT2()");
		fireCountMaterialRequest(q, new Receiver<Long>() {
			@Override
			public void onSuccess(Long response) {
				if (view == null) {
					// This activity is dead
					return;
				}
				Log.debug("Geholte Nationalitäten aus der Datenbank: "
						+ response);
				System.out
						.println("Arrived result of count set table size according to it");
				view.getTable().setRowCount(response.intValue(), true);

				onRangeChanged(q);
			}
		});

		rangeChangeHandler = table
				.addRangeChangeHandler(new RangeChangeEvent.Handler() {
					public void onRangeChange(RangeChangeEvent event) {
						RoomMaterialsActivity.this.onRangeChanged(q);
					}
				});
	}

	// @SPEC To add data in table
	public void fireCountMaterialRequest(String name, Receiver<Long> callback) {
		// requests.nationalityRequest().countNationalitys().fire(callback);
		//System.out.println("Finding total result count value : " + quickSearchTerm);

		requests.materialListRequest()
				.countMaterialListByName(quickSearchTerm, searchThrough)
				.fire(callback);
	}

	// @SPEC To handle table chabge lostner

	protected void onRangeChanged(String q) {
		final Range range = table.getVisibleRange();

		final Receiver<List<MaterialListProxy>> callback = new Receiver<List<MaterialListProxy>>() {

			@Override
			public void onSuccess(List<MaterialListProxy> response) {
				if (view == null) {
					return;
				}
				//System.out.println("Successfully result set in table");
				// System.out.println("Successfully result set in table count : "
				// + response.get(0).getRoleTopics().size());
				table.setRowData(range.getStart(), response);

				// finishPendingSelection();
				if (widget != null) {
					widget.setWidget(view.asWidget());
				}
			}

		};

		fireRangeRequest(q, range, callback);
	}

	private void fireRangeRequest(String name, final Range range,
			final Receiver<List<MaterialListProxy>> callback) {
		//System.out.println("Inside fire range request with value " + name);
		createRangeRequest(name, range).with(view.getPaths()).fire(callback);
		// Log.debug(((String[])view.getPaths().toArray()).toString());
	}

	protected Request<List<MaterialListProxy>> createRangeRequest(String name,
			Range range) {
		//System.out.println("Calling RoomMaterialActivity with value : " + name);

		return requests.materialListRequest().findUsedMaterialByName(
				sortname, sortorder, quickSearchTerm, searchThrough,
				range.getStart(), range.getLength());
	}

	@Override
	public void editClicked() {
		Log.info("new clicked");
		view.setDetailPanel(true);
		goTo(new RoomMaterialsDetailsPlace(Operation.CREATE));
	}

	@Override
	public void deleteClicked(MaterialListProxy materialListProxy) {

		requests.materialListRequest().remove().using(materialListProxy)
				.fire(new Receiver<Void>() {
					public void onSuccess(Void ignore) {
						Log.debug("Sucessfully deleted");
						init();
						placeController.goTo(new RoomMaterialsPlace(
								"RoomMaterialsPlace"));

					}
				});

	}

	@Override
	public void performSearch(String q) {
		quickSearchTerm = q;
		Log.debug("Search for " + q);
		// init();
		initSearch();
	}

	private class MaterialListReceiver extends
			Receiver<List<MaterialListProxy>> {
		public void onFailure(ServerFailure error) {
			Log.error(error.getMessage());
			// onStop();
		}

		public void onViolation(Set<Violation> errors) {
			Iterator<Violation> iter = errors.iterator();
			String message = "";
			while (iter.hasNext()) {
				Violation it = iter.next();
				message += "message " + it.getMessage() + "\n";
				message += "path " + it.getPath() + "\n";
				message += "class " + it.getClass() + "\n";
				message += "INV " + it.getInvalidProxy() + "\n";
				message += "OR " + it.getOriginalProxy() + "\n";
				message += "ID " + it.getProxyId() + "<br>";
			}
			Log.warn(" in Simpat -" + message);
			// onStop();
		}

		@Override
		public void onSuccess(List<MaterialListProxy> response) {
			final Range range = table.getVisibleRange();

			table.setRowData(range.getStart(), response);

		}
	}

}
