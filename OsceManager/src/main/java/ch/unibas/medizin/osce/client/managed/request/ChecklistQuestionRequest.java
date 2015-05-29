// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;

@ServiceName("ch.unibas.medizin.osce.domain.ChecklistQuestion")
public interface ChecklistQuestionRequest extends RequestContext {

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.ChecklistQuestionProxy, java.lang.Void> persist();

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.ChecklistQuestionProxy, java.lang.Void> remove();

    abstract Request<java.lang.Long> countChecklistQuestions();

    abstract Request<ch.unibas.medizin.osce.client.managed.request.ChecklistQuestionProxy> findChecklistQuestion(Long id);

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.ChecklistQuestionProxy>> findAllChecklistQuestions();

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.ChecklistQuestionProxy>> findChecklistQuestionEntries(int firstResult, int maxResults);

	abstract InstanceRequest<ChecklistQuestionProxy, Void> questionMoveUp(long checklisTopictID);
	abstract InstanceRequest<ChecklistQuestionProxy, Void> questionMoveDown(long checklisTopictID);
	abstract Request<ChecklistQuestionProxy> findQuestionsByOrderSmaller(int sort_order,long checklisTopictID);
	abstract Request<ChecklistQuestionProxy> findQuestionsByOrderGreater(int sort_order,long checklistTopicID);
	abstract Request<Boolean> updateSequence(List<ChecklistQuestionProxy> ids);

}