// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;

@ProxyForName("ch.unibas.medizin.osce.domain.Topic")
public interface TopicProxy extends EntityProxy {

    abstract Long getId();

    abstract void setId(Long id);

    abstract Integer getVersion();

    abstract void setVersion(Integer version);

    abstract String getTopicDesc();

    abstract void setTopicDesc(String topicDesc);

    abstract ClassificationTopicProxy getClassificationTopic();

    abstract void setClassificationTopic(ClassificationTopicProxy classificationTopic);
}
