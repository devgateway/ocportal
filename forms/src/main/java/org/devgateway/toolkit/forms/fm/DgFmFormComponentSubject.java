package org.devgateway.toolkit.forms.fm;

import org.devgateway.toolkit.web.fm.DgFmInputSubject;

/**
 * Interface that should be attached to objects for FM behavior, providing input, and also bound to wicket
 * {@link org.apache.wicket.Component} because it extends {@link DgFmComponentSubject}
 */
public interface DgFmFormComponentSubject extends DgFmInputSubject, DgFmComponentSubject {

}
