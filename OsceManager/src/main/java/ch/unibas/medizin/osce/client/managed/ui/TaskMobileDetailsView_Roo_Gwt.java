// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.AdministratorProxy;
import ch.unibas.medizin.osce.client.managed.request.TaskProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyDetailsView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class TaskMobileDetailsView_Roo_Gwt extends Composite implements ProxyDetailsView<TaskProxy> {

    @UiField
    Element id;

    @UiField
    Element version;

    @UiField
    Element name;

    @UiField
    Element deadline;

    @UiField
    Element isDone;

    @UiField
    Element osce;

    @UiField
    Element administrator;

    TaskProxy proxy;

    public void setValue(TaskProxy proxy) {
        this.proxy = proxy;
        id.setInnerText(proxy.getId() == null ? "" : String.valueOf(proxy.getId()));
        version.setInnerText(proxy.getVersion() == null ? "" : String.valueOf(proxy.getVersion()));
        name.setInnerText(proxy.getName() == null ? "" : String.valueOf(proxy.getName()));
        deadline.setInnerText(proxy.getDeadline() == null ? "" : DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_MEDIUM).format(proxy.getDeadline()));
        isDone.setInnerText(proxy.getIsDone() == null ? "" : String.valueOf(proxy.getIsDone()));
        osce.setInnerText(proxy.getOsce() == null ? "" : ch.unibas.medizin.osce.client.managed.ui.TaskProxyRenderer.instance().render(proxy.getOsce()));
        administrator.setInnerText(proxy.getAdministrator() == null ? "" : ch.unibas.medizin.osce.client.managed.ui.AdministratorProxyRenderer.instance().render(proxy.getAdministrator()));
    }
}
