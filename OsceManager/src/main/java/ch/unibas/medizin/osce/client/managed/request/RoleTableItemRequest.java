// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import com.google.gwt.requestfactory.shared.InstanceRequest;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.ServiceName;
import org.springframework.roo.addon.gwt.RooGwtMirroredFrom;

@RooGwtMirroredFrom("ch.unibas.medizin.osce.domain.RoleTableItem")
@ServiceName("ch.unibas.medizin.osce.domain.RoleTableItem")
public interface RoleTableItemRequest extends RequestContext {

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.RoleTableItemProxy, java.lang.Void> persist();

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.RoleTableItemProxy, java.lang.Void> remove();

    abstract Request<java.lang.Long> countRoleTableItems();

    abstract Request<ch.unibas.medizin.osce.client.managed.request.RoleTableItemProxy> findRoleTableItem(Long id);

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.RoleTableItemProxy>> findAllRoleTableItems();

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.RoleTableItemProxy>> findRoleTableItemEntries(int firstResult, int maxResults);
}