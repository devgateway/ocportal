package org.devgateway.toolkit.forms.wicket.components.form;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.ladda.LaddaAjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.fm.DgFmComponentSubject;
import org.devgateway.toolkit.web.fm.service.DgFmService;

public class DgFmLaddaAjaxButton extends LaddaAjaxButton implements DgFmComponentSubject {

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

    public DgFmLaddaAjaxButton(String id, Buttons.Type type) {
        super(id, type);
    }

    public DgFmLaddaAjaxButton(String id, IModel<String> model, Buttons.Type type) {
        super(id, model, type);
    }

    public DgFmLaddaAjaxButton(String id, Form<?> form, Buttons.Type type) {
        super(id, form, type);
    }

    public DgFmLaddaAjaxButton(String id, IModel<String> model, Form<?> form, Buttons.Type type) {
        super(id, model, form, type);
    }
}
