package org.devgateway.ocds.persistence.mongo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import org.devgateway.ocds.persistence.mongo.excel.annotation.ExcelExport;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MakueniAward extends Award {


    @JsonProperty("firstTimeWinner")
    @JsonPropertyDescription("The supplier of this award (in makueni case there is always one supplier) has won an "
            + "award for the first time.")
    @ExcelExport
    private Boolean firstTimeWinner;

    public Boolean getFirstTimeWinner() {
        return firstTimeWinner;
    }

    public void setFirstTimeWinner(Boolean firstTimeWinner) {
        this.firstTimeWinner = firstTimeWinner;
    }
}

