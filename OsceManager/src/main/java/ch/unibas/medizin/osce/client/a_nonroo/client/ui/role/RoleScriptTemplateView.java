package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;


import java.util.List;
import java.util.Map;

import ch.unibas.medizin.osce.client.managed.request.RoleTemplateProxy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;

public interface RoleScriptTemplateView extends IsWidget{
	   public interface Presenter {
	        void goTo(Place place);
	    }
		/**
		 * Implemented by the owner of the view.
		 */
		interface Delegate {
			void goToDetailClicked();
			
			void deleteClicked(RoleTemplateProxy roleTemplate);
			
			void editClicked(RoleTemplateProxy roleTemplate,int left,int top);
			
			void newClicked(String name);
			
			void performSearch(String q);
		}

		CellTable<RoleTemplateProxy> getTable();
	    //String[] getPaths();
		List<String> getPaths();
		String[] getPathsString();
	    void setDelegate(Delegate delegate);
	    
		SimplePanel getDetailsPanel();
	    void setPresenter(Presenter roleScriptTemplateActivity);
	    
	    public void setDetailPanel(boolean isDetailPlace);
	 // Violation Changes Highlight
    	public Map getAadTemplateMap();
	 // E Violation Changes Highlight
}
