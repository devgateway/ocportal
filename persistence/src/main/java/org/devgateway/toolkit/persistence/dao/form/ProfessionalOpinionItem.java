package org.devgateway.toolkit.persistence.dao.form;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.devgateway.toolkit.persistence.dao.AbstractDocsChildExpAuditEntity;
import org.devgateway.toolkit.persistence.dao.ListViewItem;
import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.excel.annotation.ExcelExport;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * @author mpostelnicu
 */
@Entity
@Audited
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfessionalOpinionItem extends AbstractDocsChildExpAuditEntity<ProfessionalOpinion>
        implements ListViewItem {

    @ExcelExport(useTranslation = true, name = "Professional Opinion Date")
    private Date professionalOpinionDate;

    @ExcelExport(useTranslation = true, name = "Awardee")
    @ManyToOne
    private Supplier awardee;

    @ExcelExport(useTranslation = true, name = "Recommended Award Amount")
    private BigDecimal recommendedAwardAmount;

    public Date getProfessionalOpinionDate() {
        return professionalOpinionDate;
    }

    public void setProfessionalOpinionDate(final Date professionalOpinionDate) {
        this.professionalOpinionDate = professionalOpinionDate;
    }

    public Supplier getAwardee() {
        return awardee;
    }

    public void setAwardee(final Supplier awardee) {
        this.awardee = awardee;
    }

    public BigDecimal getRecommendedAwardAmount() {
        return recommendedAwardAmount;
    }

    public void setRecommendedAwardAmount(final BigDecimal recommendedAwardAmount) {
        this.recommendedAwardAmount = recommendedAwardAmount;
    }

    @Override
    public String toString() {
        return Objects.toString(awardee, "") + " " + Objects.toString(recommendedAwardAmount, "");
    }
}
