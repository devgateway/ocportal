package org.devgateway.ocds.web.spring;

import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;

final class HibernateProxyUtil {
    private HibernateProxyUtil() {
    }
    public static Class<?> getClassWithoutInitializingProxy(Object object) {
        if (object instanceof HibernateProxy) {
            HibernateProxy proxy = (HibernateProxy) object;
            LazyInitializer li = proxy.getHibernateLazyInitializer();
            return li.getPersistentClass();  // Returns the actual class of the entity
        } else {
            return object.getClass();  // Return the class for non-proxies
        }
    }
}
