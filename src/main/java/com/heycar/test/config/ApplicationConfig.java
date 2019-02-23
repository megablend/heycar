/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heycar.test.config;

import javax.servlet.MultipartConfigElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.DispatcherServlet;
import static springfox.documentation.builders.PathSelectors.any;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
@Configuration
@EnableConfigurationProperties(ApplicationProperties.class)
@EnableSwagger2
@EnableTransactionManagement
@EnableAsync
public class ApplicationConfig {
    
    @Autowired
    private ApplicationProperties appProperties;
    
    @Bean
    public ServletRegistrationBean dispatcherRegistration(DispatcherServlet dispatcherServlet) {
        ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet);
        registration.addUrlMappings("/*");
        
        registration.setMultipartConfig(new MultipartConfigElement("", 2097152, 4194304, 0));
        registration.setAsyncSupported(true);
        return registration;
    }
    
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage(appProperties.getSwaggerBasePackage()))
                .paths(any()).build().apiInfo(new ApiInfo(appProperties.getSwaggerDocTitle(), appProperties.getSwaggerDocDescription(), 
                        appProperties.getSwaggerDocVersion(), null, new Contact(appProperties.getDeveloperName(), appProperties.getDeveloperUrl(), appProperties.getDeveloperEmail()), 
                        null, null));
    }
}
