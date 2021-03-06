// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import java.util.List;

import ch.unibas.medizin.osce.shared.Sorting;
import ch.unibas.medizin.osce.shared.StudyYears;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;

@ServiceName("ch.unibas.medizin.osce.domain.Specialisation")
public interface SpecialisationRequest extends RequestContext {

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.SpecialisationProxy, java.lang.Void> persist();

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.SpecialisationProxy, java.lang.Void> remove();

    abstract Request<java.lang.Long> countSpecialisations();

    abstract Request<ch.unibas.medizin.osce.client.managed.request.SpecialisationProxy> findSpecialisation(Long id);

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.SpecialisationProxy>> findAllSpecialisations();

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.SpecialisationProxy>> findSpecialisationEntries(int firstResult, int maxResults);

    abstract Request<Long> countSpecializations(String name);
	
	abstract Request<List<SpecialisationProxy>> findAllSpecialisation(String sortname,Sorting sortorder,String name, int firstResult, int maxResults);
	
	//abstract Request<List<SpecialisationProxy>> findSpecialisationSortByName();
	abstract Request<List<SpecialisationProxy>> findSpecialisationSortByName(StudyYears studyYear);

	abstract  Request<List<SpecialisationProxy>> findSpecialisations();
}
