package me.zhulin.shopapi;

//Spring Boot SpringApplication class is used to bootstrap and launch a Spring application from a Java main method.
import org.springframework.boot.SpringApplication;

//annotation is used to mark a configuration class that declares one or more @Bean methods and also triggers auto-configuration and component scanning
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;

//Implementation of PasswordEncoder that uses the BCrypt strong hashing function.
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//For encoding passwords.Verifies the encoded password obtained from storage and matches with the submitted
import org.springframework.security.crypto.password.PasswordEncoder;

//A PasswordEncoder implementation that uses PBKDF2 
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

//For enabling cross-origin request handling for the specified path
import org.springframework.web.servlet.config.annotation.CorsRegistry;

//Annotation-based setup for Spring MVC.
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//For implementation of WebMvcConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
public class ShopApiApplication {
	
	//defining the simple BCryptPasswordEncoder as a bean in configuration.
	//be aware that the BCrypt algorithm generates a String of length 60, so we need to make sure that the password will be stored in a column that can accommodate it. A common mistake is to create a column of a different length and then get an Invalid Username or Password error at authentication time.
	
	
	//https://www.baeldung.com/spring-security-registration-password-encoding-bcrypt
	//https://github.com/Baeldung/spring-security-registration
    @Bean
    public PasswordEncoder passwordEncoder() {
    	//Password encoding mechanism by Spring Security.
        return new BCryptPasswordEncoder();
    }

    
    //Enabling Cross Origin Requests for RESTful Web Service
    //https://spring.io/guides/gs/rest-service-cors/
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
            	
            	//Enabling CORS for the whole application
            	//https://spring.io/blog/2015/06/08/cors-support-in-spring-framework
                registry.addMapping("/**");
            }
        };
    }

    public static void main(String[] args) {
    	//Class that can be used to bootstrap and launch the application from main method.
        SpringApplication.run(ShopApiApplication.class, args);
    }

}

