// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;

@ServiceName("ch.unibas.medizin.osce.domain.RoleTableItem")
public interface RoleTableItemRequest extends RequestContext {

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.RoleTableItemProxy, java.lang.Void> persist();

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.RoleTableItemProxy, java.lang.Void> remove();

    abstract Request<java.lang.Long> countRoleTableItems();

    abstract Request<ch.unibas.medizin.osce.client.managed.request.RoleTableItemProxy> findRoleTableItem(Long id);

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.RoleTableItemProxy>> findAllRoleTableItems();

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.RoleTableItemProxy>> findRoleTableItemEntries(int firstResult, int maxResults);

    abstract Request<List<RoleTableItemProxy>> findRoleTableItemByBaseItemId(Long id);
	abstract InstanceRequest<RoleTableItemProxy, Void> roleTableItemMoveUp(Long id);
	abstract InstanceRequest<RoleTableItemProxy, Void> roleTableItemMoveDown(Long id);
}
