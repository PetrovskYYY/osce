// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ch.unibas.medizin.osce.domain;

import ch.unibas.medizin.osce.shared.BindType;
import ch.unibas.medizin.osce.shared.Comparison;
import ch.unibas.medizin.osce.shared.PossibleFields;
import java.lang.Long;
import java.lang.String;

privileged aspect AdvancedSearchCriteria_Roo_JavaBean {
    
    public PossibleFields AdvancedSearchCriteria.getField() {
        return this.field;
    }
    
    public void AdvancedSearchCriteria.setField(PossibleFields field) {
        this.field = field;
    }
    
    public Long AdvancedSearchCriteria.getObjectId() {
        return this.objectId;
    }
    
    public void AdvancedSearchCriteria.setObjectId(Long objectId) {
        this.objectId = objectId;
    }
    
    public BindType AdvancedSearchCriteria.getBindType() {
        return this.bindType;
    }
    
    public void AdvancedSearchCriteria.setBindType(BindType bindType) {
        this.bindType = bindType;
    }
    
    public Comparison AdvancedSearchCriteria.getComparation() {
        return this.comparation;
    }
    
    public void AdvancedSearchCriteria.setComparation(Comparison comparation) {
        this.comparation = comparation;
    }
    
    public String AdvancedSearchCriteria.getValue() {
        return this.value;
    }
    
    public void AdvancedSearchCriteria.setValue(String value) {
        this.value = value;
    }
    
}
