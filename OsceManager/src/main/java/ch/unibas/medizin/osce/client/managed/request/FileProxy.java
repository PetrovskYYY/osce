// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;

@ProxyForName("ch.unibas.medizin.osce.domain.File")
public interface FileProxy extends EntityProxy {

    abstract Long getId();

    abstract void setId(Long id);

    abstract Integer getVersion();

    abstract void setVersion(Integer version);

    abstract String getPath();

    abstract void setPath(String path);

    abstract Integer getSortOrder();

    abstract void setSortOrder(Integer sortOrder);

    abstract String getDescription();

    abstract void setDescription(String description);

    abstract StandardizedRoleProxy getStandardizedRole();

    abstract void setStandardizedRole(StandardizedRoleProxy standardizedRole);
}
