package org.devgateway.toolkit.web.fm.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.devgateway.toolkit.web.fm.entity.UnchainedDgFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class DgFeatureYamlUnmarshallerServiceImpl implements DgFeatureYamlUnmarshallerService {

    private static final Logger logger = LoggerFactory.getLogger(DgFeatureYamlUnmarshallerServiceImpl.class);

    @Autowired
    @Qualifier("yamlObjectMapper")
    private ObjectMapper yamlObjectMapper;

    @Value("#{'${fm.resources}'.split(',')}")
    private List<String> resources;

    @Override
    public List<String> getResources() {
        return resources;
    }

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
    public List<UnchainedDgFeature> unmarshall(String resourceLocation) {
        logger.debug(String.format("FM: Unmarshalling resource location %s", resourceLocation));
        try {
            List<UnchainedDgFeature> features = yamlObjectMapper.readValue(getClass().getClassLoader()
                            .getResourceAsStream(resourceLocation),
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
