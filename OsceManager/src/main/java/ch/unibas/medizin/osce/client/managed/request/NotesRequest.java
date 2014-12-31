// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;

@ServiceName("ch.unibas.medizin.osce.domain.Notes")
public interface NotesRequest extends RequestContext {

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.NotesProxy, java.lang.Void> persist();

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.NotesProxy, java.lang.Void> remove();

    abstract Request<java.lang.Long> countNoteses();

    abstract Request<ch.unibas.medizin.osce.client.managed.request.NotesProxy> findNotes(Long id);

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.NotesProxy>> findAllNoteses();

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.NotesProxy>> findNotesEntries(int firstResult, int maxResults);
}
