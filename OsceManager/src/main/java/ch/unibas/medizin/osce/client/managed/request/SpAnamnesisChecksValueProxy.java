// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;

@ProxyForName("ch.unibas.medizin.osce.domain.spportal.SpAnamnesisChecksValue")
public interface SpAnamnesisChecksValueProxy extends EntityProxy {

    abstract Long getId();

    abstract void setId(Long id);

    abstract Integer getVersion();

    abstract void setVersion(Integer version);

    abstract Boolean getTruth();

    abstract void setTruth(Boolean truth);

    abstract String getComment();

    abstract void setComment(String comment);

    abstract String getAnamnesisChecksValue();

    abstract void setAnamnesisChecksValue(String anamnesisChecksValue);

    abstract SpAnamnesisFormProxy getAnamnesisform();

    abstract void setAnamnesisform(SpAnamnesisFormProxy anamnesisform);

    abstract SpAnamnesisCheckProxy getAnamnesischeck();

    abstract void setAnamnesischeck(SpAnamnesisCheckProxy anamnesischeck);
}