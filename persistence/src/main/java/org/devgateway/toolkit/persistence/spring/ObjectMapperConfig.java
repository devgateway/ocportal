package org.devgateway.toolkit.persistence.spring;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class ObjectMapperConfig {

    @Primary
    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder objectMapperBuilder) {
        return objectMapperBuilder.build();
    }

    @Bean("yamlObjectMapper")
    public ObjectMapper yamlObjectMapper() {
        YAMLFactory factory = new YAMLFactory();
        factory.enable(YAMLGenerator.Feature.MINIMIZE_QUOTES);
        factory.enable(YAMLGenerator.Feature.INDENT_ARRAYS);
        factory.disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER);
        ObjectMapper objectMapper = new ObjectMapper(factory);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        return objectMapper;
    }
}
