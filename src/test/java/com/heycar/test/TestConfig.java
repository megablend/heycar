/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heycar.test;

import javax.servlet.ServletContext;
import javax.validation.Validator;
import static org.mockito.Mockito.mock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.DispatcherServlet;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
@Configuration
public class TestConfig {
    
    @Bean
    public Validator localValidatorFactoryBean() {
       return new LocalValidatorFactoryBean();
    }
    
    @Bean
    public ServletContext servletContext() {
        return mock(ServletContext.class);
    }
    
    @Bean
    public DispatcherServlet dispatcherServlet() {
        return mock(DispatcherServlet.class);
    }
}
