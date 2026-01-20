package com.flow.fileExtension.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI FileExtensionAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("File Extension API")
                        .description("마드라스체크 채용 과제")
                        .version("v1.0.0"));
    }
}