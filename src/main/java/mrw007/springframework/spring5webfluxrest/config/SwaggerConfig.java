package mrw007.springframework.spring5webfluxrest.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        Server localServer = new Server();
        localServer.setDescription("local");
        localServer.setUrl("http://localhost:8080");

        OpenAPI openAPI = new OpenAPI();
        openAPI.info(new Info()
                .title("Spring 5 Flux Rest API")
                .description(
                        "Documenting Spring Boot Reactive REST API with SpringDoc and OpenAPI 3 spec")
                .version("1.0.0")
                .contact(new Contact().name("Wahib Kerkeni").
                        url("https://github.com/mrw007").email("mr.wahib@gmail.com")));

        openAPI.setServers(Arrays.asList(localServer));
        return openAPI;
    }
}
