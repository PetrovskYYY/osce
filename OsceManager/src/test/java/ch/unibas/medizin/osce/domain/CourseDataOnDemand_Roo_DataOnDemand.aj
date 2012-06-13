// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ch.unibas.medizin.osce.domain;

import ch.unibas.medizin.osce.domain.Course;
import ch.unibas.medizin.osce.domain.Osce;
import ch.unibas.medizin.osce.domain.OsceDataOnDemand;
import ch.unibas.medizin.osce.domain.OsceSequence;
import java.lang.String;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

privileged aspect CourseDataOnDemand_Roo_DataOnDemand {
    
    declare @type: CourseDataOnDemand: @Component;
    
    private Random CourseDataOnDemand.rnd = new SecureRandom();
    
    private List<Course> CourseDataOnDemand.data;
    
    @Autowired
    private OsceDataOnDemand CourseDataOnDemand.osceDataOnDemand;
    
    public Course CourseDataOnDemand.getNewTransientCourse(int index) {
        Course obj = new Course();
        setColor(obj, index);
        setOsce(obj, index);
        setOsceSequence(obj, index);
        return obj;
    }
    
    public void CourseDataOnDemand.setColor(Course obj, int index) {
        String color = "color_" + index;
        obj.setColor(color);
    }
    
    public void CourseDataOnDemand.setOsce(Course obj, int index) {
        Osce osce = osceDataOnDemand.getRandomOsce();
        obj.setOsce(osce);
    }
    
    public void CourseDataOnDemand.setOsceSequence(Course obj, int index) {
        OsceSequence osceSequence = null;
        obj.setOsceSequence(osceSequence);
    }
    
    public Course CourseDataOnDemand.getSpecificCourse(int index) {
        init();
        if (index < 0) index = 0;
        if (index > (data.size() - 1)) index = data.size() - 1;
        Course obj = data.get(index);
        return Course.findCourse(obj.getId());
    }
    
    public Course CourseDataOnDemand.getRandomCourse() {
        init();
        Course obj = data.get(rnd.nextInt(data.size()));
        return Course.findCourse(obj.getId());
    }
    
    public boolean CourseDataOnDemand.modifyCourse(Course obj) {
        return false;
    }
    
    public void CourseDataOnDemand.init() {
        data = Course.findCourseEntries(0, 10);
        if (data == null) throw new IllegalStateException("Find entries implementation for 'Course' illegally returned null");
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<ch.unibas.medizin.osce.domain.Course>();
        for (int i = 0; i < 10; i++) {
            Course obj = getNewTransientCourse(i);
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
