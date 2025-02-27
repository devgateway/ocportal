package org.devgateway.toolkit.web.spring;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {


    @Bean
    public OpenAPI openContractingAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Open Contracting Dashboards API")
                        .description("API endpoints for accessing and managing data in the Open Contracting system.")
                        .version("3.0.0")
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .externalDocs(new ExternalDocumentation()
                        .description("Open Contracting Data Standard Documentation")
                        .url("https://standard.open-contracting.org/1.1/en/"));
    }

}
