package com.northstar.portfolio.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI portfolioOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Portfolio API")
                        .description("API backend du portfolio Northstar.")
                        .version("v1"));
    }
}