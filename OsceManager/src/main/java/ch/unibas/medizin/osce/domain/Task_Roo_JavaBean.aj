// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ch.unibas.medizin.osce.domain;

import ch.unibas.medizin.osce.domain.Administrator;
import ch.unibas.medizin.osce.domain.Task;
import java.lang.Boolean;
import java.lang.String;
import java.util.Date;

privileged aspect Task_Roo_JavaBean {
    
    public String Task.getName() {
        return this.name;
    }
    
    public void Task.setName(String name) {
        this.name = name;
    }
    
    public Date Task.getDeadline() {
        return this.deadline;
    }
    
    public void Task.setDeadline(Date deadline) {
        this.deadline = deadline;
    }
    
    public Boolean Task.getIsDone() {
        return this.isDone;
    }
    
    public void Task.setIsDone(Boolean isDone) {
        this.isDone = isDone;
    }
    
    public Task Task.getOsce() {
        return this.osce;
    }
    
    public void Task.setOsce(Task osce) {
        this.osce = osce;
    }
    
    public Administrator Task.getAdministrator() {
        return this.administrator;
    }
    
    public void Task.setAdministrator(Administrator administrator) {
        this.administrator = administrator;
    }
    
}