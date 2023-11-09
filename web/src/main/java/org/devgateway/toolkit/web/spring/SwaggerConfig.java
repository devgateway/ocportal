package org.devgateway.toolkit.web.spring;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnProperty("apiDocs")
@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi ocDashboardsApi() {

        return GroupedOpenApi.builder()
                .group("1ocDashboardsApi")
                .pathsToMatch("/api/.*")
                .build();
    }

    @Bean
    public GroupedOpenApi manageApi() {
        return GroupedOpenApi.builder()
                .group("2manageApi")
                .pathsToMatch("/manage/.*")
                .build();
    }

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Open Contracting Dashboards API")
                        .description("These endpoints are used to feed the open contracting dashboard")
                        .version("1.0")
                        .license(new License().name("MIT License").url("https://opensource.org/licenses/MIT")))
                .externalDocs(new ExternalDocumentation()
                        .description("SpringShop Wiki Documentation")
                        .url("https://springshop.wiki.github.org/docs"));
    }

}