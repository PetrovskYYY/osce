package ch.unibas.medizin.osce.server.spalloc.constraints;

import java.util.Set;

import ch.unibas.medizin.osce.domain.Assignment;
import ch.unibas.medizin.osce.domain.RoleTopic;
import ch.unibas.medizin.osce.server.spalloc.model.OsceModel;
import ch.unibas.medizin.osce.server.spalloc.model.ValPatient;
import ch.unibas.medizin.osce.server.spalloc.model.VarAssignment;

import net.sf.cpsolver.ifs.model.GlobalConstraint;

/**
 * This constraint assures that a SimPat changes the role (for roles that have a limitation
 * of slots) after playing it for a certain number of slots.
 * 
 * @author dk
 *
 */
public class ChangeRoleConstraint extends GlobalConstraint<VarAssignment, ValPatient> {

	@Override
	public void computeConflicts(ValPatient patient, Set<ValPatient> conflicts) {
		VarAssignment varAssignment = patient.variable();
		Assignment assignment = patient.variable().getOsceAssignment();
		OsceModel model = (OsceModel) patient.variable().getModel();
		
		for(VarAssignment va : assignedVariables()) {
			Assignment a = va.getOsceAssignment();
			
			// skip check of assignment with itself and assignments that are further away than +/- 1
			if(assignment.equals(a) ||
					(varAssignment.getNextAssignment() != assignment &&
					varAssignment.getPrevAssignment() != assignment))
				continue;
			
			ValPatient p = va.getAssignment();
			RoleTopic roleTopic = a.getOscePostRoom().getOscePost().getStandardizedRole().getRoleTopic();
			if(p.getPatient().equals(patient.getPatient()) &&
					roleTopic.getSlotsUntilChange() > 0 && 
					2 * model.getNrSimPatAssignmentSlots() > roleTopic.getSlotsUntilChange()) {
				conflicts.add(p);
			}
		}
		
	}

}