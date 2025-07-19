package br.com.dagostini.infrasystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "br.com.dagostini")
@ComponentScan(basePackages = "br.com.dagostini")
@EntityScan(basePackages="br.com.dagostini")
@SpringBootApplication(scanBasePackages = "br.com.dagostini")
public class InfrasystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(InfrasystemApplication.class, args);
	}

}
