package com.stock.inventorymanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.stock.inventorymanagement.domain")
@ComponentScan(basePackages = "com.stock.inventorymanagement.repository,com.stock.inventorymanagement.config")
public class StockKeeperApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockKeeperApplication.class, args);
	}

}
