package org.devgateway.ocds.web.rest.controller.flags;

import org.bson.Document;
import org.devgateway.ocds.web.rest.controller.request.YearFilterPagingRequest;

import java.util.List;

/**
 * @author Octavian Ciubotaru
 */
public abstract class AbstractSingleFlagCrosstabController extends AbstractFlagCrosstabController {

    public abstract List<Document> flagStats(YearFilterPagingRequest filter);
}
