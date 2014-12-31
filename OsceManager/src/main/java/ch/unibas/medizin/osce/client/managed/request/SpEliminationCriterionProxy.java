// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;

@ProxyForName("ch.unibas.medizin.osce.domain.spportal.SpEliminationCriterion")
public interface SpEliminationCriterionProxy extends EntityProxy {

    abstract Long getId();

    abstract void setId(Long id);

    abstract Integer getVersion();

    abstract void setVersion(Integer version);

    abstract Boolean getAnamnesisCheckValue();

    abstract void setAnamnesisCheckValue(Boolean anamnesisCheckValue);

    abstract SpScarProxy getScar();

    abstract void setScar(SpScarProxy scar);

    abstract SpAnamnesisCheckProxy getAnamnesisCheck();

    abstract void setAnamnesisCheck(SpAnamnesisCheckProxy anamnesisCheck);
}
