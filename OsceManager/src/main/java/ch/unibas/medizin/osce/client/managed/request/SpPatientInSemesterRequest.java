// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;

@ServiceName("ch.unibas.medizin.osce.domain.spportal.SpPatientInSemester")
public interface SpPatientInSemesterRequest extends RequestContext {

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.SpPatientInSemesterProxy, java.lang.Void> persist();

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.SpPatientInSemesterProxy, java.lang.Void> remove();

    abstract Request<java.lang.Long> countSpPatientInSemesters();

    abstract Request<ch.unibas.medizin.osce.client.managed.request.SpPatientInSemesterProxy> findSpPatientInSemester(Long id);

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.SpPatientInSemesterProxy>> findAllSpPatientInSemesters();

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.SpPatientInSemesterProxy>> findSpPatientInSemesterEntries(int firstResult, int maxResults);
}
