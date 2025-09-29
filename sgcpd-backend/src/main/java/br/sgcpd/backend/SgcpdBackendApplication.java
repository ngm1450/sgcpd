package br.sgcpd.backend;

import br.sgcpd.backend.config.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class SgcpdBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SgcpdBackendApplication.class, args);
	}

}
