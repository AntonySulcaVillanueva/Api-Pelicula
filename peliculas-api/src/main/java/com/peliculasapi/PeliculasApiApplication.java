package com.peliculasapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@ComponentScan(basePackages = {"com.peliculasapi", "com.external.config"})
@SpringBootApplication
public class PeliculasApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PeliculasApiApplication.class, args);
	}

}
