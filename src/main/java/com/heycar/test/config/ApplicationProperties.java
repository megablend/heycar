/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heycar.test.config;

import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
@ConfigurationProperties(prefix = "heycar")
@Data
@Validated
public class ApplicationProperties {
    
    /**
     * Swagger base package
     * 
     */
    @NotNull
    private String swaggerBasePackage = "com.heycar.test.controllers";
    
    /**
     * Swagger Document Title
     * 
     */
    @NotNull
    private String swaggerDocTitle = "Hey Car Backend Challenge";
    
    /**
     * Swagger Document Description
     * 
     */
    @NotNull
    private String swaggerDocDescription = "This application manages the upload of dealers listing for hey car";
    
    /**
     * Swagger Document Version
     * 
     */
    @NotNull
    private String swaggerDocVersion = "1.0.0";
    
    /**
     * The name of the developer
     */
    @NotNull
    private String developerName = "Charles Megafu";
    
    /**
     * The Profile of the developer
     * 
     */
    @NotNull
    private String developerUrl = "https://www.linkedin.com/in/charles-megafu-295a2768/";
    
    /**
     * The email of the developer
     * 
     */
    @NotNull
    private String developerEmail = "noniboycharsy@gmail.com";
    
    /**
     * Active MQ Broker URL
     */
    @NotBlank
    private String activemqBrokerUrl = "vm://localhost";
    
    /**
     * Active MQ Consumer Thread Pool
     * 
     */
    @NotBlank
    private String activemqConsumerThreadPool = "2-100";
    
    /**
     * ActiveMQ Delivery Retrial
     * 
     */
    private int activemqMessageDeliveryRetrial = 1;
    
    /**
     * The Active MQ topic to handle the processing of hits
     */
    @NotBlank
    private String activemqSaveListingTopic = "dealersListing";
}
