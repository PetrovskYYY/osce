// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import java.util.Set;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;

@ProxyForName("ch.unibas.medizin.osce.domain.Nationality")
public interface NationalityProxy extends EntityProxy {

    abstract Long getId();

    abstract void setId(Long id);

    abstract Integer getVersion();

    abstract void setVersion(Integer version);

    abstract String getNationality();

    abstract void setNationality(String nationality);

    abstract Set<ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy> getStandardizedpatients();

    abstract void setStandardizedpatients(Set<StandardizedPatientProxy> standardizedpatients);
    
    abstract Set<StandardizedPatientProxy> getStandardizedpatientsOfCountry();

	abstract void setStandardizedpatientsOfCountry(Set<StandardizedPatientProxy> standardizedpatientsOfCountry);
}