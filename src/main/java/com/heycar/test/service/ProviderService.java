/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heycar.test.service;

import com.heycar.test.models.Dealer;
import com.heycar.test.models.Provider;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
public interface ProviderService {
    /**
     * Get provider details by name and dealer
     * @param dealer
     * @param providerName
     * @return 
     */
    Provider getByProviderNameAndDealer(Dealer dealer, String providerName);
}
