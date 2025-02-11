package org.devgateway.toolkit.persistence.service.category;

import com.google.common.collect.ImmutableMap;
import org.devgateway.toolkit.persistence.dao.AbstractAuditableEntity;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.Labelable;
import org.devgateway.toolkit.persistence.dao.Person;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.Designation;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.categories.PMCStaff;
import org.devgateway.toolkit.persistence.dao.categories.PMCStatus;
import org.devgateway.toolkit.persistence.dao.categories.ProjectClosureHandover;
import org.devgateway.toolkit.persistence.dao.categories.Subcounty;
import org.devgateway.toolkit.persistence.dao.categories.Ward;
import org.devgateway.toolkit.persistence.dao.form.Contract_;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan_;
import org.devgateway.toolkit.persistence.dao.form.Tender;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess_;
import org.devgateway.toolkit.persistence.dao.form.Tender_;
import org.devgateway.toolkit.persistence.service.BaseJpaService;
import org.devgateway.toolkit.persistence.service.PersonService;
import org.devgateway.toolkit.persistence.service.form.TenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Join;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
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

    @Autowired
    private PMCStatusService pmcStatusService;

    @Autowired
    private PersonService personService;

    protected static <C extends AbstractAuditableEntity & Labelable> ImmutableMap<String, Serializable>
    convertLabelableToMap(C c) {
        return ImmutableMap.of("id", c.getId(), "label", c.getLabel());
    }

    protected static ImmutableMap<String, Serializable>
    convertTenderToMap(Tender t) {
        return ImmutableMap.of("id", t.getId(), "fyId", t.getProcurementPlan().getFiscalYear().getId(),
                "deptId", t.getDepartment().getId(), "label", t.getLabel());
    }

    protected static ImmutableMap<String, Serializable>
    convertWardToMap(Ward w) {
        return ImmutableMap.of("id", w.getId(), "parentId", w.getSubcounty().getId(), "label", w.getLabel());
    }

    protected <C extends AbstractAuditableEntity> List<C> getMetadataList(BaseJpaService<C> service) {
        return service.findAll(Sort.by("id"));
    }

    protected <C extends Tender> List<C> getTenderList(BaseJpaService<C> service, Collection<Department> deps) {
        return service.findAll((r, cq, cb) -> {
            Join<C, TenderProcess> tpJoin = r.join(Tender_.tenderProcess);
            return cb.and(
                    tpJoin.join(TenderProcess_.procurementPlan).join(ProcurementPlan_.department).in(deps),
                    cb.equal(tpJoin.join(TenderProcess_.contract).get(Contract_.STATUS), DBConstants.Status.APPROVED),
                    cb.equal(r.get(Tender_.STATUS), DBConstants.Status.APPROVED));
        }, Sort.by("id"));
    }

    protected <C extends AbstractAuditableEntity & Labelable> void convertAndAddPairListToMap(
            Map<String, List<Serializable>> ret, BaseJpaService<C> service, Class<C> clazz) {
        convertAndAddPairListToMap(ret, service, clazz, this::getMetadataList,
                MetadataExportServiceImpl::convertLabelableToMap);
    }


    protected <C extends AbstractAuditableEntity & Labelable> void convertAndAddPairListToMap(
            Map<String, List<Serializable>> ret, BaseJpaService<C> service, Class<C> clazz,
            Function<C, Serializable> serialize) {
        convertAndAddPairListToMap(ret, service, clazz, this::getMetadataList, serialize);
    }

    protected <C extends AbstractAuditableEntity & Labelable> void convertAndAddPairListToMap(
            Map<String, List<Serializable>> ret, BaseJpaService<C> service, Class<C> clazz,
            Function<BaseJpaService<C>, Collection<C>> getList, Function<C, Serializable> serialize) {
        ret.put(clazz.getSimpleName(),
                getList.apply(service).stream().map(serialize).collect(Collectors.toList()));
    }

    protected <C extends AbstractAuditableEntity & Labelable> void convertAndAddPairListToMap(
            Map<String, List<Serializable>> ret, Class<C> clazz,
            Collection<C> list, Function<C, Serializable> serialize) {
        ret.put(clazz.getSimpleName(), list.stream().map(serialize).collect(Collectors.toList()));
    }

    @Override
    public Map<String, List<Serializable>> getMetadataMap(Long userId) {
        Optional<Person> user = personService.findById(userId);
        if (!user.isPresent()) {
            throw new RuntimeException("Unknown user id");
        }

        Map<String, List<Serializable>> ret = new HashMap<>();
        convertAndAddPairListToMap(ret, pmcStaffService, PMCStaff.class);
        convertAndAddPairListToMap(ret, pmcStatusService, PMCStatus.class);
        convertAndAddPairListToMap(ret, projectClosureHandoverService, ProjectClosureHandover.class);
        convertAndAddPairListToMap(ret, designationService, Designation.class);
        convertAndAddPairListToMap(ret, subcountyService, Subcounty.class);
        convertAndAddPairListToMap(ret, wardService, Ward.class, MetadataExportServiceImpl::convertWardToMap);
        convertAndAddPairListToMap(ret, Department.class, user.get().getDepartments(),
                MetadataExportServiceImpl::convertLabelableToMap);

        List<Tender> tenderList = this.getTenderList(tenderService, user.get().getDepartments());
        convertAndAddPairListToMap(ret, FiscalYear.class, tenderList.stream()
                .map(Tender::getProcurementPlan).map(ProcurementPlan::getFiscalYear)
                .collect(Collectors.toSet()), MetadataExportServiceImpl::convertLabelableToMap);
        convertAndAddPairListToMap(ret, Tender.class, tenderList, MetadataExportServiceImpl::convertTenderToMap);
        return ret;
    }
}
