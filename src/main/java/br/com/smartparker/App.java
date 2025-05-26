package br.com.smartparker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@EnableCaching
@OpenAPIDefinition(info = @Info(title = "Sistema de motos SmartParker", description = "CRUD da SmartParker com métodos pensados em facilitar o gerenciamento de veículos nos pátios."))
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

}
