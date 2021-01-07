package org.devgateway.toolkit.forms.wicket.page.lists.fm;

import java.io.Serializable;

/**
 * @author Octavian Ciubotaru
 */
public class Filter implements Serializable {

    public static final String ALL = "All";
    public static final String YES = "Yes";
    public static final String NO = "No";

    private String feature;

    private String visible = ALL;

    private String enabled = ALL;

    private String mandatory = ALL;

    private String visibleDep;

    private String enabledDep;

    private String mandatoryDep;

    private String mixin;

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getMandatory() {
        return mandatory;
    }

    public void setMandatory(String mandatory) {
        this.mandatory = mandatory;
    }

    public String getVisibleDep() {
        return visibleDep;
    }

    public void setVisibleDep(String visibleDep) {
        this.visibleDep = visibleDep;
    }

    public String getEnabledDep() {
        return enabledDep;
    }

    public void setEnabledDep(String enabledDep) {
        this.enabledDep = enabledDep;
    }

    public String getMandatoryDep() {
        return mandatoryDep;
    }

    public void setMandatoryDep(String mandatoryDep) {
        this.mandatoryDep = mandatoryDep;
    }

    public String getMixin() {
        return mixin;
    }

    public void setMixin(String mixin) {
        this.mixin = mixin;
    }
}
