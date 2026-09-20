package com.northstar.portfolio;

import com.northstar.portfolio.config.DotenvLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PortfolioApplication {

	public static void main(String[] args) {
		DotenvLoader.load();
		SpringApplication.run(PortfolioApplication.class, args);
	}
}