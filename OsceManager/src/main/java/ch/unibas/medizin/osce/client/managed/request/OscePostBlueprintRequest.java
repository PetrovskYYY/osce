// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;

@ServiceName("ch.unibas.medizin.osce.domain.OscePostBlueprint")
public interface OscePostBlueprintRequest extends RequestContext {

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.OscePostBlueprintProxy, java.lang.Void> persist();

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.OscePostBlueprintProxy, java.lang.Void> remove();

    abstract Request<java.lang.Long> countOscePostBlueprints();

    abstract Request<ch.unibas.medizin.osce.client.managed.request.OscePostBlueprintProxy> findOscePostBlueprint(Long id);

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.OscePostBlueprintProxy>> findAllOscePostBlueprints();

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.OscePostBlueprintProxy>> findOscePostBlueprintEntries(int firstResult, int maxResults);

    abstract Request<Long> countOscebluePrintValue(Long osceid);
	
	abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.OscePostBlueprintProxy,Boolean> removeOscePostBlueprint(Long oscePostBluePrintId, Long nextOscePostBlueprintId);

}
