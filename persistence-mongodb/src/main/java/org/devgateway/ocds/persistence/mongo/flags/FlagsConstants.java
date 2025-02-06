package org.devgateway.ocds.persistence.mongo.flags;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public final class FlagsConstants {

    public static final String I038_VALUE = "flags.i038.value";
    public static final String I007_VALUE = "flags.i007.value";
    public static final String I004_VALUE = "flags.i004.value"; //this is not used
    public static final String I019_VALUE = "flags.i019.value";
    public static final String I077_VALUE = "flags.i077.value";
    public static final String I180_VALUE = "flags.i180.value";
    public static final String I002_VALUE = "flags.i002.value";
    public static final String I085_VALUE = "flags.i085.value";
    public static final String I171_VALUE = "flags.i171.value";
    public static final String I184_VALUE = "flags.i184.value";
    public static final String I016_VALUE = "flags.i016.value";
    public static final String I045_VALUE = "flags.i045.value";
    public static final String I182_VALUE = "flags.i182.value";
    public static final String I083_VALUE = "flags.i083.value";


    public static final List<String> FLAGS_LIST = Collections.unmodifiableList(
            Arrays.asList(new String[]{I038_VALUE, I007_VALUE, I019_VALUE, I182_VALUE, I045_VALUE,
                    I077_VALUE, I180_VALUE, I002_VALUE, I085_VALUE, I171_VALUE, I184_VALUE, I016_VALUE
                    //,I083_VALUE this flag is not working properly and is disabled
            }));

    public static final Map<String, String> FLAG_VALUES_BY_NAME = Collections.unmodifiableMap(
            FLAGS_LIST.stream().collect(toMap(value -> value.substring(6, 10), identity())));

    private FlagsConstants() {
    }

}
