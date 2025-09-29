package br.uniesquina.backend;

import br.uniesquina.backend.config.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class UniesquinaBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(UniesquinaBackendApplication.class, args);
	}

}
