// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import ch.unibas.medizin.osce.shared.EditRequestState;
import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;
import java.util.Date;
import java.util.Set;

@ProxyForName("ch.unibas.medizin.osce.domain.spportal.SPPortalPerson")
public interface SPPortalPersonProxy extends EntityProxy {

    abstract Long getId();

    abstract void setId(Long id);

    abstract Integer getVersion();

    abstract void setVersion(Integer version);

    abstract String getEmail();

    abstract void setEmail(String email);

    abstract String getPassword();

    abstract void setPassword(String password);

    abstract Date getExpiration();

    abstract void setExpiration(Date expiration);

    abstract Boolean getIsFirstLogin();

    abstract void setIsFirstLogin(Boolean isFirstLogin);

    abstract EditRequestState getEditRequestState();

    abstract void setEditRequestState(EditRequestState editRequestState);

    abstract String getActivationUrl();

    abstract void setActivationUrl(String activationUrl);

    abstract String getToken();

    abstract void setToken(String token);

    abstract Boolean getChanged();

    abstract void setChanged(Boolean changed);

    abstract Set<ch.unibas.medizin.osce.client.managed.request.SpPatientInSemesterProxy> getPatientInSemester();

    abstract void setPatientInSemester(Set<SpPatientInSemesterProxy> patientInSemester);
}