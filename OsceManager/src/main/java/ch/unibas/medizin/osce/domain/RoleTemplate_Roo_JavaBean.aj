// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ch.unibas.medizin.osce.domain;

import ch.unibas.medizin.osce.domain.RoleBaseItem;
import java.lang.String;
import java.util.Date;
import java.util.List;

privileged aspect RoleTemplate_Roo_JavaBean {
    
    public String RoleTemplate.getTemplateName() {
        return this.templateName;
    }
    
    public void RoleTemplate.setTemplateName(String templateName) {
        this.templateName = templateName;
    }
    
    public Date RoleTemplate.getDate_cretaed() {
        return this.date_cretaed;
    }
    
    public void RoleTemplate.setDate_cretaed(Date date_cretaed) {
        this.date_cretaed = date_cretaed;
    }
    
    public Date RoleTemplate.getDate_edited() {
        return this.date_edited;
    }
    
    public void RoleTemplate.setDate_edited(Date date_edited) {
        this.date_edited = date_edited;
    }
    
    public List<RoleBaseItem> RoleTemplate.getRoleBaseItem() {
        return this.roleBaseItem;
    }
    
    public void RoleTemplate.setRoleBaseItem(List<RoleBaseItem> roleBaseItem) {
        this.roleBaseItem = roleBaseItem;
    }
    
}