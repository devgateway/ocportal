package org.devgateway.toolkit.web.rest.controller.offline;

import org.devgateway.toolkit.persistence.dto.PMCReportOffline;
import org.devgateway.toolkit.persistence.service.category.PMCReportOfflineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
public class PMCReportController {

    @Autowired
    private PMCReportOfflineService reportOfflineService;

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
    public List<PMCReportOffline> updatePMCReports(@PathVariable Long userId,
                                                   @RequestBody List<PMCReportOffline> pmcReportList) {
        return reportOfflineService.updatePMCReports(userId, pmcReportList);
    }

}
