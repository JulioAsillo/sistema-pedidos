package com.zv.pedidos_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class PedidosServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PedidosServiceApplication.class, args);
	}

}
