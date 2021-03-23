package org.devgateway.toolkit.persistence.service.excel;

import org.devgateway.toolkit.persistence.dao.prequalification.AbstractContact;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalifiedSupplier;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalifiedSupplierItem;

import java.util.stream.Collectors;

/**
 * @author Octavian Ciubotaru
 */
public final class ExporterUtil {

    private ExporterUtil() {
    }

    public static String extractDirectors(PrequalifiedSupplier prequalifiedSupplier) {
        return prequalifiedSupplier.getItems().stream()
                .map(PrequalifiedSupplierItem::getNonNullContact)
                .map(AbstractContact::getDirectors)
                .distinct()
                .sorted()
                .collect(Collectors.joining(", "));
    }
}
