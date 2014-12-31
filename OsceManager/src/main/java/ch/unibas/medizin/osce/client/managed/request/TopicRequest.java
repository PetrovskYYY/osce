// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;

@ServiceName("ch.unibas.medizin.osce.domain.Topic")
public interface TopicRequest extends RequestContext {

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.TopicProxy, java.lang.Void> persist();

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.TopicProxy, java.lang.Void> remove();

    abstract Request<java.lang.Long> countTopics();

    abstract Request<ch.unibas.medizin.osce.client.managed.request.TopicProxy> findTopic(Long id);

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.TopicProxy>> findAllTopics();

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.TopicProxy>> findTopicEntries(int firstResult, int maxResults);

	abstract Request<List<TopicProxy>> findTopicByClassiTopic(Long value);

}
