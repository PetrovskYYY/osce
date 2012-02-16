// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import com.google.gwt.requestfactory.shared.EntityProxy;
import com.google.gwt.requestfactory.shared.ProxyForName;
import java.util.Date;
import org.springframework.roo.addon.gwt.RooGwtMirroredFrom;

@RooGwtMirroredFrom("ch.unibas.medizin.osce.domain.LogEntry")
@ProxyForName("ch.unibas.medizin.osce.domain.LogEntry")
public interface LogEntryProxy extends EntityProxy {

    abstract Long getId();

    abstract void setId(Long id);

    abstract Integer getVersion();

    abstract void setVersion(Integer version);

    abstract Integer getShibId();

    abstract void setShibId(Integer shibId);

    abstract Date getLogtime();

    abstract void setLogtime(Date logtime);

    abstract String getOldValue();

    abstract void setOldValue(String oldValue);

    abstract String getNewValue();

    abstract void setNewValue(String newValue);
}