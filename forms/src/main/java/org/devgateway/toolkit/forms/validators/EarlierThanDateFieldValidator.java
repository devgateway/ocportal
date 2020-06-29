/*******************************************************************************
 * Copyright (c) 2015 Development Gateway, Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 *
 * Contributors:
 * Development Gateway - initial API and implementation
 *******************************************************************************/
/**
 * 
 */
package org.devgateway.toolkit.forms.validators;

import org.devgateway.toolkit.forms.wicket.components.form.AbstractDateFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.DateFieldBootstrapFormComponent;

import java.util.Date;

/**
 * @author mpostelnicu {@link DateFieldBootstrapFormComponent} validator for
 *         dates that have a chronology
 */
public class EarlierThanDateFieldValidator extends AbstractEarlierThanDateFieldValidator<Date> {
    private static final long serialVersionUID = -2178272950402808280L;

    /**
     * Provide a {@link DateFieldBootstrapFormComponent} that has to be
     * chronologically after the current's
     * {@link DateFieldBootstrapFormComponent} validator
     *
     * @param highDate
     */
    public EarlierThanDateFieldValidator(final AbstractDateFieldBootstrapFormComponent highDate) {
        super(highDate);
    }

    protected boolean isBefore(final Date highValue, final Date currentValue) {
        return highValue.before(currentValue);
    }
}
