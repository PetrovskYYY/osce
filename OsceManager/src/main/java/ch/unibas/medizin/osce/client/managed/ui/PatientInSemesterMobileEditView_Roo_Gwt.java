// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.activity.PatientInSemesterEditActivityWrapper;
import ch.unibas.medizin.osce.client.managed.activity.PatientInSemesterEditActivityWrapper.View;
import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.PatientInRoleProxy;
import ch.unibas.medizin.osce.client.managed.request.PatientInSemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.managed.request.TrainingProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyEditView;
import ch.unibas.medizin.osce.client.scaffold.ui.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.requestfactory.client.RequestFactoryEditorDriver;
import com.google.gwt.requestfactory.shared.RequestFactory;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DoubleBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.LongBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.datepicker.client.DateBox;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public abstract class PatientInSemesterMobileEditView_Roo_Gwt extends Composite implements View<PatientInSemesterMobileEditView> {

    @UiField(provided = true)
    ValueListBox<SemesterProxy> semester = new ValueListBox<SemesterProxy>(ch.unibas.medizin.osce.client.managed.ui.SemesterProxyRenderer.instance(), new com.google.gwt.requestfactory.ui.client.EntityProxyKeyProvider<ch.unibas.medizin.osce.client.managed.request.SemesterProxy>());

    @UiField(provided = true)
    ValueListBox<StandardizedPatientProxy> standardizedPatient = new ValueListBox<StandardizedPatientProxy>(ch.unibas.medizin.osce.client.managed.ui.StandardizedPatientProxyRenderer.instance(), new com.google.gwt.requestfactory.ui.client.EntityProxyKeyProvider<ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy>());

    @UiField(provided = true)
    CheckBox accepted = new CheckBox() {

        public void setValue(Boolean value) {
            super.setValue(value == null ? Boolean.FALSE : value);
        }
    };

    @UiField
    OsceDaySetEditor osceDays;

    @UiField
    PatientInRoleSetEditor patientInRole;

    @UiField
    TrainingSetEditor trainings;

    public void setPatientInRolePickerValues(Collection<PatientInRoleProxy> values) {
        patientInRole.setAcceptableValues(values);
    }

    public void setSemesterPickerValues(Collection<SemesterProxy> values) {
        semester.setAcceptableValues(values);
    }

    public void setTrainingsPickerValues(Collection<TrainingProxy> values) {
        trainings.setAcceptableValues(values);
    }

    public void setStandardizedPatientPickerValues(Collection<StandardizedPatientProxy> values) {
        standardizedPatient.setAcceptableValues(values);
    }

    public void setOsceDaysPickerValues(Collection<OsceDayProxy> values) {
        osceDays.setAcceptableValues(values);
    }
}
