package com.leocode.the_java_academy_bank;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "The Java Academy Bank App",
				description = "Backend REST APIs for TJA Bank",
				version = "v1.0",
				contact = @Contact(
						name = "Lionel KINGUE",
						email = "meuguiwarano2014@gmail.com",
						url = "https://github.com/MaitreGIMS75"
				),
				license = @License(
						name = "The Java Academy",
						url = "https://github.com/MaitreGIMS75"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "The Java Academy Bank Application",
				url = "https://github.com/MaitreGIMS75"
		)
)
public class TheJavaAcademyBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(TheJavaAcademyBankApplication.class, args);
	}

}
