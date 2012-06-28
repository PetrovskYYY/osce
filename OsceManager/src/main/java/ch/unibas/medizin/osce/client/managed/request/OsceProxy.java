// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import ch.unibas.medizin.osce.shared.OsceStatus;
import ch.unibas.medizin.osce.shared.StudyYears;
import com.google.gwt.requestfactory.shared.EntityProxy;
import com.google.gwt.requestfactory.shared.ProxyForName;
import java.util.List;
import java.util.Set;
import org.springframework.roo.addon.gwt.RooGwtMirroredFrom;

@RooGwtMirroredFrom("ch.unibas.medizin.osce.domain.Osce")
@ProxyForName("ch.unibas.medizin.osce.domain.Osce")
public interface OsceProxy extends EntityProxy {

    abstract Long getId();

    abstract void setId(Long id);

    abstract Integer getVersion();

    abstract void setVersion(Integer version);

    abstract StudyYears getStudyYear();

    abstract void setStudyYear(StudyYears studyYear);

    abstract Integer getMaxNumberStudents();

    abstract void setMaxNumberStudents(Integer maxNumberStudents);

    abstract String getName();

    abstract void setName(String name);

    abstract Short getShortBreak();

    abstract void setShortBreak(Short shortBreak);

    abstract Short getShortBreakSimpatChange();

    abstract void setShortBreakSimpatChange(Short shortBreakSimpatChange);

    abstract Short getLongBreak();

    abstract void setLongBreak(Short longBreak);

    abstract Short getLunchBreak();

    abstract void setLunchBreak(Short lunchBreak);

    abstract Short getMiddleBreak();

    abstract void setMiddleBreak(Short middleBreak);

    abstract Integer getNumberPosts();

    abstract void setNumberPosts(Integer numberPosts);

    abstract Integer getNumberCourses();

    abstract void setNumberCourses(Integer numberCourses);

    abstract Integer getPostLength();

    abstract void setPostLength(Integer postLength);

    abstract Boolean getIsRepeOsce();

    abstract void setIsRepeOsce(Boolean isRepeOsce);

    abstract Integer getNumberRooms();

    abstract void setNumberRooms(Integer numberRooms);

    abstract Boolean getIsValid();

    abstract void setIsValid(Boolean isValid);

    abstract OsceStatus getOsceStatus();

    abstract void setOsceStatus(OsceStatus osceStatus);

    abstract SemesterProxy getSemester();

    abstract void setSemester(SemesterProxy semester);

    abstract Set<ch.unibas.medizin.osce.client.managed.request.OsceDayProxy> getOsce_days();

    abstract void setOsce_days(Set<OsceDayProxy> osce_days);

    abstract List<ch.unibas.medizin.osce.client.managed.request.OscePostBlueprintProxy> getOscePostBlueprints();

    abstract void setOscePostBlueprints(List<OscePostBlueprintProxy> oscePostBlueprints);

    abstract Set<ch.unibas.medizin.osce.client.managed.request.TaskProxy> getTasks();

    abstract void setTasks(Set<TaskProxy> tasks);

    abstract Set<ch.unibas.medizin.osce.client.managed.request.StudentOscesProxy> getOsceStudents();

    abstract void setOsceStudents(Set<StudentOscesProxy> osceStudents);

    abstract ch.unibas.medizin.osce.client.managed.request.OsceProxy getCopiedOsce();

    abstract void setCopiedOsce(ch.unibas.medizin.osce.client.managed.request.OsceProxy copiedOsce);
}
