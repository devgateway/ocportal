package org.devgateway.toolkit.persistence.test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.Serializable;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.devgateway.toolkit.persistence.dao.GenericPersistable;
import org.junit.Test;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * @author Octavian Ciubotaru
 */
public class IdSerializationTest {

    private static class DummyEntity extends AbstractPersistable<Long> implements Serializable {
    }

    @Test
    public void testDummyEntitySerialization() throws IllegalAccessException {
        DummyEntity obj = new DummyEntity();
        FieldUtils.writeField(obj, "id", 10L, true);

        DummyEntity obj2 = SerializationUtils.deserialize(SerializationUtils.serialize(obj));

        assertThat(obj2.getId(), is(nullValue()));
    }

    @Test
    public void testGenericPersistableSerialization() throws IllegalAccessException {
        GenericPersistable obj = new GenericPersistable();
        FieldUtils.writeField(obj, "id", 10L, true);

        GenericPersistable obj2 = SerializationUtils.deserialize(SerializationUtils.serialize(obj));

        assertThat(obj2.getId(), is(10L));
    }
}
