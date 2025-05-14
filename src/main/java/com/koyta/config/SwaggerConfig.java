package com.koyta.config;

import java.util.List;
import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ch.qos.logback.core.subst.Token.Type;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme.In;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI() {

		OpenAPI openAPI = new OpenAPI();

		Info info = new Info();
		info.setTitle("Enotes API");
		info.setDescription("Enotes Api");
		info.setVersion("1.0.0");
		info.setTermsOfService("http://koytaindustries.com");
		info.setContact(new Contact().name("Abhishek Narkhede").url("http://koytaindustries.com").email("abhisheknarkharkhede083@gmail.com"));
		info.setLicense(new License().name("Enotes 1.0").url("http://koytaindustries.com"));

		List<Server> serversList = Arrays.asList(new Server().description("Dev").url("http://localhost:8080"),
				new Server().description("Test").url("http://localhost:8081"),
				new Server().description("Prod").url("http://localhost:8082"),
				new Server().description("uat").url("http://localhost:8083")
				);

		// bearer kjdfkkjdfnksdfjnkdfnjfdkjnsfd

		io.swagger.v3.oas.models.security.SecurityScheme securityScheme = new io.swagger.v3.oas.models.security.SecurityScheme();

		securityScheme.name("Auhorization").scheme("bearer")
				.type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP).bearerFormat("JWT").in(In.HEADER);

		Components components = new Components().addSecuritySchemes("Token", securityScheme);

		openAPI.setInfo(info);
		openAPI.setServers(serversList);
		openAPI.setSecurity(List.of(new SecurityRequirement().addList("Token")));
		openAPI.setComponents(components);

		return openAPI;
	}

}
