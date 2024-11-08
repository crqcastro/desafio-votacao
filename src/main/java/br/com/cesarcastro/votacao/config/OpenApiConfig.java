package br.com.cesarcastro.votacao.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Configuration
public class OpenApiConfig {
    private final String appName;
    private final String appVersion;
    private final String description;
    private final Environment env;
    private final String appEndpointBase;
    private final Integer port;

    public OpenApiConfig(@Value("${info.app.name}") String appName,
                         @Value("${info.app.version}") String appVersion,
                         @Value("${info.app.description}") String description,
                         Environment env,
                         @Value("${APP_ENDPOINT_BASE}") String appEndpointBase,
                         @Value("${server.port}") Integer port) {
        this.appName = appName;
        this.appVersion = appVersion;
        this.description = description;
        this.env = env;
        this.appEndpointBase = appEndpointBase;
        this.port = port;
    }


    @Bean
    public OpenAPI customOpenAPI() {
        List<Server> servers = new ArrayList();
        final var envLocal = Arrays.asList(env.getActiveProfiles()).contains("local");
        final var envProd = Arrays.asList(env.getActiveProfiles()).contains("prod");

        if (envLocal) {
            Server localServer = new Server()
                    .description("Local Server")
                    .url("http://localhost:" + port + "/");

            servers.add(localServer);
        } else if (envProd) {
            Server productionServer = new Server()
                    .description("Production server (uses live data)")
                    .url(appEndpointBase + "/");

            servers.add(productionServer);
        } else {
            Server developmentServer = new Server()
                    .description("Development Server (uses mock data)")
                    .url(appEndpointBase + "/");
            servers.add(developmentServer);
        }

        return new OpenAPI()
                .info(new Info().title(appName)
                        .description(description)
                        .version(appVersion)
                        .contact(new Contact().name("Cesar Castro").email("cesarrqc@gmail.com"))
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description(description)
                        .url("https://github.com/crqcastro/desafio-votacao"))
                .servers(servers);
    }
}