package org.devgateway.toolkit.persistence.dto;

import java.io.Serializable;

/**
 * This class together with FileSizeUnit is needed to allow locale formatting for both size and unit.
 * It covers the limitations of FileUtils.byteCountToDisplaySize and org.apache.wicket.util.lang.Bytes.toString().
 *
 * @author Nadejda Mandrescu
 */
public class FileSize implements Serializable {
    private static final long serialVersionUID = 5848568685865060136L;

    private final double value;

    private final FileSizeUnit unit;

    public FileSize(final double sizeInBytes) {
        this(sizeInBytes, FileSizeUnit.B);
    }

    public FileSize(final double sizePerUnit, final FileSizeUnit fileSizeUnit) {
        this.value = sizePerUnit;
        this.unit = fileSizeUnit;
    }

    public double getValue() {
        return value;
    }

    public FileSizeUnit getUnit() {
        return unit;
    }

    public long toBytes() {
        return Math.round(value * Math.pow(FileSizeUnit.UNIT_DIVISOR, unit.ordinal()));
    }

    /**
     * @return file size using highest unit with size >= 1
     */
    public FileSize toHighest() {
        double v = value;
        FileSizeUnit u = this.unit;

        while (v > FileSizeUnit.UNIT_DIVISOR && (FileSizeUnit.values().length > u.ordinal() + 1)) {
            v = v / FileSizeUnit.UNIT_DIVISOR;
            u = FileSizeUnit.values()[u.ordinal() + 1];
        }
        return new FileSize(v, u);
    }
}
