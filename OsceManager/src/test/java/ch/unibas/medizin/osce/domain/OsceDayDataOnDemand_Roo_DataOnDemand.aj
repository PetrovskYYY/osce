// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ch.unibas.medizin.osce.domain;

import ch.unibas.medizin.osce.domain.Osce;
import ch.unibas.medizin.osce.domain.OsceDataOnDemand;
import ch.unibas.medizin.osce.domain.OsceDay;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

privileged aspect OsceDayDataOnDemand_Roo_DataOnDemand {
    
    declare @type: OsceDayDataOnDemand: @Component;
    
    private Random OsceDayDataOnDemand.rnd = new SecureRandom();
    
    private List<OsceDay> OsceDayDataOnDemand.data;
    
    @Autowired
    private OsceDataOnDemand OsceDayDataOnDemand.osceDataOnDemand;
    
    public OsceDay OsceDayDataOnDemand.getNewTransientOsceDay(int index) {
        OsceDay obj = new OsceDay();
        setOsce(obj, index);
        setOsceDate(obj, index);
        setTimeEnd(obj, index);
        setTimeStart(obj, index);
        return obj;
    }
    
    public void OsceDayDataOnDemand.setOsce(OsceDay obj, int index) {
        Osce osce = osceDataOnDemand.getRandomOsce();
        obj.setOsce(osce);
    }
    
    public void OsceDayDataOnDemand.setOsceDate(OsceDay obj, int index) {
        Date osceDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setOsceDate(osceDate);
    }
    
    public void OsceDayDataOnDemand.setTimeEnd(OsceDay obj, int index) {
        Date timeEnd = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setTimeEnd(timeEnd);
    }
    
    public void OsceDayDataOnDemand.setTimeStart(OsceDay obj, int index) {
        Date timeStart = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setTimeStart(timeStart);
    }
    
    public OsceDay OsceDayDataOnDemand.getSpecificOsceDay(int index) {
        init();
        if (index < 0) index = 0;
        if (index > (data.size() - 1)) index = data.size() - 1;
        OsceDay obj = data.get(index);
        return OsceDay.findOsceDay(obj.getId());
    }
    
    public OsceDay OsceDayDataOnDemand.getRandomOsceDay() {
        init();
        OsceDay obj = data.get(rnd.nextInt(data.size()));
        return OsceDay.findOsceDay(obj.getId());
    }
    
    public boolean OsceDayDataOnDemand.modifyOsceDay(OsceDay obj) {
        return false;
    }
    
    public void OsceDayDataOnDemand.init() {
        data = OsceDay.findOsceDayEntries(0, 10);
        if (data == null) throw new IllegalStateException("Find entries implementation for 'OsceDay' illegally returned null");
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<ch.unibas.medizin.osce.domain.OsceDay>();
        for (int i = 0; i < 10; i++) {
            OsceDay obj = getNewTransientOsceDay(i);
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
