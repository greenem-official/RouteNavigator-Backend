package org.daylight.routenavigator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * The main class of the application
 */
@SpringBootApplication
public class MarketplaceApplication {
	private static ConfigurableApplicationContext context;

	public static void main(String[] args) {
		context = SpringApplication.run(MarketplaceApplication.class, args);
	}
}
