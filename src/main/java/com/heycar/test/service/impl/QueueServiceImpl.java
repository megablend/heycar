/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heycar.test.service.impl;

import com.heycar.test.config.ApplicationProperties;
import com.heycar.test.service.QueueService;
import javax.jms.Session;
import javax.jms.TextMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ScheduledMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
@Service
@Slf4j
public class QueueServiceImpl implements QueueService {
    
    private final JmsTemplate jmsTemplate;
    private final ApplicationProperties properties;
    
    @Autowired
    public QueueServiceImpl(JmsTemplate jmsTemplate, ApplicationProperties properties) {
        this.jmsTemplate = jmsTemplate;
        this.properties = properties;
    }

    /** {@inheritDoc} */
    @Override
    public void sendToTopic(String topic, String payload) {
        log.trace("Received payload for topic {} \n: {}", topic, payload);
        jmsTemplate.send(topic, (Session session) -> {
            TextMessage textMessage = session.createTextMessage(payload);
            textMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, properties.getActivemqMessageDeliveryRetrial() * 60 * 1000); // failed message delivery will repeat within the specified minutes configured
            return textMessage;
        });
    }
    
}
