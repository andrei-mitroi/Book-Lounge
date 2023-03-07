package com.licenta.bookLounge.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class BookLoungeApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookLoungeApplication.class, args);
	}

}
