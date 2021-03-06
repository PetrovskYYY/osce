package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.List;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

/**
 * @author dk
 *
 */
public interface SummoningsPopupView extends IsWidget{
	
    public interface Presenter {
        void goTo(Place place);
    }
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {
	}

    String[] getPaths();
    
    void setDelegate(Delegate delegate);
    
    void setPresenter(Presenter systemStartActivity);

	Button getSendMailButton();

	String getMessageContent();

	void setMessageContent(String html);

	Button getSaveTemplateButton();

	Button getRestoreTemplateButton();

	/*ListBox getSemesterList();*/

	Button getLoadTemplateButton();

	void setFileListValues(List<String> fileList);

	TextBox getSubject();

	String getSelectedTemplateFile();

	Label getSubjectLbl();
	
	public Label getSendCopyLbl();
	
	public TextBox getSendCopy();
	
	public TextBox getEmailFrom();
	
	public Label getEmailFromLbl();
}
