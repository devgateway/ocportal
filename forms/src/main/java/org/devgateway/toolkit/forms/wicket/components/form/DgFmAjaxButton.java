package org.devgateway.toolkit.forms.wicket.components.form;

import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.fm.DgFmComponentSubject;
import org.devgateway.toolkit.persistence.fm.service.DgFmService;

public class DgFmAjaxButton extends AjaxButton implements DgFmComponentSubject {

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

    public DgFmAjaxButton(String id) {
        super(id);
    }

    public DgFmAjaxButton(String id, IModel<String> model) {
        super(id, model);
    }

    public DgFmAjaxButton(String id, Form<?> form) {
        super(id, form);
    }

    public DgFmAjaxButton(String id, IModel<String> model, Form<?> form) {
        super(id, model, form);
    }
}
