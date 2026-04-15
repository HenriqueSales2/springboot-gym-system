package br.com.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Startup {

	public static void main(String[] args) {
		SpringApplication.run(Startup.class, args);
		// LEMBRE-SE DE ALTERAR O USER E PASSWORD NO APPLICATION.YML
		// SE NÃO A APLICAÇÃO QUEBRA
	}
}
