package org.devgateway.ocds.persistence.mongo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.Generated;
import java.util.Date;

/**
 * Period
 * <p>
 *
 * http://standard.open-contracting.org/latest/en/schema/reference/#period
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "startDate",
        "endDate"
})
public class Period {

    /**
     * The start date for the period.
     *
     */
    @JsonProperty("startDate")
    private Date startDate;

    /**
     * The end date for the period.
     *
     */
    @JsonProperty("endDate")
    private Date endDate;

    /**
     * The start date for the period.
     *
     * @return
     *     The startDate
     */
    @JsonProperty("startDate")
    public Date getStartDate() {
        return startDate;
    }

    /**
     * The start date for the period.
     *
     * @param startDate
     *     The startDate
     */
    @JsonProperty("startDate")
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * The end date for the period.
     *
     * @return
     *     The endDate
     */
    @JsonProperty("endDate")
    public Date getEndDate() {
        return endDate;
    }

    /**
     * The end date for the period.
     *
     * @param endDate
     *     The endDate
     */
    @JsonProperty("endDate")
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().
                append(startDate).
                append(endDate).
                toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Period) == false) {
            return false;
        }
        Period rhs = ((Period) other);
        return new EqualsBuilder().
                append(startDate, rhs.startDate).
                append(endDate, rhs.endDate).
                isEquals();
    }

}
