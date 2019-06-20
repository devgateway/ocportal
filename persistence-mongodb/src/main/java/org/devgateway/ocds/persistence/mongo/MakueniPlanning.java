package org.devgateway.ocds.persistence.mongo;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.devgateway.ocds.persistence.mongo.excel.annotation.ExcelExport;
import org.devgateway.ocds.persistence.mongo.excel.annotation.ExcelExportSepareteSheet;

import java.util.LinkedHashSet;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MakueniPlanning extends Planning {

    @JsonProperty("items")
    @ExcelExport
    @ExcelExportSepareteSheet
    @JsonDeserialize(as = LinkedHashSet.class)
    @JsonPropertyDescription("The goods and services planned to be purchased, broken into line items wherever possible."
            + " Items should not be duplicated, but a quantity of 2 specified instead.")
    private Set<Item> items = new LinkedHashSet<>();

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }
}
