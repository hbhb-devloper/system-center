package com.hbhb.cw.systemcenter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * @author xiaokang
 * @since 2020-10-12
 */
@Configuration
public class SwaggerConfig {

    @Value("${springdoc.title}")
    private String title;
    @Value("${springdoc.version}")
    private String version;
    @Value("${springdoc.description}")
    private String description;

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearer-key",
                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
                .info(new Info()
                        .title(title)
                        .version(version)
                        .description(description));
    }
}
