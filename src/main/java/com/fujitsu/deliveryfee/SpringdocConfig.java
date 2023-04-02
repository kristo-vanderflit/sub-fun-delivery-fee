package com.fujitsu.deliveryfee;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition
@Configuration
public class SpringdocConfig {

    /**
     * REST API documentation: http://localhost:8080/swagger-ui/index.html#/
     */
    @Bean
    public OpenAPI baseOpenAPI() {
        return new OpenAPI().info(new Info()
                        .title("Delivery fee")
                        .version("1.0.0")
                        .description("A sub-functionality of the food delivery application " +
                                "calculates the delivery fee for food couriers based on regional base fee," +
                                " vehicle type, and weather conditions."));
    }

}
