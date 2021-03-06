package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;


import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

public interface CheckListTopicPopupView extends IsWidget{

	interface Delegate {
		
		
	}
	
	void setDelegate(Delegate delegate);
	
	public Label getTopicLbl();
	
	public Label getDescriptionLbl();
	
	public TextBox getTopicTxtBox() ;
	
	public TextBox getDescriptionTxtBox();
	
	public Button getOkBtn();
	
	public CheckBox getIsOverallQuestionChkBox();
	
	// Issue Role
	public Button getCancelBtn();
	
	public ListBox getCriteriaCountLstBox();
	
	public TextArea getOptionDescTextArea();
}

