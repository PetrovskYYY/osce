// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;

@ServiceName("ch.unibas.medizin.osce.domain.spportal.SpNationality")
public interface SpNationalityRequest extends RequestContext {

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.SpNationalityProxy, java.lang.Void> persist();

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.SpNationalityProxy, java.lang.Void> remove();

    abstract Request<java.lang.Long> countSpNationalitys();

    abstract Request<ch.unibas.medizin.osce.client.managed.request.SpNationalityProxy> findSpNationality(Long id);

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.SpNationalityProxy>> findAllSpNationalitys();

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.SpNationalityProxy>> findSpNationalityEntries(int firstResult, int maxResults);
}
