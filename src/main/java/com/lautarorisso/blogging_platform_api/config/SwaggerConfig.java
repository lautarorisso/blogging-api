package com.lautarorisso.blogging_platform_api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(info = @Info(title = "Blogging Platform API", description = "REST API for managing blog posts, including authentication, pagination and role-based access control", version = "1.0.0"), servers = {
        @Server(description = "DEV SERVER", url = "http://localhost:8080") }, security = @SecurityRequirement(name = "Security Token"))
@SecurityScheme(name = "Security Token", description = "Access Token for my API", type = SecuritySchemeType.HTTP, paramName = "Authorization", in = SecuritySchemeIn.HEADER, scheme = "bearer", bearerFormat = "JWT")
public class SwaggerConfig {
}
