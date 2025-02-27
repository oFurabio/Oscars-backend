package com.ofurabio.oscars.configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    OpenAPI springOscarsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Oscars Voting")
                        .description("Projeto Oscars - oFurabio")
                        .version("v0.0.1")
                        .license(new License()
                                .name("Flávio Farias")
                                .url("https://flaviofarias.vercel.app/"))
                        .contact(new Contact()
                                .name("Flávio Farias")
                                .url("https://github.com/oFurabio/")
                                .email("fs.flaviosilv4@gmail.com")))
                .externalDocs(new ExternalDocumentation()
                        .description("Github")
                        .url("https://github.com/oFurabio"));
    }

    @Bean
    OpenApiCustomizer customerGlobalHeaderOpenApiCustomizer() {
        return openApi -> {
            openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations()
                    .forEach(operation -> {
                        ApiResponses apiResponses = operation.getResponses();

                        apiResponses.addApiResponse("200", createApiResponse("Sucesso!"));
                        apiResponses.addApiResponse("201", createApiResponse("Objeto Persistido!"));
                        apiResponses.addApiResponse("204", createApiResponse("Objeto Excluído!"));
                        apiResponses.addApiResponse("400", createApiResponse("Erro na Requisição!"));
                        apiResponses.addApiResponse("401", createApiResponse("Aceeso não Autorizado!"));
                        apiResponses.addApiResponse("403", createApiResponse("Acesso Proibido!"));
                        apiResponses.addApiResponse("404", createApiResponse("Objeto não Encontrado!"));
                        apiResponses.addApiResponse("500", createApiResponse("Erro na Aplicação!"));
                    }));
        };
    }

    private ApiResponse createApiResponse(String message) {
        return new ApiResponse().description(message);
    }
}
