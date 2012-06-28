// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import ch.unibas.medizin.osce.shared.StudyYears;
import com.google.gwt.requestfactory.shared.EntityProxy;
import com.google.gwt.requestfactory.shared.ProxyForName;
import java.util.List;
import java.util.Set;
import org.springframework.roo.addon.gwt.RooGwtMirroredFrom;

@RooGwtMirroredFrom("ch.unibas.medizin.osce.domain.RoleTopic")
@ProxyForName("ch.unibas.medizin.osce.domain.RoleTopic")
public interface RoleTopicProxy extends EntityProxy {

    abstract Long getId();

    abstract void setId(Long id);

    abstract Integer getVersion();

    abstract void setVersion(Integer version);

    abstract String getName();

    abstract void setName(String name);

    abstract String getDescription();

    abstract void setDescription(String description);

    abstract StudyYears getStudyYear();

    abstract void setStudyYear(StudyYears studyYear);

    abstract Integer getSlotsUntilChange();

    abstract void setSlotsUntilChange(Integer slotsUntilChange);

    abstract List<ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy> getStandardizedRoles();

    abstract void setStandardizedRoles(List<StandardizedRoleProxy> standardizedRoles);

    abstract SpecialisationProxy getSpecialisation();

    abstract void setSpecialisation(SpecialisationProxy specialisation);

    abstract Set<ch.unibas.medizin.osce.client.managed.request.OscePostBlueprintProxy> getOscePostBlueprints();

    abstract void setOscePostBlueprints(Set<OscePostBlueprintProxy> oscePostBlueprints);
}
