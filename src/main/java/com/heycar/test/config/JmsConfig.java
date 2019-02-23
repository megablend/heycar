/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heycar.test.config;

import javax.jms.ConnectionFactory;
import javax.jms.Session;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
@Configuration
@EnableJms
public class JmsConfig {
    
    @Autowired
    private ApplicationProperties properties;
    
    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(properties.getActivemqBrokerUrl());
        connectionFactory.setTrustAllPackages(true);
        return connectionFactory;
    }
    
    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate((ConnectionFactory)this.connectionFactory());
        jmsTemplate.setDefaultDestinationName(properties.getActivemqSaveListingTopic());
        jmsTemplate.setDeliveryMode(1);
        jmsTemplate.setExplicitQosEnabled(true);
        jmsTemplate.setDeliveryPersistent(false);
        jmsTemplate.setSessionAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);
        return jmsTemplate;
    }
    
    @Bean
    public DefaultJmsListenerContainerFactory containerFactory() {
        DefaultJmsListenerContainerFactory containerFactory = new DefaultJmsListenerContainerFactory();
        containerFactory.setConnectionFactory(connectionFactory());
        containerFactory.setConcurrency(properties.getActivemqConsumerThreadPool());
        return containerFactory;
    }
}
