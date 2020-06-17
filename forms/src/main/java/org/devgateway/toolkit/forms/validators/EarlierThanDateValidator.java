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
package org.devgateway.toolkit.forms.validators;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

import java.util.Date;

/**
 * Validator to test if the date is earlier than provided high date
 * 
 * @author idobre
 * @since 5/18/15
 */
public class EarlierThanDateValidator extends AbstractEarlierThanDateValidator<Date> {
    private static final long serialVersionUID = -6111787761451589941L;

    public EarlierThanDateValidator(final Date highDate) {
        super(highDate);
    }

    protected boolean isBefore(final Date highValue, final Date currentValue) {
        return highValue.before(currentValue);
    }
}
