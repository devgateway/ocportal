package org.devgateway.toolkit.persistence.service.category;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.devgateway.toolkit.persistence.dao.AbstractAuditableEntity;
import org.devgateway.toolkit.persistence.dao.Labelable;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.Designation;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.categories.PMCStaff;
import org.devgateway.toolkit.persistence.dao.categories.ProjectClosureHandover;
import org.devgateway.toolkit.persistence.dao.categories.Subcounty;
import org.devgateway.toolkit.persistence.dao.categories.Ward;
import org.devgateway.toolkit.persistence.dao.form.Tender;
import org.devgateway.toolkit.persistence.service.BaseJpaService;
import org.devgateway.toolkit.persistence.service.form.TenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@Service
public class MetadataExportServiceImpl implements MetadataExportService {

    @Autowired
    private PMCStaffService pmcStaffService;

    @Autowired
    private ProjectClosureHandoverService projectClosureHandoverService;

    @Autowired
    private DesignationService designationService;

    @Autowired
    private SubcountyService subcountyService;

    @Autowired
    private WardService wardService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private FiscalYearService fiscalYearService;

    @Autowired
    private TenderService tenderService;

    protected static <C extends AbstractAuditableEntity & Labelable> ImmutablePair<Long, String>
    convertLabelableToPair(C c) {
        return ImmutablePair.of(c.getId(), c.getLabel());
    }

    protected <C extends AbstractAuditableEntity> List<C> getMetadataList(BaseJpaService<C> service) {
        return service.findAll(Sort.by("id"));
    }

    protected <C extends AbstractAuditableEntity & Labelable> void convertAndAddPairListToMap(
            Map<String, List<ImmutablePair<Long, String>>> ret, BaseJpaService<C> service, Class<C> clazz) {
        ret.put(clazz.getSimpleName(),
                getMetadataList(service).stream().map(MetadataExportServiceImpl::convertLabelableToPair)
                        .collect(Collectors.toList()));
    }


    @Override
    public Map<String, List<ImmutablePair<Long, String>>> getMetadataMap() {
        Map<String, List<ImmutablePair<Long, String>>> ret = new HashMap<>();
        convertAndAddPairListToMap(ret, pmcStaffService, PMCStaff.class);
        convertAndAddPairListToMap(ret, projectClosureHandoverService, ProjectClosureHandover.class);
        convertAndAddPairListToMap(ret, designationService, Designation.class);
        convertAndAddPairListToMap(ret, subcountyService, Subcounty.class);
        convertAndAddPairListToMap(ret, wardService, Ward.class);
        convertAndAddPairListToMap(ret, departmentService, Department.class);
        convertAndAddPairListToMap(ret, fiscalYearService, FiscalYear.class);
        convertAndAddPairListToMap(ret, tenderService, Tender.class);
        return ret;
    }
}
