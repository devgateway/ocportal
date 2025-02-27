package org.devgateway.ocds.web.rest.controller.request;

import org.devgateway.ocds.persistence.mongo.flags.ValidFlagName;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;

/**
 * @author Octavian Ciubotaru
 */
public class FlagsWithFilterRequest extends YearFilterPagingRequest {

    @NotEmpty
    private ArrayList<@NotNull @ValidFlagName String> flags;

    public ArrayList<String> getFlags() {
        return flags;
    }

    public void setFlags(ArrayList<String> flags) {
        this.flags = flags;
    }
}
