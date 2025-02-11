package org.devgateway.ocds.web.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.devgateway.ocds.web.spring.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by mpostelnicu on 16-May-17.
 */
@RestController
public class TranslationController {

    @Autowired
    private TranslationService translationService;

    @Operation(summary = "Returns a json with the merged translations, based on language specified")
    @RequestMapping(value = "/api/translations/{language}", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Map<String, String> translations(final HttpServletResponse response,
                                            @PathVariable String language) throws IOException {
        return translationService.getAllTranslationsForLanguage(language);
    }

}
