package org.devgateway.toolkit.web.rest.controller.offline;

import org.devgateway.toolkit.persistence.dao.Person;
import org.devgateway.toolkit.persistence.dao.form.PMCReport;
import org.devgateway.toolkit.persistence.dto.PMCReportOffline;
import org.devgateway.toolkit.persistence.service.PersonService;
import org.devgateway.toolkit.persistence.service.category.PMCReportOfflineService;
import org.devgateway.toolkit.persistence.service.form.PMCReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.devgateway.toolkit.persistence.dao.DBConstants.Status.DRAFT;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@Validated
public class PMCReportController {

    @Autowired
    private PMCReportOfflineService reportOfflineService;

    @Autowired
    private PMCReportService pmcReportService;

    @Autowired
    private PersonService personService;

    @RequestMapping(value = "/api/pmcReport/list/{userId}",
            method = {RequestMethod.POST, RequestMethod.GET}, produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<PMCReportOffline> pmcReportList(@PathVariable Long userId) {
        return reportOfflineService.getPMCReports(userId);
    }

    @RequestMapping(value = "/api/pmcReport/update/{userId}",
            method = {RequestMethod.POST}, produces = APPLICATION_JSON_UTF8_VALUE,
            consumes = APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public PMCReportOffline updatePMCReports(@PathVariable Long userId,
                                             @RequestBody PMCReportOffline pmcReport) {
        Person person = reportOfflineService.loadPersonById(userId, personService);
        PMCReport ret;
        if (DRAFT.equals(pmcReport.getStatus())) {
            ret = reportOfflineService.convertToDaoDraft(pmcReport, person);
        } else {
            ret = reportOfflineService.convertToDaoNonDraft(pmcReport, person);
        }
        if (!person.getDepartments().contains(ret.getDepartment())) {
            throw new RuntimeException("User cannot add report to " + ret.getDepartment() + " department");
        }
        return reportOfflineService.convertToOffline(pmcReportService.saveReportAndUpdateTenderProcess(ret));
    }

}
