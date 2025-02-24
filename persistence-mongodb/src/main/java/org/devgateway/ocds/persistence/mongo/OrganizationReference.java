package org.devgateway.ocds.persistence.mongo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.devgateway.ocds.persistence.mongo.excel.annotation.ExcelExport;

import java.io.Serializable;


/**
 * Organization Reference
 * <p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "id"
})
public class OrganizationReference implements Identifiable {

    /**
     * Common name
     * <p>
     * A common name for this organization or other participant in the contracting process. The identifier object
     * provides an space for the formal legal name, and so this may either repeat that value, or could provide the
     * common name by which this organization or entity is known. This field may also include details of the
     * department or sub-unit involved in this contracting process.
     */
    @JsonProperty("name")
    @ExcelExport
    @JsonPropertyDescription("A common name for this organization or other participant in the contracting process. "
            + "The identifier object provides an space for the formal legal name, and so this may either repeat that "
            + "value, or could provide the common name by which this organization or entity is known. This field may "
            + "also include details of the department or sub-unit involved in this contracting process.")
    protected String name;
    /**
     * Entity ID
     * <p>
     * The ID used for cross-referencing to this party from other sections of the release. This field may be built
     * with the following structure {identifier.scheme}-{identifier.id}(-{department-identifier}).
     */
    @JsonProperty("id")
    @ExcelExport
    @JsonPropertyDescription("The ID used for cross-referencing to this party from other sections of the release. "
            + "This field may be built with the following structure {identifier.scheme}-{identifier.id}"
            + "(-{department-identifier}).")
    protected String id;
    /**
     * Identifier
     * <p>
     */

    /**
     * Common name
     * <p>
     * A common name for this organization or other participant in the contracting process. The identifier object
     * provides an space for the formal legal name, and so this may either repeat that value, or could provide the
     * common name by which this organization or entity is known. This field may also include details of the
     * department or sub-unit involved in this contracting process.
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * Common name
     * <p>
     * A common name for this organization or other participant in the contracting process. The identifier object
     * provides an space for the formal legal name, and so this may either repeat that value, or could provide the
     * common name by which this organization or entity is known. This field may also include details of the
     * department or sub-unit involved in this contracting process.
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Entity ID
     * <p>
     * The ID used for cross-referencing to this party from other sections of the release. This field may be built
     * with the following structure {identifier.scheme}-{identifier.id}(-{department-identifier}).
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * Entity ID
     * <p>
     * The ID used for cross-referencing to this party from other sections of the release. This field may be built
     * with the following structure {identifier.scheme}-{identifier.id}(-{department-identifier}).
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", name)
                .append("id", id)
                .toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(name)
                .append(id)
                .toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof OrganizationReference rhs)) {
            return false;
        }
        return new EqualsBuilder()
                .append(name, rhs.name)
                .append(id, rhs.id)
                .isEquals();
    }

    @Override
    public Serializable getIdProperty() {
        return id;
    }


}
