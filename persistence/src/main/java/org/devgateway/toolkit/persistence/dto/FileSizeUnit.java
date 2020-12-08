package org.devgateway.toolkit.persistence.dto;

/**
 * Used for FileSize
 * @author Nadejda Mandrescu
 * @see FileSize
 */
public enum FileSizeUnit {
    B("B"),
    KB("KB"),
    MB("MB"),
    GB("GB"),
    TB("TB");

    // or can be UX standard of 1000
    public static final long UNIT_DIVISOR = 1024;

    private final String name;

    FileSizeUnit(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
