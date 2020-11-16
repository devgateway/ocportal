package org.devgateway.toolkit.persistence.util;

/**
 * @author Octavian Ciubotaru
 */
public final class StringUtils {

    private StringUtils() {
    }

    /**
     * <p>Example input: PMCReport</p>
     * <p>Example output: pmcReport</p>
     */
    public static String uncapitalizeCamelCase(String value) {
        String[] strings = org.apache.commons.lang3.StringUtils.splitByCharacterTypeCamelCase(value);
        if (strings.length > 0) {
            strings[0] = strings[0].toLowerCase();
        }
        return String.join("", strings);
    }
}
