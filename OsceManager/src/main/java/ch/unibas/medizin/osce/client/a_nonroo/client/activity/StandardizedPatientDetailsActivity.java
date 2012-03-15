package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.Iterator;
import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.StandardizedPatientDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StandardizedPatientPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientAnamnesisSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientDetailsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientDetailsViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientScarSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientLangSkillSubView;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisChecksValueProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisFormProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisFormRequest;
import ch.unibas.medizin.osce.client.managed.request.LangSkillProxy;
import ch.unibas.medizin.osce.client.managed.request.LangSkillRequest;
import ch.unibas.medizin.osce.client.managed.request.ScarProxy;
import ch.unibas.medizin.osce.client.managed.request.SpokenLanguageProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.style.widgets.ProxySuggestOracle;
import ch.unibas.medizin.osce.shared.LangSkillLevel;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.ServerFailure;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class StandardizedPatientDetailsActivity extends AbstractActivity implements
StandardizedPatientDetailsView.Presenter, 
StandardizedPatientDetailsView.Delegate, 
StandardizedPatientScarSubView.Delegate,
StandardizedPatientAnamnesisSubView.Delegate,
StandardizedPatientLangSkillSubView.Delegate {
	
    private OsMaRequestFactory requestFactory;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private StandardizedPatientDetailsView view;
	// FIXME what could this be used for?
	private SingleSelectionModel<StandardizedPatientProxy> selectionModel;
	private HandlerRegistration rangeChangeHandler;

	private StandardizedPatientDetailsPlace place;
	private StandardizedPatientProxy standardizedPatientProxy;

	private StandardizedPatientScarSubView standardizedPatientScarSubView;
	private CellTable<ScarProxy> scarTable;
	private ValueListBox<ScarProxy> scarBox;
	private HandlerRegistration rangeChangeHandlerScars;
	
	private StandardizedPatientAnamnesisSubView standardizedPatientAnamnesisSubView;
	private CellTable<AnamnesisChecksValueProxy> anamnesisTable;
	
	private StandardizedPatientLangSkillSubView standardizedPatientLangSkillSubView;
	private CellTable<LangSkillProxy> langSkillTable;

	private AnamnesisFormProxy anamnesisForm ;

	public StandardizedPatientDetailsActivity(StandardizedPatientDetailsPlace place, OsMaRequestFactory requests, PlaceController placeController) {
		this.place = place;
    	this.requestFactory = requests;
    	this.placeController = placeController;
    }

	public void onStop(){

	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("StandardizedPatientDetailsActivity.start()");
		StandardizedPatientDetailsView standardizedPatientDetailsView = new StandardizedPatientDetailsViewImpl();
		standardizedPatientDetailsView.setPresenter(this);
		this.widget = panel;
		this.view = standardizedPatientDetailsView;
		standardizedPatientScarSubView = view.getStandardizedPatientScarSubViewImpl();
		standardizedPatientAnamnesisSubView = view.getStandardizedPatientAnamnesisSubViewImpl();
		standardizedPatientLangSkillSubView = view.getStandardizedPatientLangSkillSubViewImpl();
		
		widget.setWidget(standardizedPatientDetailsView.asWidget());
		
		view.setDelegate(this);
		standardizedPatientScarSubView.setDelegate(this);
		standardizedPatientAnamnesisSubView.setDelegate(this);
		standardizedPatientLangSkillSubView.setDelegate(this);
		
		requestFactory.find(place.getProxyId()).with("profession", "descriptions", "nationality", "bankAccount", "langskills", "anamnesisForm", "anamnesisForm.scars").fire(new InitializeActivityReceiver());
	}
	
	/**
	 * Used as a callback for the request that gets the @StandardizedPatientProxy
	 * that is edited in this activities instance.
	 */
	@SuppressWarnings("deprecation")
	private class InitializeActivityReceiver extends Receiver<Object> {
		@Override
		public void onFailure(ServerFailure error){
			Log.error(error.getMessage());
		}
		
		@Override
		public void onSuccess(Object response) {
			if(response instanceof StandardizedPatientProxy){
				Log.info(((StandardizedPatientProxy) response).getName());
				standardizedPatientProxy = (StandardizedPatientProxy) response;
				init();
			}
		}
	}
	
	/**
	 * Callback for filling the language picker with all available @SpokenLanguageProxy
	 * elements.
	 */
	@SuppressWarnings("deprecation")
	private class SpokenLanguageReceiver extends Receiver<List<SpokenLanguageProxy>> {
		@Override
		public void onSuccess(List<SpokenLanguageProxy> response) {
			Log.debug("Geholte Sprachen aus der Datenbank: " + response.size());
			standardizedPatientLangSkillSubView.setLanguagePickerValues(response);
		}
	}
	
	/**
	 * Callback for 
	 */
	@SuppressWarnings("deprecation")
	private class LangSkillCountReceiver extends Receiver<Long> {
		@Override
		public void onSuccess(Long count) {
			if (view == null) {
				return;
			}
			
			Log.debug(count.toString() + " language skills to load");
			langSkillTable.setRowCount(count.intValue(), true);
			onRangeChangedLanguageSkillTable();
		}
		
	}
	
	@SuppressWarnings("deprecation")
	private class LangSkillReceiver extends  Receiver<List<LangSkillProxy>> {
		private Range _range;
		
		public LangSkillReceiver(Range range) {
			super();
			_range = range;
		}
		
		@Override
		public void onSuccess(List<LangSkillProxy> values) {
			if (view == null) {
				return;
			}
			langSkillTable.setRowData(_range.getStart(), values);
		}
	}
	
	@SuppressWarnings("deprecation")
	private class LangSkillUpdateReceiver extends Receiver<Void> {
		@Override
		public void onSuccess(Void response) {
			Log.debug("Langskills updated successfully");
			initLangSkills();
		}
	}
	
	@SuppressWarnings("deprecation")
	private class AnamnesisChecksValueCountReceiver extends Receiver<Long> {
		@Override
		public void onSuccess(Long count) {
			if (view == null) {
				return;
			}

			Log.debug(count.toString() + " scars loaded");
			anamnesisTable.setRowCount(count.intValue(), true);

			onRangeChangedAnamnesisTable();
		}
	}
	
	@SuppressWarnings("deprecation")
	private class AnamnesisChecksValueUpdateReceiver extends Receiver<Void> {

		@Override
		public void onSuccess(Void response) {
			Log.info("AnamnesisChecksValue updated");
			initAnamnesis();
		}
		
	}
	
	@SuppressWarnings("deprecation")
	private class ScarBoxReceiver extends Receiver<List<ScarProxy>> {
		@Override
		public void onSuccess(List<ScarProxy> response) {
			standardizedPatientScarSubView.setScarBoxValues(response);
		}
	}
	
	@SuppressWarnings("deprecation")
	private class ScarCountReceiver extends Receiver<Long> {
		@Override
		public void onSuccess(Long count) {
			if (view == null) {
				// This activity is dead
				return;
			}
			
			Log.debug(count.toString() + " scars loaded");
			scarTable.setRowCount(count.intValue(), true);
			
			onRangeChangedScarTable();
		}
	}
	
	@SuppressWarnings("deprecation")
	private class ScarReceiver extends Receiver<List<ScarProxy>> {
		private Range _range;
		public ScarReceiver(Range range) {
			super();
			_range = range;
		}
		
		@Override
		public void onSuccess(List<ScarProxy> values) {
			if (view == null) {
				// This activity is dead
				return;
			}
			scarTable.setRowData(_range.getStart(), values);

			// finishPendingSelection();
			if (widget != null) {
				widget.setWidget(view.asWidget());
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	private class ScarUpdateReceiver extends Receiver<Void>{
		@Override
		public void onSuccess(Void arg0) {
			Log.debug("scar updated...");
			initScar();
		}
	}

	private void init() {
		view.setValue(standardizedPatientProxy);
		anamnesisForm =  standardizedPatientProxy.getAnamnesisForm();
		initScar();
		initAnamnesis();
		initLangSkills();
	}
	
	/*******************
	 * LANGSKILL TABLE
	 ******************/
	
	/**
	 * Loads languages, which the standardizedPatient does not speak and fills them into ValueListBoxe of the view.
	 * Requests the number of languages spoken by the given patient and then calls onRangeChangedLanguageSkillTable() 
	 * to fill the table.
	 */
	@SuppressWarnings("deprecation")
	protected void initLangSkills() {
		// FIXME maybe it should be discerned between stuff that has to be done once
		// and stuff that has to be done multiple times? (handler, the class variable assignment vs. filling in values)
		this.langSkillTable = standardizedPatientLangSkillSubView.getLangSkillTable();
		
		// Fill ValueListBoxes
		requestFactory.languageRequestNonRoo().findLanguagesByNotStandardizedPatient(standardizedPatientProxy.getId()).fire(new SpokenLanguageReceiver());
		
		// Request number of Languages spoken by patient and call onRangeChangedLanguageSkillTable() to fill table
		requestFactory.langSkillRequestNonRoo().countLangSkillsByPatientId(standardizedPatientProxy.getId()).fire(new LangSkillCountReceiver());
		
		// FIXME: this should not be added everytime initLangSkills() is called, right?
		langSkillTable.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			@Override
			public void onRangeChange(RangeChangeEvent event) {
				Log.info("onRangeChangedLanguageSkillTable()");
				onRangeChangedLanguageSkillTable();
			}
		});
	}
	
	/**
	 * Fills the Language Skill table with data from the DB-Request, by
	 * firing the database request and providing a callback method.
	 */
	protected void onRangeChangedLanguageSkillTable() {
		final Range range = langSkillTable.getVisibleRange();
		fireLangSkillRangeRequest(range, new LangSkillReceiver(range));
	}
	
	/**
	 * Fire database request for Language Skills
	 * @param range defines which elements to fetch ("from element x on, take n elements...");
	 * @param callback Method to call after the request is executed.
	 */
	@SuppressWarnings("deprecation")
	private void fireLangSkillRangeRequest(final Range range, final Receiver<List<LangSkillProxy>> callback) {
		requestFactory.langSkillRequestNonRoo().findLangSkillsByPatientId(standardizedPatientProxy.getId(), range.getStart(), range.getLength()).with("spokenlanguage").fire(callback);
	}
	
	/*******************
	 * ANAMNESIS TABLE
	 ******************/

	protected void initAnamnesis() {
		this.anamnesisTable = standardizedPatientAnamnesisSubView.getTable();
		
		// TODO Implementation mit Checkboxes korrekt machen
		if (standardizedPatientAnamnesisSubView.areUnansweredQuestionsShown()) {
			// TODO make this request conditional (only execute if explicitly requested)
			// fills the AnamnesisChecksValue table in the database with NULL-values for unanswered questions
			requestFactory.anamnesisChecksValueRequestNonRoo().fillAnamnesisChecksValues(anamnesisForm.getId()).fire(new Receiver<Void>() {
				@Override
				public void onSuccess(Void response) {
					Log.debug("Updated AnamnesisChecksValue.");
				}
				
			});
		}
		
		// requests the number of rows in AnamnesisChecksValue for the current patient
		requestFactory.anamnesisChecksValueRequestNonRoo().countAnamnesisChecksValuesByAnamnesisForm(anamnesisForm.getId()).fire(new AnamnesisChecksValueCountReceiver());
	}

	protected void onRangeChangedAnamnesisTable() {
		final Range range = anamnesisTable.getVisibleRange();
		
		fireAnamnesisChecksValueRangeRequest(range, new Receiver<List<AnamnesisChecksValueProxy>>() {
			@Override
			public void onSuccess(List<AnamnesisChecksValueProxy> values) {
				if (view == null) {
					return;
				}
				// FIXME: ORacle should be filled with all anamnesisCheckProxyValues... and not be limited by range...
				((ProxySuggestOracle<AnamnesisChecksValueProxy>) standardizedPatientAnamnesisSubView.getAnamnesisQuestionSuggestBox().getSuggestOracle()).addAll(values);
				anamnesisTable.setRowData(range.getStart(), values);
			}
		});
	}

	/**
	 * Executes the request for filling the AnamnesisCheckProxy-table in the AnamnesisSubView.
	 * @param range 
	 * @param receiver Callback (which should fill the table)
	 */
	@SuppressWarnings("deprecation")
	private void fireAnamnesisChecksValueRangeRequest(Range range, Receiver<List<AnamnesisChecksValueProxy>> receiver) {
		String[] paths = standardizedPatientAnamnesisSubView.getPaths();
		Long anamnesisId = anamnesisForm.getId();
		
		requestFactory.anamnesisChecksValueRequestNonRoo().findAnamnesisChecksValuesByAnamnesisForm(anamnesisId, range.getStart(), range.getLength())
				.with(paths).fire(receiver);
	}
	
	/*******************
	 * SCAR TABLE
	 ******************/

	/**
	 * Fills the ValueListBox and Table of the ScarSubView
	 */
	protected void initScar() {
		// FIXME maybe it should be discerned between stuff that has to be done once
		// and stuff that has to be done multiple times? (handler, the class variable assignment vs. filling in values)
		this.scarTable = standardizedPatientScarSubView.getTable();
		this.scarBox = standardizedPatientScarSubView.getScarBox();
		
		// Finds all scars, that can still be added to the patient (i.e. the patient doesn't have them yet) 
		// and fills the corresponding ValueListBox
		requestFactory.scarRequestNonRoo().findScarEntriesByNotAnamnesisForm(anamnesisForm.getId()).fire(new ScarBoxReceiver());
		// Request number of scars the patient has and then fill the table by calling onRangeChangedScarTable()
		requestFactory.scarRequestNonRoo().countScarsByAnamnesisForm(anamnesisForm.getId()).fire(new ScarCountReceiver());
		
		// FIXME: should probably only be called once?
		scarTable.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			@Override
			public void onRangeChange(RangeChangeEvent event) {
				Log.info("onRangeChangedScarTable()");
				onRangeChangedScarTable();
			}
		});
	}
	
	/**
	 * Fills the scar table of the ScarSubView with values from the database.
	 * Fires a database request and defines the callback-class for filling the
	 * database.
	 */
	protected void onRangeChangedScarTable() {
		final Range range = scarTable.getVisibleRange();
		fireScarRangeRequest(range, new ScarReceiver(range));
	}
	
	/**
	 * fires a request on the database to load the scars 
	 * @param range defines which elements to fetch ("from element x on, take n elements...");
	 * @param callback Class that handles the possible callbacks
	 */
	@SuppressWarnings("deprecation")
	private void fireScarRangeRequest(final Range range, final Receiver<List<ScarProxy>> callback) {
		createScarRangeRequest(range).with(standardizedPatientScarSubView.getPaths()).fire(callback);
	}
	
	/**
	 * 
	 * @param range
	 * @return
	 */
	@SuppressWarnings("deprecation")
	protected Request<List<ScarProxy>> createScarRangeRequest(Range range) {
		return requestFactory.scarRequestNonRoo().findScarEntriesByAnamnesisForm(anamnesisForm.getId(), range.getStart(), range.getLength());
	}
	
	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
		
	}

	@Override
	public void editPatientClicked() {
		Log.info("edit clicked");
		goTo(new StandardizedPatientDetailsPlace(standardizedPatientProxy.stableId(),
				StandardizedPatientDetailsPlace.Operation.EDIT));
	}

	@Override
	public void deletePatientClicked() {
		if (!Window.confirm("Really delete this entry? You cannot undo this change.")) {
            return;
        }
		
        requestFactory.standardizedPatientRequest().remove().using(standardizedPatientProxy).fire(new Receiver<Void>() {

            public void onSuccess(Void ignore) {
                if (widget == null) {
                    return;
                }
            	placeController.goTo(new StandardizedPatientPlace("StandardizedPatientPlace!DELETED"));
            }
        });
		
	}

	@Override
	@SuppressWarnings("deprecation")
	public void deleteLangSkillClicked(LangSkillProxy langSkill) {
		requestFactory.langSkillRequest().remove().using(langSkill).fire(new LangSkillUpdateReceiver());
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void deleteScarClicked(ScarProxy scar) {
		AnamnesisFormRequest anamReq = requestFactory.anamnesisFormRequest();
		anamnesisForm =  anamReq.edit(anamnesisForm);
		
		Log.debug("Remove scar (" + scar.getId() + ") from anamnesis-form (" + anamnesisForm.getId() + ")");
		
		Iterator<ScarProxy> i = anamnesisForm.getScars().iterator();
		while (i.hasNext()) {
			ScarProxy scarProxy = (ScarProxy) i.next();
			//Log.warn(scarProxy.stableId() + " ");
			//Log.warn(scar.stableId() + " ");
			if (scarProxy.getId() == scar.getId() ) {
				anamnesisForm.getScars().remove(scarProxy);
				break;
			}
		}
//		anamnesisForm.getScars().remove(scar);
		
		anamReq.persist().using(anamnesisForm).fire(new ScarUpdateReceiver());
	}

	@Override
	@SuppressWarnings("deprecation")
	public void addScarClicked() {
		AnamnesisFormRequest anamReq = requestFactory.anamnesisFormRequest();
		
		ScarProxy scar = scarBox.getValue();
		Log.debug("Add scar (" + scar.getBodypart() + " - id " + scar.getId() + ") to anamnesis-form (" + anamnesisForm.getId() + ")");
		
		anamnesisForm = anamReq.edit(anamnesisForm);
		
		anamnesisForm.getScars().add(scar);
		
		anamReq.persist().using(anamnesisForm).fire(new ScarUpdateReceiver());
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void addLangSkillClicked(SpokenLanguageProxy spokenLanguageProxy, LangSkillLevel langSkillLevel) {
		// get requestContext and initialize new langSkillProxy
		LangSkillRequest langSkillRequest = requestFactory.langSkillRequest();
		LangSkillProxy langSkillProxy = langSkillRequest.create(LangSkillProxy.class);
		langSkillProxy.setSkill(langSkillLevel);
		langSkillProxy.setSpokenlanguage(spokenLanguageProxy);
		langSkillProxy.setStandardizedpatient(standardizedPatientProxy);
		
		Log.debug("add skill " + langSkillLevel.toString() + " in language " + spokenLanguageProxy.getLanguageName());
		
		// write new langSkill to database and re-initialize table
		langSkillRequest.persist().using(langSkillProxy).fire(new LangSkillUpdateReceiver());
	}

	@Override
	public void searchAnamnesisQuestion(AnamnesisChecksValueProxy proxy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void searchAnamnesisQuestion(String needle) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveAnamnesisChecksValueProxyChanges(AnamnesisChecksValueProxy proxy, String anamnesisChecksValue, Boolean truth) {
		// TODO Auto-generated method stub
		Log.info("anamnesisChecksValue: " + anamnesisChecksValue);
		if (truth == null) {
			Log.info("truth: null");
		} else {
			Log.info("truth: " + truth.toString());
		}
		
		anamnesisForm = requestFactory.anamnesisFormRequest().edit(anamnesisForm);
		// FIXME: this is stupid because of toooooooo many elements to iterate over...
		// TODO: what about comment?
		// TODO: what about empty checkbox selections?
		Iterator<AnamnesisChecksValueProxy> i = anamnesisForm.getAnamnesischecksvalues().iterator();
		while (i.hasNext()) {
			AnamnesisChecksValueProxy iteratedProxy = i.next();
			if (iteratedProxy.equals(proxy)) {
				iteratedProxy.setAnamnesisChecksValue(anamnesisChecksValue);
				iteratedProxy.setTruth(truth);
			}
		}
		
		requestFactory.anamnesisFormRequest().persist().using(anamnesisForm).fire(new AnamnesisChecksValueUpdateReceiver());
		
		
		// FIXME: How to go about this Attempting to edit an EntityProxy previously edited by another RequestContext issue?
		
//		anamnesisChecksValueRequest.persist().using(proxy).fire(new Receiver<Void>() {
//
//			@Override
//			public void onSuccess(Void response) {
//				initAnamnesis();
//			}
//			
//		});
	}
}
