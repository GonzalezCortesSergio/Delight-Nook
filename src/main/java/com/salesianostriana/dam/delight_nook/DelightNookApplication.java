package com.salesianostriana.dam.delight_nook;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				description = "Api para administrar la gestión de la tienda Delight-Nook",
				title = "Delight-Nook",
				version = "1.0",
				contact = @Contact(
						email = "gonzalez.coser24@triana.salesianos.edu",
						name = "Sergio González Cortés"
				)
		)
)
public class DelightNookApplication {

	public static void main(String[] args) {
		SpringApplication.run(DelightNookApplication.class, args);
	}

}
