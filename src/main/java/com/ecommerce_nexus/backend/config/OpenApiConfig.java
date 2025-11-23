package com.ecommerce_nexus.backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI/Swagger configuration for documenting all REST APIs.
 *
 * This uses springdoc-openapi to automatically generate the OpenAPI 3 spec
 * and expose Swagger UI at /swagger-ui.html and the raw spec at /v3/api-docs.
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Ecommerce Nexus API",
                version = "1.0.0",
                description = "OpenAPI documentation for all backend endpoints",
                contact = @Contact(name = "Ecommerce Nexus", email = "support@enexus.com")
        ),
        servers = {
                @Server(url = "/", description = "Default Server")
        },
        security = {
                @SecurityRequirement(name = "bearerAuth")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
    // No explicit beans required for basic setup; annotations above configure the spec.
}
