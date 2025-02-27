package org.devgateway.ocds.web.rest.controller.flags;

import org.bson.Document;
import org.devgateway.ocds.web.rest.controller.request.YearFilterPagingRequest;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.validation.Valid;
import java.util.List;

/**
 * @author Octavian Ciubotaru
 */
public abstract class AbstractSingleFlagStatsController extends AbstractFlagStatsController {

    public abstract List<Document> flagStats(@ModelAttribute @Valid YearFilterPagingRequest filter);
}
