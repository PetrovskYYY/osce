// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.CourseProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostRoomProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceSequenceProxy;
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
import java.util.Set;

public abstract class CourseMobileDetailsView_Roo_Gwt extends Composite implements ProxyDetailsView<CourseProxy> {

    @UiField
    Element id;

    @UiField
    Element version;

    @UiField
    Element color;

    @UiField
    Element osce;

    @UiField
    Element oscePostRooms;

    @UiField
    Element osceSequence;

    CourseProxy proxy;

    public void setValue(CourseProxy proxy) {
        this.proxy = proxy;
        id.setInnerText(proxy.getId() == null ? "" : String.valueOf(proxy.getId()));
        version.setInnerText(proxy.getVersion() == null ? "" : String.valueOf(proxy.getVersion()));
        color.setInnerText(proxy.getColor() == null ? "" : String.valueOf(proxy.getColor()));
        osce.setInnerText(proxy.getOsce() == null ? "" : ch.unibas.medizin.osce.client.managed.ui.OsceProxyRenderer.instance().render(proxy.getOsce()));
        oscePostRooms.setInnerText(proxy.getOscePostRooms() == null ? "" : ch.unibas.medizin.osce.client.scaffold.place.CollectionRenderer.of(ch.unibas.medizin.osce.client.managed.ui.OscePostRoomProxyRenderer.instance()).render(proxy.getOscePostRooms()));
        osceSequence.setInnerText(proxy.getOsceSequence() == null ? "" : ch.unibas.medizin.osce.client.managed.ui.OsceSequenceProxyRenderer.instance().render(proxy.getOsceSequence()));
    }
}
