package org.deepforest.dcinside.configuration

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .components(
                Components().addSecuritySchemes(
                    "bearerAuth",
                    SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .`in`(SecurityScheme.In.HEADER)
                        .name("Authorization")
                        .scheme("bearer")
                        .bearerFormat("JWT")
                )
            )
            .security(
                listOf(
                    SecurityRequirement().addList("bearerAuth")
                )
            )
            .info(
                Info().title("Dcinside API")
                    .version("1.0.0")
                    .description("Dcinside API Docs 페이지입니다.")
            )
    }
}