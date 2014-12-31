// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import ch.unibas.medizin.osce.shared.BindType;
import ch.unibas.medizin.osce.shared.Comparison;
import ch.unibas.medizin.osce.shared.PossibleFields;
import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;

@ProxyForName("ch.unibas.medizin.osce.domain.AdvancedSearchCriteria")
public interface AdvancedSearchCriteriaProxy extends EntityProxy {

    abstract Long getId();

    abstract void setId(Long id);

    abstract Integer getVersion();

    abstract void setVersion(Integer version);

    abstract PossibleFields getField();

    abstract void setField(PossibleFields field);

    abstract Long getObjectId();

    abstract void setObjectId(Long objectId);

    abstract BindType getBindType();

    abstract void setBindType(BindType bindType);

    abstract Comparison getComparation();

    abstract void setComparation(Comparison comparation);

    abstract String getValue();

    abstract void setValue(String value);

    abstract String getShownValue();

    abstract void setShownValue(String shownValue);

    abstract StandardizedRoleProxy getStandardizedRole();

    abstract void setStandardizedRole(StandardizedRoleProxy standardizedRole);
}
