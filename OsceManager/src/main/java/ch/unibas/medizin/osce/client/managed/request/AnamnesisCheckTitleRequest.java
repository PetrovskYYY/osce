// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;

@ServiceName("ch.unibas.medizin.osce.domain.AnamnesisCheckTitle")
public interface AnamnesisCheckTitleRequest extends RequestContext {

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckTitleProxy, java.lang.Void> persist();

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckTitleProxy, java.lang.Void> remove();

    abstract Request<java.lang.Long> countAnamnesisCheckTitles();

    abstract Request<ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckTitleProxy> findAnamnesisCheckTitle(Long id);

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckTitleProxy>> findAllAnamnesisCheckTitles();

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckTitleProxy>> findAnamnesisCheckTitleEntries(int firstResult, int maxResults);

    abstract InstanceRequest<AnamnesisCheckTitleProxy, Void> insertNewSortOder(Integer preSortorder);
//	public abstract Request<AnamnesisCheckTitleProxy> findAnamnesisChecksBySortOder(int sort_order);
//	 public abstract InstanceRequest<AnamnesisCheckTitleProxy, Void> oderByPreviousAnamnesisCheckTitle(int preSortorder);
//	 public abstract Request<List<AnamnesisCheckTitleProxy>> findAnamnesisCheckTitlesBySortOderBetween(int lower, int upper);
	 //public abstract Request<List<AnamnesisCheckTitleProxy>> getReSortingList(Integer sortFrom);
		
	abstract Request<Void> reSorting(Integer sortFrom);	 
	 
	 abstract InstanceRequest<AnamnesisCheckTitleProxy, Void> moveUp();
	 
	 abstract InstanceRequest<AnamnesisCheckTitleProxy, Void> moveDown();

	//issue sol
	 Request<Integer> findMaxSortOrder();
	public abstract Request<Boolean> saveAnamnesisCheckTitleInSpPortal(AnamnesisCheckTitleProxy anamnesisCheckTitle);
	
	public abstract Request<Boolean> edittitleInSpportal(Long anamnesisCheckTitleId);
	
	public abstract Request<Boolean> deleteTitleFromSpPortal(Long deletedTitleId);
}