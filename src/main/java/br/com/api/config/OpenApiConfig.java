package br.com.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API PromoCode")
                        .version("v1")
                        .description("API para gerenciamento de PromoCodes.")
                        .termsOfService("http://localhost:8080/termsOfService")
                        .license(new License().name("Apache 2.0")
                                .url("http://localhost:8080/license")));
    }

    //http://localhost:8080/v3/api-docs
    //http://localhost:8080/swagger-ui/index.html
}
