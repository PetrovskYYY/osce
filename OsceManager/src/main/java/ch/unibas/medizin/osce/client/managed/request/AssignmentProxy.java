// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import ch.unibas.medizin.osce.shared.AssignmentTypes;
import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;
import java.util.Date;

@ProxyForName("ch.unibas.medizin.osce.domain.Assignment")
public interface AssignmentProxy extends EntityProxy {

    abstract Long getId();

    abstract void setId(Long id);

    abstract Integer getVersion();

    abstract void setVersion(Integer version);

    abstract AssignmentTypes getType();

    abstract void setType(AssignmentTypes type);

    abstract Date getTimeStart();

    abstract void setTimeStart(Date timeStart);

    abstract Date getTimeEnd();

    abstract void setTimeEnd(Date timeEnd);

    abstract OsceDayProxy getOsceDay();

    abstract void setOsceDay(OsceDayProxy osceDay);

    abstract OscePostRoomProxy getOscePostRoom();

    abstract void setOscePostRoom(OscePostRoomProxy oscePostRoom);

    abstract StudentOscesProxy getOsceStudent();

    abstract void setOsceStudent(StudentOscesProxy osceStudent);

    abstract StudentProxy getStudent();

    abstract void setStudent(StudentProxy student);

    abstract PatientInRoleProxy getPatientInRole();

    abstract void setPatientInRole(PatientInRoleProxy patientInRole);

    abstract DoctorProxy getExaminer();

    abstract void setExaminer(DoctorProxy examiner);

    abstract Integer getSequenceNumber();

    abstract void setSequenceNumber(Integer sequenceNumber);

    abstract Integer getRotationNumber();

    abstract void setRotationNumber(Integer rotationNumber);
}
