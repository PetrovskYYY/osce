// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;

@ServiceName("ch.unibas.medizin.osce.domain.CheckList")
public interface CheckListRequest extends RequestContext {

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.CheckListProxy, java.lang.Void> persist();

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.CheckListProxy, java.lang.Void> remove();

    abstract Request<java.lang.Long> countCheckLists();

    abstract Request<ch.unibas.medizin.osce.client.managed.request.CheckListProxy> findCheckList(Long id);

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.CheckListProxy>> findAllCheckLists();

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.CheckListProxy>> findCheckListEntries(int firstResult, int maxResults);
}
