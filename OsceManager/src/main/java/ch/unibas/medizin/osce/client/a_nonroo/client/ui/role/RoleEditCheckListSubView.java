package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
//file by spec
public interface RoleEditCheckListSubView extends IsWidget {
    void setDelegate(Delegate delegate);

    public interface Presenter {
        void goTo(Place place);
    }
    
	interface Delegate {
		
	}

	
}
