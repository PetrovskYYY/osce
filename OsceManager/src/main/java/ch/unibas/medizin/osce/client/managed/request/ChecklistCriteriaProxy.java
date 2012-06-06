// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import com.google.gwt.requestfactory.shared.EntityProxy;
import com.google.gwt.requestfactory.shared.ProxyForName;
import org.springframework.roo.addon.gwt.RooGwtMirroredFrom;

@RooGwtMirroredFrom("ch.unibas.medizin.osce.domain.ChecklistCriteria")
@ProxyForName("ch.unibas.medizin.osce.domain.ChecklistCriteria")
public interface ChecklistCriteriaProxy extends EntityProxy {

    abstract Long getId();

    abstract void setId(Long id);

    abstract Integer getVersion();

    abstract void setVersion(Integer version);

    abstract String getCriteria();

    abstract void setCriteria(String criteria);

    abstract ChecklistQuestionProxy getChecklistQuestion();

    abstract void setChecklistQuestion(ChecklistQuestionProxy checklistQuestion);
}