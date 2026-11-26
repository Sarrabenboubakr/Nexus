
package tn.esprit.autoloc.collaborationservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Collaboration Service API",
                version = "1.0",
                description = "API de gestion des discussions, messages et mentions"
        )
)
public class OpenApiConfig {
}
