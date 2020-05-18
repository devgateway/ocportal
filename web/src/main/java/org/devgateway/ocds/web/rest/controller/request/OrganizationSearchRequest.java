package org.devgateway.ocds.web.rest.controller.request;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Size;

/**
 * @author mpostelnicu
 */
public class OrganizationSearchRequest extends GenericPagingRequest {

    @Size(min = 3, max = 30)
    @ApiModelProperty(value = "Searches organization fields (name and id) by the given keyword text. "
            + "This uses full text search.")
    private String text;

    @ApiModelProperty(value = "The organization scheme. "
            + "See https://standard.open-contracting.org/latest/en/schema/identifiers/")
    private String scheme;

    public OrganizationSearchRequest() {
        super();
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }
}
