/**
 *
 */
package org.devgateway.ocds.web.rest.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author mpostelnicu
 *
 */
public class GroupingFilterPagingRequest extends YearFilterPagingRequest {

    @Schema(title = "This parameter specified which category can be used for grouping the results."
            + " Possible values here are: bidTypeId, procuringEntityId.")
    private String groupByCategory;

    public String getGroupByCategory() {
        return groupByCategory;
    }

    public void setGroupByCategory(final String groupByCategory) {
        this.groupByCategory = groupByCategory;
    }

}
