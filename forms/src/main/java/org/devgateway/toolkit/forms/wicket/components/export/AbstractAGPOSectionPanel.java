package org.devgateway.toolkit.forms.wicket.components.export;

import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.providers.GenericChoiceProvider;
import org.devgateway.toolkit.persistence.dto.NamedDateRange;
import org.devgateway.toolkit.persistence.service.category.FiscalYearService;

import java.io.Serializable;

/**
 * @author Octavian Ciubotaru
 */
public abstract class AbstractAGPOSectionPanel extends AbstractReportPanel<AbstractAGPOSectionPanel.Filter> {

    public static class Filter implements Serializable {
        private NamedDateRange dateRange;

        public NamedDateRange getDateRange() {
            return dateRange;
        }

        public void setDateRange(NamedDateRange dateRange) {
            this.dateRange = dateRange;
        }
    }

    @SpringBean
    private FiscalYearService fiscalYearService;

    public AbstractAGPOSectionPanel(String id, AjaxFormListener ajaxFormListener) {
        super(id, ajaxFormListener, Model.of(new Filter()));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        Select2ChoiceBootstrapFormComponent<NamedDateRange> dateRange =
                new Select2ChoiceBootstrapFormComponent<>("dateRange",
                        new GenericChoiceProvider<>(fiscalYearService.createSixMonthDateRangesForAllFiscalYears()));
        getDataExportForm().add(dateRange);
    }
}
