package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.Map;

import ch.unibas.medizin.osce.client.managed.request.SpokenLanguageProxy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * @author dk
 *
 */
public interface SpokenLanguageView  extends IsWidget{
	
    public interface Presenter {
        void goTo(Place place);
    }
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {
		void newClicked(String name);
		void deleteClicked(SpokenLanguageProxy lang);
		void performSearch();
		void updateClicked(SpokenLanguageProxy proxy, String value);
	}

    CellTable<SpokenLanguageProxy> getTable();
    String[] getPaths();
    
    void setDelegate(Delegate delegate);
    
//	SimplePanel getDetailsPanel();
    void setPresenter(Presenter systemStartActivity);
    String getSearchTerm();

    // Highlight onViolation
    SpokenLanguageViewImpl getSpokenLanguageView();
    EditPopView getEditPopView();
    Map getLanguageMap();
    Map getNewLanguageMap();
 // E Highlight onViolation
	
    
    
}
