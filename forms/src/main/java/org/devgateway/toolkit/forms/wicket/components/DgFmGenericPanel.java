package org.devgateway.toolkit.forms.wicket.components;

import org.apache.wicket.IGenericComponent;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.fm.DgFmComponentSubject;
import org.devgateway.toolkit.web.fm.service.DgFmService;

public class DgFmGenericPanel<T> extends GenericPanel<T> implements DgFmComponentSubject {

    @SpringBean
    protected DgFmService fmService;

    @Override
    public DgFmService getFmService() {
        return fmService;
    }

    @Override
    public boolean isEnabled() {
        return isFmEnabled(super::isEnabled);
    }

    @Override
    public boolean isVisible() {
        return isFmVisible(super::isVisible);
    }

    public DgFmGenericPanel(String id) {
        super(id);
    }

    public DgFmGenericPanel(String id, IModel<T> model) {
        super(id, model);
    }
}