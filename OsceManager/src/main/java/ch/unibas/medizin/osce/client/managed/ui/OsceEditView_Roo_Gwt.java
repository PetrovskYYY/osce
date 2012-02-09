// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.activity.OsceEditActivityWrapper;
import ch.unibas.medizin.osce.client.managed.activity.OsceEditActivityWrapper.View;
import ch.unibas.medizin.osce.client.managed.request.CourseProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.StudentProxy;
import ch.unibas.medizin.osce.client.managed.request.TaskProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyEditView;
import ch.unibas.medizin.osce.client.scaffold.ui.*;
import ch.unibas.medizin.osce.shared.StudyYears;
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

public abstract class OsceEditView_Roo_Gwt extends Composite implements View<OsceEditView> {

    @UiField(provided = true)
    ValueListBox<StudyYears> studyYear = new ValueListBox<StudyYears>(new AbstractRenderer<ch.unibas.medizin.osce.shared.StudyYears>() {

        public String render(ch.unibas.medizin.osce.shared.StudyYears obj) {
            return obj == null ? "" : String.valueOf(obj);
        }
    });

    @UiField
    IntegerBox maxNumberStudents;

    @UiField
    IntegerBox numberPosts;

    @UiField
    IntegerBox numberCourses;

    @UiField
    IntegerBox postLength;

    @UiField(provided = true)
    CheckBox isRepeOsce = new CheckBox() {

        public void setValue(Boolean value) {
            super.setValue(value == null ? Boolean.FALSE : value);
        }
    };

    @UiField
    IntegerBox numberRooms;

    @UiField(provided = true)
    CheckBox isValid = new CheckBox() {

        public void setValue(Boolean value) {
            super.setValue(value == null ? Boolean.FALSE : value);
        }
    };

    @UiField(provided = true)
    ValueListBox<SemesterProxy> semester = new ValueListBox<SemesterProxy>(ch.unibas.medizin.osce.client.managed.ui.SemesterProxyRenderer.instance(), new com.google.gwt.requestfactory.ui.client.EntityProxyKeyProvider<ch.unibas.medizin.osce.client.managed.request.SemesterProxy>());

    @UiField
    OsceDaySetEditor osce_days;

    @UiField
    CourseSetEditor courses;

    @UiField
    StudentSetEditor students;

    @UiField
    TaskSetEditor tasks;

    public void setStudentsPickerValues(Collection<StudentProxy> values) {
        students.setAcceptableValues(values);
    }

    public void setOsce_daysPickerValues(Collection<OsceDayProxy> values) {
        osce_days.setAcceptableValues(values);
    }

    public void setSemesterPickerValues(Collection<SemesterProxy> values) {
        semester.setAcceptableValues(values);
    }

    public void setTasksPickerValues(Collection<TaskProxy> values) {
        tasks.setAcceptableValues(values);
    }

    public void setCoursesPickerValues(Collection<CourseProxy> values) {
        courses.setAcceptableValues(values);
    }

    public void setStudyYearPickerValues(Collection<StudyYears> values) {
        studyYear.setAcceptableValues(values);
    }
}
