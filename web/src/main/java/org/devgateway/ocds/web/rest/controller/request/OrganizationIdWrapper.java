package org.devgateway.ocds.web.rest.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class OrganizationIdWrapper {

    @Schema(title = "List of organization identifiers")
    private List<String> id;

    public List<String> getId() {
        return id;
    }

    public void setId(List<String> ids) {
        this.id = ids;
    }

}
