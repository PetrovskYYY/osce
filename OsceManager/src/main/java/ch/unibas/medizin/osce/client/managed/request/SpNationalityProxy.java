// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;
import java.util.Set;

@ProxyForName("ch.unibas.medizin.osce.domain.spportal.SpNationality")
public interface SpNationalityProxy extends EntityProxy {

    abstract Long getId();

    abstract void setId(Long id);

    abstract Integer getVersion();

    abstract void setVersion(Integer version);

    abstract String getNationality();

    abstract void setNationality(String nationality);

    abstract Set<ch.unibas.medizin.osce.client.managed.request.SpStandardizedPatientProxy> getStandardizedpatients();

    abstract void setStandardizedpatients(Set<SpStandardizedPatientProxy> standardizedpatients);
}
