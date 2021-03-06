// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import java.util.Date;
import java.util.List;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;

@ServiceName("ch.unibas.medizin.osce.domain.TrainingDate")
public interface TrainingDateRequest extends RequestContext {

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.TrainingDateProxy, java.lang.Void> persist();

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.TrainingDateProxy, java.lang.Void> remove();

    abstract Request<java.lang.Long> countTrainingDates();

    abstract Request<ch.unibas.medizin.osce.client.managed.request.TrainingDateProxy> findTrainingDate(Long id);

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.TrainingDateProxy>> findAllTrainingDates();

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.TrainingDateProxy>> findTrainingDateEntries(int firstResult, int maxResults);

    abstract Request<TrainingBlockProxy>  persistThisDateAsTrainingDate(Date currentlySelectedDate,Long semesterId);

	abstract Request<Boolean> removeTrainingDate(Date currentlySelectedDate, Long id);

	abstract Request<List<TrainingDateProxy>> findTrainingDatesFromGivenDateToEndOfMonth(Date date,Long semId);

	abstract Request<TrainingBlockProxy>  persistSelectedMultipleDatesAsTrainingDates(List<Date> currentlySelectedDatesList, Long id);

	abstract  Request<List<TrainingSuggestionProxy>> findTrainingSuggestionsOfDate(Date currentlySelectedDate, Long semId);

}
