package ru.stockmann.BonusStorage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class BonusStorageApplication {


	public static void main(String[] args) {
		SpringApplication.run(BonusStorageApplication.class, args);
/*		SpringApplication application = new SpringApplication(BonusStorageApplication.class);
		application.setWebApplicationType(WebApplicationType.NONE);
		application.run(args);*/
	}
}
