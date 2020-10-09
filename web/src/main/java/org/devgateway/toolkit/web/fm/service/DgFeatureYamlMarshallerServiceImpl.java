package org.devgateway.toolkit.web.fm.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.devgateway.toolkit.web.fm.entity.DgFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

@Service
public class DgFeatureYamlMarshallerServiceImpl implements DgFeatureYamlMarshallerService {

    private static final Logger logger = LoggerFactory.getLogger(DgFeatureYamlMarshallerServiceImpl.class);

    @Autowired
    @Qualifier("yamlObjectMapper")
    private ObjectMapper yamlObjectMapper;

    @Override
    public void marshall(String resourceLocation, Collection<DgFeature> dgFeatures) {
        try {
            File file = new File(resourceLocation);
            logger.debug(String.format("FM: Writing projected features to file %s", file.getAbsolutePath()));
            OutputStream output = new FileOutputStream(file);
            yamlObjectMapper.writeValue(output, dgFeatures);
            output.close();
            logger.debug(String.format("FM: Written projected features to file %s", file.getAbsolutePath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
