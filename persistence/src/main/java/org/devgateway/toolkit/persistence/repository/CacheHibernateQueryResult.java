package org.devgateway.toolkit.persistence.repository;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.QueryHints;

/**
 * @author Octavian Ciubotaru
 */
@Target({ ElementType.METHOD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
public @interface CacheHibernateQueryResult {
}
