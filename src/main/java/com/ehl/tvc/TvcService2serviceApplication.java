package com.ehl.tvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class TvcService2serviceApplication {
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(TvcService2serviceApplication.class, args);
	}
	
}
