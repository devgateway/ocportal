/**
 * Copyright (c) 2015 Development Gateway, Inc and others.
 * <p>
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 * <p>
 * Contributors:
 * Development Gateway - initial API and implementation
 */
/**
 *
 */
package org.devgateway.toolkit.forms.wicket.components;

import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.fm.DgFmComponentSubject;
import org.devgateway.toolkit.persistence.fm.service.DgFmService;

/**
 * @author mpostelnicu
 *
 */
public class FieldPanel<T> extends GenericPanel<T> implements DgFmComponentSubject {

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

    public FieldPanel(final String id) {
        super(id);
    }

    /**
     * @param id
     * @param model
     */
    public FieldPanel(final String id, final IModel<T> model) {
        super(id, model);
    }

}
