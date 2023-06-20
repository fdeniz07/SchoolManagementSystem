package com.schoolmanagement.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "StudentManagement API", version = "1.0.0"),//-->Swagger ile ilgili tüm döküman islemlerini icin gerekli islemleri hazirliyor
        security =@SecurityRequirement(name = "Bearer"))
@SecurityScheme(name = "Bearer", type = SecuritySchemeType.HTTP, scheme ="Bearer")
public class OpenAPIConfig {


}
