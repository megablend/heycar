/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heycar.test.service;

import com.heycar.test.dto.ListingSearch;
import com.heycar.test.models.Listing;
import com.heycar.test.models.Provider;
import java.util.List;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
public interface ListingService {
    /**
     * Save Listings by provider
     * @param provider
     * @param listings 
     */
    void saveListing(Provider provider, List<Listing> listings);
    /**
     * Save all listings
     * @param listings 
     */
    void saveAll(List<Listing> listings);
    
    /**
     * Get Listing by code and provider
     * @param code
     * @param provider
     * @return 
     */
    Listing getListingByCodeAndProvider(String code, Provider provider);
    /**
     * Gets all listings
     * @return 
     */
    List<Listing> getAllListings();
    
    /**
     * Search by a selected query parameter
     * @param searchParameter
     * @return 
     */
    List<ListingSearch> searchByQueryParameter(String searchParameter);
}
