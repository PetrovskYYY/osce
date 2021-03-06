// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;

@ServiceName("ch.unibas.medizin.osce.domain.MediaContentType")
public interface MediaContentTypeRequest extends RequestContext {

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.MediaContentTypeProxy, java.lang.Void> persist();

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.MediaContentTypeProxy, java.lang.Void> remove();

    abstract Request<java.lang.Long> countMediaContentTypes();

    abstract Request<ch.unibas.medizin.osce.client.managed.request.MediaContentTypeProxy> findMediaContentType(Long id);

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.MediaContentTypeProxy>> findAllMediaContentTypes();

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.MediaContentTypeProxy>> findMediaContentTypeEntries(int firstResult, int maxResults);
}
