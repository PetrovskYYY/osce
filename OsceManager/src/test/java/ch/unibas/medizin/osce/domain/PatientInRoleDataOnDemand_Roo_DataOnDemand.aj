// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ch.unibas.medizin.osce.domain;

import ch.unibas.medizin.osce.domain.OscePost;
import ch.unibas.medizin.osce.domain.OscePostDataOnDemand;
import ch.unibas.medizin.osce.domain.PatientInRole;
import ch.unibas.medizin.osce.domain.PatientInSemester;
import ch.unibas.medizin.osce.domain.PatientInSemesterDataOnDemand;
import java.lang.Boolean;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

privileged aspect PatientInRoleDataOnDemand_Roo_DataOnDemand {
    
    declare @type: PatientInRoleDataOnDemand: @Component;
    
    private Random PatientInRoleDataOnDemand.rnd = new SecureRandom();
    
    private List<PatientInRole> PatientInRoleDataOnDemand.data;
    
    @Autowired
    private OscePostDataOnDemand PatientInRoleDataOnDemand.oscePostDataOnDemand;
    
    @Autowired
    private PatientInSemesterDataOnDemand PatientInRoleDataOnDemand.patientInSemesterDataOnDemand;
    
    public PatientInRole PatientInRoleDataOnDemand.getNewTransientPatientInRole(int index) {
        PatientInRole obj = new PatientInRole();
        setFit_criteria(obj, index);
        setIs_backup(obj, index);
        setOscePost(obj, index);
        setPatientInSemester(obj, index);
        setStayInPost(obj, index);
        return obj;
    }
    
    public void PatientInRoleDataOnDemand.setFit_criteria(PatientInRole obj, int index) {
        Boolean fit_criteria = Boolean.TRUE;
        obj.setFit_criteria(fit_criteria);
    }
    
    public void PatientInRoleDataOnDemand.setIs_backup(PatientInRole obj, int index) {
        Boolean is_backup = Boolean.TRUE;
        obj.setIs_backup(is_backup);
    }
    
    public void PatientInRoleDataOnDemand.setOscePost(PatientInRole obj, int index) {
        OscePost oscePost = oscePostDataOnDemand.getRandomOscePost();
        obj.setOscePost(oscePost);
    }
    
    public void PatientInRoleDataOnDemand.setPatientInSemester(PatientInRole obj, int index) {
        PatientInSemester patientInSemester = patientInSemesterDataOnDemand.getRandomPatientInSemester();
        obj.setPatientInSemester(patientInSemester);
    }
    
    public void PatientInRoleDataOnDemand.setStayInPost(PatientInRole obj, int index) {
        Boolean stayInPost = Boolean.TRUE;
        obj.setStayInPost(stayInPost);
    }
    
    public PatientInRole PatientInRoleDataOnDemand.getSpecificPatientInRole(int index) {
        init();
        if (index < 0) index = 0;
        if (index > (data.size() - 1)) index = data.size() - 1;
        PatientInRole obj = data.get(index);
        return PatientInRole.findPatientInRole(obj.getId());
    }
    
    public PatientInRole PatientInRoleDataOnDemand.getRandomPatientInRole() {
        init();
        PatientInRole obj = data.get(rnd.nextInt(data.size()));
        return PatientInRole.findPatientInRole(obj.getId());
    }
    
    public boolean PatientInRoleDataOnDemand.modifyPatientInRole(PatientInRole obj) {
        return false;
    }
    
    public void PatientInRoleDataOnDemand.init() {
        data = PatientInRole.findPatientInRoleEntries(0, 10);
        if (data == null) throw new IllegalStateException("Find entries implementation for 'PatientInRole' illegally returned null");
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<ch.unibas.medizin.osce.domain.PatientInRole>();
        for (int i = 0; i < 10; i++) {
            PatientInRole obj = getNewTransientPatientInRole(i);
            try {
                obj.persist();
            } catch (ConstraintViolationException e) {
                StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> it = e.getConstraintViolations().iterator(); it.hasNext();) {
                    ConstraintViolation<?> cv = it.next();
                    msg.append("[").append(cv.getConstraintDescriptor()).append(":").append(cv.getMessage()).append("=").append(cv.getInvalidValue()).append("]");
                }
                throw new RuntimeException(msg.toString(), e);
            }
            obj.flush();
            data.add(obj);
        }
    }
    
}
