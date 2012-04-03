// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisFormProxy;
import ch.unibas.medizin.osce.client.managed.request.BankaccountProxy;
import ch.unibas.medizin.osce.client.managed.request.DescriptionProxy;
import ch.unibas.medizin.osce.client.managed.request.LangSkillProxy;
import ch.unibas.medizin.osce.client.managed.request.NationalityProxy;
import ch.unibas.medizin.osce.client.managed.request.ProfessionProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyDetailsView;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyListView;
import ch.unibas.medizin.osce.shared.Gender;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
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

public abstract class StandardizedPatientDetailsView_Roo_Gwt extends Composite implements ProxyDetailsView<StandardizedPatientProxy> {

    @UiField
    SpanElement id;

    @UiField
    SpanElement version;

    @UiField
    SpanElement gender;

    @UiField
    SpanElement name;

    @UiField
    SpanElement preName;

    @UiField
    SpanElement street;

    @UiField
    SpanElement city;

    @UiField
    SpanElement postalCode;

    @UiField
    SpanElement telephone;

    @UiField
    SpanElement telephone2;

    @UiField
    SpanElement mobile;

    @UiField
    SpanElement height;

    @UiField
    SpanElement weight;

    @UiField
    SpanElement immagePath;

    @UiField
    SpanElement videoPath;

    @UiField
    SpanElement birthday;

    @UiField
    SpanElement email;

    @UiField
    SpanElement descriptions;

    @UiField
    SpanElement bankAccount;

    @UiField
    SpanElement nationality;

    @UiField
    SpanElement profession;

    @UiField
    SpanElement anamnesisForm;

    @UiField
    SpanElement langskills;

    StandardizedPatientProxy proxy;

    @UiField
    SpanElement displayRenderer;

    public void setValue(StandardizedPatientProxy proxy) {
        this.proxy = proxy;
        id.setInnerText(proxy.getId() == null ? "" : String.valueOf(proxy.getId()));
        version.setInnerText(proxy.getVersion() == null ? "" : String.valueOf(proxy.getVersion()));
        gender.setInnerText(proxy.getGender() == null ? "" : String.valueOf(proxy.getGender()));
        name.setInnerText(proxy.getName() == null ? "" : String.valueOf(proxy.getName()));
        preName.setInnerText(proxy.getPreName() == null ? "" : String.valueOf(proxy.getPreName()));
        street.setInnerText(proxy.getStreet() == null ? "" : String.valueOf(proxy.getStreet()));
        city.setInnerText(proxy.getCity() == null ? "" : String.valueOf(proxy.getCity()));
        postalCode.setInnerText(proxy.getPostalCode() == null ? "" : String.valueOf(proxy.getPostalCode()));
        telephone.setInnerText(proxy.getTelephone() == null ? "" : String.valueOf(proxy.getTelephone()));
        telephone2.setInnerText(proxy.getTelephone2() == null ? "" : String.valueOf(proxy.getTelephone2()));
        mobile.setInnerText(proxy.getMobile() == null ? "" : String.valueOf(proxy.getMobile()));
        height.setInnerText(proxy.getHeight() == null ? "" : String.valueOf(proxy.getHeight()));
        weight.setInnerText(proxy.getWeight() == null ? "" : String.valueOf(proxy.getWeight()));
        immagePath.setInnerText(proxy.getImmagePath() == null ? "" : String.valueOf(proxy.getImmagePath()));
        videoPath.setInnerText(proxy.getVideoPath() == null ? "" : String.valueOf(proxy.getVideoPath()));
        birthday.setInnerText(proxy.getBirthday() == null ? "" : DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_MEDIUM).format(proxy.getBirthday()));
        email.setInnerText(proxy.getEmail() == null ? "" : String.valueOf(proxy.getEmail()));
        descriptions.setInnerText(proxy.getDescriptions() == null ? "" : ch.unibas.medizin.osce.client.managed.ui.DescriptionProxyRenderer.instance().render(proxy.getDescriptions()));
        bankAccount.setInnerText(proxy.getBankAccount() == null ? "" : ch.unibas.medizin.osce.client.managed.ui.BankaccountProxyRenderer.instance().render(proxy.getBankAccount()));
        nationality.setInnerText(proxy.getNationality() == null ? "" : ch.unibas.medizin.osce.client.managed.ui.NationalityProxyRenderer.instance().render(proxy.getNationality()));
        profession.setInnerText(proxy.getProfession() == null ? "" : ch.unibas.medizin.osce.client.managed.ui.ProfessionProxyRenderer.instance().render(proxy.getProfession()));
        anamnesisForm.setInnerText(proxy.getAnamnesisForm() == null ? "" : ch.unibas.medizin.osce.client.managed.ui.AnamnesisFormProxyRenderer.instance().render(proxy.getAnamnesisForm()));
        langskills.setInnerText(proxy.getLangskills() == null ? "" : ch.unibas.medizin.osce.client.scaffold.place.CollectionRenderer.of(ch.unibas.medizin.osce.client.managed.ui.LangSkillProxyRenderer.instance()).render(proxy.getLangskills()));
        displayRenderer.setInnerText(StandardizedPatientProxyRenderer.instance().render(proxy));
    }
}
