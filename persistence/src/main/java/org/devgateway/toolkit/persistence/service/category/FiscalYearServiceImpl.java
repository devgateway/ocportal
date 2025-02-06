package org.devgateway.toolkit.persistence.service.category;

import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear_;
import org.devgateway.toolkit.persistence.dto.NamedDateRange;
import org.devgateway.toolkit.persistence.repository.category.FiscalYearRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.service.BaseJpaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.metamodel.SingularAttribute;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author mpostelnicu
 */
@Service
@Transactional(readOnly = true)
public class FiscalYearServiceImpl extends BaseJpaServiceImpl<FiscalYear> implements FiscalYearService {
    @Autowired
    private FiscalYearRepository fiscalYearRepository;

    @Override
    protected BaseJpaRepository<FiscalYear, Long> repository() {
        return fiscalYearRepository;
    }

    @Override
    public FiscalYear newInstance() {
        return new FiscalYear();
    }

    public List<FiscalYear> getAll() {
        return findAll();
    }

    @Override
    public FiscalYear findByName(final String name) {
        return fiscalYearRepository.findByName(name);
    }



    @Override
    public List<NamedDateRange> createSixMonthDateRangesForAllFiscalYears() {
        List<FiscalYear> fiscalYears = fiscalYearRepository.findAll();
        List<NamedDateRange> dateRanges = new ArrayList<>();

        fiscalYears.forEach(fiscalYear -> {
            Calendar calendar = Calendar.getInstance();
            NamedDateRange ndr1 = new NamedDateRange();
            ndr1.setStartDate(fiscalYear.getStartDate());
            calendar.setTime(fiscalYear.getStartDate());
            calendar.add(Calendar.MONTH, 5);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            ndr1.setEndDate(calendar.getTime());
            dateRanges.add(ndr1);

            NamedDateRange ndr2 = new NamedDateRange();
            ndr2.setEndDate(fiscalYear.getEndDate());
            calendar.setTime(fiscalYear.getEndDate());
            calendar.add(Calendar.MONTH, -5);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            ndr2.setStartDate(calendar.getTime());
            ndr2.setSecondInterval(true);
            ndr2.setFiscalYearStartDate(fiscalYear.getStartDate());
            ndr2.setFiscalYearEndDate(fiscalYear.getEndDate());
            dateRanges.add(ndr2);
        });
        return dateRanges;
    }

    @Override
    public FiscalYear getLastFiscalYear() {
        return getYearsWithData().isEmpty() ? null : getYearsWithData().get(0);
    }

    @Override
    public List<FiscalYear> getYearsWithData() {
        return fiscalYearRepository.getYearsWithData();
    }

    @Override
    public SingularAttribute<? super FiscalYear, String> getTextAttribute() {
        return FiscalYear_.name;
    }
}
