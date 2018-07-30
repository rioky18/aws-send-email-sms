package com.rio.spring.aws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;

@SpringBootApplication
@EnableSpringConfigured
public class Application
{
    public static void main( final String[] args )
    {
        SpringApplication.run( Application.class, args );
    }
}
