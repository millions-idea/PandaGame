package com.panda.game.management;

import com.panda.game.management.config.WebLogAspectConfiguration;
import com.panda.game.management.entity.Constant;
import com.panda.game.management.entity.DynamicConfiguration;
import com.panda.game.management.utils.SpringApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication
@EnableCaching
@EnableAspectJAutoProxy
@EnableAsync
public class ManagementApplication extends SpringBootServletInitializer {
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ManagementApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(ManagementApplication.class, args);
		System.out.println("Server start successful!");
	}

	@Bean
	public WebLogAspectConfiguration geWebLogAspectConfiguration(){
		return new WebLogAspectConfiguration(true);
	}

	@Bean
	public SpringApplicationContext getSpringApplicationContext() {
		return new SpringApplicationContext();
	}

	@Bean
	public MultipartConfigElement multipartConfigElement(){
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setMaxFileSize("5MB");
		factory.setMaxRequestSize("5MB");
		return factory.createMultipartConfig();
	}
}
