// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;
import java.util.Set;

@ProxyForName("ch.unibas.medizin.osce.domain.Specialisation")
public interface SpecialisationProxy extends EntityProxy {

    abstract Long getId();

    abstract void setId(Long id);

    abstract Integer getVersion();

    abstract void setVersion(Integer version);

    abstract String getName();

    abstract void setName(String name);

    abstract Set<ch.unibas.medizin.osce.client.managed.request.RoleTopicProxy> getRoleTopics();

    abstract void setRoleTopics(Set<RoleTopicProxy> roleTopics);

    abstract Set<ch.unibas.medizin.osce.client.managed.request.DoctorProxy> getDoctors();

    abstract void setDoctors(Set<DoctorProxy> doctors);

    abstract Set<ch.unibas.medizin.osce.client.managed.request.OscePostBlueprintProxy> getOscePostBlueprint();

    abstract void setOscePostBlueprint(Set<OscePostBlueprintProxy> oscePostBlueprint);
}
