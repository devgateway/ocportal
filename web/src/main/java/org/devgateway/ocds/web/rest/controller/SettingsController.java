package org.devgateway.ocds.web.rest.controller;

import org.devgateway.ocds.web.util.SettingsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Created by mpostelnicu
 */
@RestController
public class SettingsController {

    @Autowired
    private SettingsUtils settingsUtils;

    @RequestMapping(value = "/api/gaId", method = {RequestMethod.GET, RequestMethod.POST},
    produces = APPLICATION_JSON_VALUE)
    public Map<String, String> gaId() {
        return Collections.singletonMap("response", settingsUtils.getGoogleAnalyticsTrackingId());
    }

}
