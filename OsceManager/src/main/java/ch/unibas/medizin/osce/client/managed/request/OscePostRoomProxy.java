// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;
import java.util.Set;

@ProxyForName("ch.unibas.medizin.osce.domain.OscePostRoom")
public interface OscePostRoomProxy extends EntityProxy {

    abstract Long getId();

    abstract void setId(Long id);

    abstract Integer getVersion();

    abstract void setVersion(Integer version);

    abstract RoomProxy getRoom();

    abstract void setRoom(RoomProxy room);

    abstract OscePostProxy getOscePost();

    abstract void setOscePost(OscePostProxy oscePost);

    abstract CourseProxy getCourse();

    abstract void setCourse(CourseProxy course);

    abstract Set<ch.unibas.medizin.osce.client.managed.request.AssignmentProxy> getAssignments();

    abstract void setAssignments(Set<AssignmentProxy> assignments);
}