// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;

@ServiceName("ch.unibas.medizin.osce.domain.RoleParticipant")
public interface RoleParticipantRequest extends RequestContext {

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.RoleParticipantProxy, java.lang.Void> persist();

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.RoleParticipantProxy, java.lang.Void> remove();

    abstract Request<java.lang.Long> countRoleParticipants();

    abstract Request<ch.unibas.medizin.osce.client.managed.request.RoleParticipantProxy> findRoleParticipant(Long id);

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.RoleParticipantProxy>> findAllRoleParticipants();

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.RoleParticipantProxy>> findRoleParticipantEntries(int firstResult, int maxResults);

    abstract Request<List<RoleParticipantProxy>> findDoctorWithStandardizedRoleAndRoleTopic(Long id,Integer type,int start,int length);
	abstract Request<Long> countDoctorWithStandardizedRoleAndRoleTopic(Long id,Integer type);
	abstract Request<List<RoleParticipantProxy>> findRoleParticipatentByDoctor(DoctorProxy proxy);
}
