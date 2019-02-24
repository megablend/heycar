/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heycar.test.service.impl;

import com.heycar.test.config.ApplicationProperties;
import com.heycar.test.dto.ListingRequest;
import com.heycar.test.models.Dealer;
import com.heycar.test.models.Listing;
import com.heycar.test.models.Provider;
import com.heycar.test.service.DealerService;
import com.heycar.test.service.ListingService;
import com.heycar.test.service.ProviderService;
import com.heycar.test.service.QueueService;
import com.heycar.test.util.Util;
import java.util.List;
import static java.util.stream.Collectors.toList;
import javax.jms.Session;
import javax.jms.TextMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ScheduledMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
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
    private final ListingService listingService;
    private final ProviderService providerService;
    private final DealerService dealerService;
    
    @Autowired
    public QueueServiceImpl(JmsTemplate jmsTemplate, ApplicationProperties properties, ListingService listingService, ProviderService providerService, DealerService dealerService) {
        this.jmsTemplate = jmsTemplate;
        this.properties = properties;
        this.listingService = listingService;
        this.providerService = providerService;
        this.dealerService = dealerService;
    }

    /** {@inheritDoc} */
    @Override
    public void sendToTopic(String topic, String payload) {
//        log.info("Received payload for topic {} \n: {}", topic, payload);
        jmsTemplate.send(topic, (Session session) -> {
            TextMessage textMessage = session.createTextMessage(payload);
            textMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, properties.getActivemqMessageDeliveryRetrial() * 60 * 1000); // failed message delivery will repeat within the specified minutes configured
            return textMessage;
        });
    }

    /** {@inheritDoc} */
    @Override
    @JmsListener(destination = "${heycar.activemq-save-listing-topic}", containerFactory = "containerFactory")
    public void saveListings(String payload) {
        ListingRequest request = Util.convertStringToObject(payload, ListingRequest.class);
        Dealer dealer = dealerService.findDealerById(request.getDealer());
        Provider provider = providerService.getByProviderNameAndDealer(dealer, request.getProvider());
        
        // save or update listing
        List<Listing> listings = request.getListings().stream().map((l) -> {
            Listing listing = new Listing.ListingBuilder().setCode(l.getCode())
                                                          .setColor(l.getColor())
                                                          .setKw(l.getKW())
                                                          .setMake(l.getMake())
                                                          .setModel(l.getModel())
                                                          .setPrice(l.getPrice())
                                                          .setYear(l.getYear())
                                                          .setProvider(provider).build();
            return listing;
        }).collect(toList());
        
        listingService.saveListing(provider, listings);
    }
    
}
