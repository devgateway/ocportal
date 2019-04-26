package org.devgateway.toolkit.persistence.dao.form;

import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author idobre
 * @since 2019-04-24
 */
@Entity
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(indexes = {@Index(columnList = "procurement_plan_id")})
public class ProfessionalOpinion extends AbstractMakueniForm {
    @OneToOne(fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore    
    private TenderQuotationEvaluation tenderQuotationEvaluation;

    private Date professionalOpinionDate;

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne
    private Supplier awardee;

    private Double recommendedAwardAmount;

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

    public Double getRecommendedAwardAmount() {
        return recommendedAwardAmount;
    }

    public void setRecommendedAwardAmount(final Double recommendedAwardAmount) {
        this.recommendedAwardAmount = recommendedAwardAmount;
    }

    public TenderQuotationEvaluation getTenderQuotationEvaluation() {
        return tenderQuotationEvaluation;
    }

    public void setTenderQuotationEvaluation(final TenderQuotationEvaluation tenderQuotationEvaluation) {
        this.tenderQuotationEvaluation = tenderQuotationEvaluation;
    }

    @Override
    public void setLabel(final String label) {

    }

    @Override
    public String getLabel() {
        return null;
    }

    @Override
    public String toString() {
        return getLabel();
    }
}
