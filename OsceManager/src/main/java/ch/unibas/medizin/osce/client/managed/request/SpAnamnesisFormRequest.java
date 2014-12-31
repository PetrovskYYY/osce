// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;

@ServiceName("ch.unibas.medizin.osce.domain.spportal.SpAnamnesisForm")
public interface SpAnamnesisFormRequest extends RequestContext {

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.SpAnamnesisFormProxy, java.lang.Void> persist();

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.SpAnamnesisFormProxy, java.lang.Void> remove();

    abstract Request<java.lang.Long> countSpAnamnesisForms();

    abstract Request<ch.unibas.medizin.osce.client.managed.request.SpAnamnesisFormProxy> findSpAnamnesisForm(Long id);

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.SpAnamnesisFormProxy>> findAllSpAnamnesisForms();

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.SpAnamnesisFormProxy>> findSpAnamnesisFormEntries(int firstResult, int maxResults);
}
