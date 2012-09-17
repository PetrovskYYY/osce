package ch.unibas.medizin.osce.shared.scaffold;

import javax.persistence.TypedQuery;

import ch.unibas.medizin.osce.client.managed.request.CourseProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostRoomProxy;
import ch.unibas.medizin.osce.domain.Course;
import ch.unibas.medizin.osce.domain.OscePost;
import ch.unibas.medizin.osce.domain.OscePostRoom;

import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;

@SuppressWarnings("deprecation")
@Service(OscePostRoom.class)
public interface OscePostRoomRequestNonRoo extends RequestContext 
{	 
	abstract Request<OscePostRoomProxy> findOscePostRoomByOscePostAndCourse(CourseProxy course, OscePostProxy oscePost);
}

