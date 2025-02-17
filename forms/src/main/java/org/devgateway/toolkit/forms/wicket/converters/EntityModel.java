package org.devgateway.toolkit.forms.wicket.converters;

import org.apache.wicket.model.LoadableDetachableModel;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class EntityModel<T> extends LoadableDetachableModel<T> {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Class<T> entityClass;
    private transient SessionFactory sessionFactory;

    public EntityModel(Class<T> entityClass, Long id) {
        this.entityClass = entityClass;
        this.id = id;
        this.sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    @Override
    protected T load() {
        try (Session session = sessionFactory.openSession()) {
            return session.get(entityClass, id);
        }
    }
}
