package org.devgateway.toolkit.persistence.dao;

import org.devgateway.toolkit.persistence.validator.RequiredAttrsBean;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used by {@link RequiredAttrsBean} to search all fields from a form.
 *
 * @author Octavian Ciubotaru
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Form {
}
