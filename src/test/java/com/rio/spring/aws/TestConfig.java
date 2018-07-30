package com.rio.spring.aws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.core.env.Environment;

import com.rio.spring.aws.EmailService;
import com.rio.spring.aws.SMSService;


@TestConfiguration
@EnableSpringConfigured
public class TestConfig
{
    @Autowired
    private Environment             env;
    
        
    @Autowired
    private ApplicationContext      ctx;
 
    @Bean
    @Primary
    public SMSService getDummySMSService()
    {
        return new DummySmsService();
    }
    
    @Bean
    @Primary
    public EmailService getDummyEmailService()
    {
        return new DummyEmailService();
    }
}
