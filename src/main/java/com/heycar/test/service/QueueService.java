/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heycar.test.service;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
public interface QueueService {
    /**
     * This method sends payload to a topic
     * @param topic
     * @param payload 
     */
    void sendToTopic(String topic, String payload);
    
    /**
     * Saves a provider's listings
     * @param payload 
     */
    void saveListings(String payload);
}
