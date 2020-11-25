package org.devgateway.toolkit.persistence.fm.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.devgateway.toolkit.persistence.fm.entity.FeatureConfig;
import org.devgateway.toolkit.persistence.fm.entity.UnchainedDgFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author mpostelnicu
 */
@Service
@Validated
public class DgFeatureYamlUnmarshallerServiceImpl implements DgFeatureYamlUnmarshallerService {

    private static final Logger logger = LoggerFactory.getLogger(DgFeatureYamlUnmarshallerServiceImpl.class);

    @Autowired
    @Qualifier("yamlObjectMapper")
    private ObjectMapper yamlObjectMapper;

    public String createHash(UnchainedDgFeature feature) {
        try {
            return DigestUtils.md5Hex(yamlObjectMapper.writeValueAsString(feature));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Validated
    public UnchainedDgFeature validateUnchainedDgFeature(@Valid UnchainedDgFeature unchainedDgFeature) {
        return unchainedDgFeature;
    }

    @Override
    public List<UnchainedDgFeature> unmarshall(FeatureConfig featureConfig) {
        String resourceLocation = featureConfig.getResourceLocation();
        logger.debug(String.format("FM: Unmarshalling resource location %s", resourceLocation));
        try {
            List<UnchainedDgFeature> features = yamlObjectMapper.readValue(featureConfig.getContent(),
                    yamlObjectMapper.getTypeFactory().constructCollectionType(List.class, UnchainedDgFeature.class));
            List<UnchainedDgFeature> ret = features.stream().peek(f -> {
                f.setResourceLocation(resourceLocation);
                f.setHash(createHash(f));
            }).map(this::validateUnchainedDgFeature).collect(Collectors.toList());
            logger.debug(String.format("FM: Unmarshalled resource location %s", resourceLocation));
            return ret;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
