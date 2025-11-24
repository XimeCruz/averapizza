package com.xime.averapizza.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "A Vera Pizza Italia - Backend API",
                version = "1.0",
                description = "API del sistema automatizado para la pizzería A Vera Pizza Italia.",
                contact = @Contact(
                        name = "Ximena Cruz",
                        email = "ximena@averapizza.com",
                        url = "https://averapizza.com"
                )
        ),
        servers = {
                @Server(
                        url = "https://averapizza.servernux.com/api",
                        description = "Servidor de producción"
                ),
                @Server(
                        url = "http://localhost:8089/api",
                        description = "Localhost"
                )
        }

)
@SecurityScheme(
        name = "bearerAuth",
        type = io.swagger.v3.oas.annotations.enums.SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {

}

