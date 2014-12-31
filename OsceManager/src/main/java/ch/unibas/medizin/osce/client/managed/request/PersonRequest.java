// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;

@ServiceName("ch.unibas.medizin.osce.domain.Person")
public interface PersonRequest extends RequestContext {

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.PersonProxy, java.lang.Void> persist();

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.PersonProxy, java.lang.Void> remove();

    abstract Request<java.lang.Long> countPeople();

    abstract Request<ch.unibas.medizin.osce.client.managed.request.PersonProxy> findPerson(Long id);

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.PersonProxy>> findAllPeople();

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.PersonProxy>> findPersonEntries(int firstResult, int maxResults);
}
