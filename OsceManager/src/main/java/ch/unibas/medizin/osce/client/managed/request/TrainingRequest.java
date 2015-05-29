// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import java.util.Date;
import java.util.List;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;

@ServiceName("ch.unibas.medizin.osce.domain.Training")
public interface TrainingRequest extends RequestContext {

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.TrainingProxy, java.lang.Void> persist();

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.TrainingProxy, java.lang.Void> remove();

    abstract Request<java.lang.Long> countTrainings();

    abstract Request<ch.unibas.medizin.osce.client.managed.request.TrainingProxy> findTraining(Long id);

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.TrainingProxy>> findAllTrainings();

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.TrainingProxy>> findTrainingEntries(int firstResult, int maxResults);

    abstract  Request<TrainingProxy> createTraining(Date startTimeDate, Date endTimedate, Long roleId,Long semId, boolean isShowingSuggestions);

	abstract Request<List<TrainingProxy>> findAllTrainingsByTimeAsc(Long semId);

	abstract Request<List<TrainingProxy>> findTrainingsOfGivenDate(Date currentlySelectedDate,Long semId);

	abstract Request<TrainingProxy> updateTraining(Date startTimeDate, Date endTimedate,Long roleId, Long semId, Long trainingId);

	abstract Request<List<StandardizedRoleProxy>> findAllRolesAssignInBlock(Date currentlySelectedDate, Long semId);
	
	abstract Request<Boolean> deleteTrainingOfGivenId(Long trainingId);

	abstract Request<TrainingProxy> findIsTrainingOverLapsWithAnyTraining(Date startTimeDate,Date endTimedate, Long semId,Long roleId);

	abstract Request<Boolean> findSelectedDateISTrainingDate(Date currentlySelectedDate, Long semesterProxyId);

}