package org.devgateway.toolkit.web.rest.controller.offline;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.devgateway.toolkit.persistence.service.category.MetadataExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class MetadataExportController {

    @Autowired
    private MetadataExportService metadataExportService;

    @RequestMapping(value = "/api/metadataExport",
            method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json")
    @ResponseBody
    public Map<String, List<ImmutablePair<Long, String>>> metadataExport() {
        return metadataExportService.getMetadataMap();
    }
}
