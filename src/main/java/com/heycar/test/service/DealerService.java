/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heycar.test.service;

import com.heycar.test.models.Dealer;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
public interface DealerService {
    /**
     * Get dealer by ID
     * @param id
     * @return 
     */
    Dealer findDealerById(Long id);
}
