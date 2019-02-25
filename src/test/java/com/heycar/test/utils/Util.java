/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heycar.test.utils;

import com.heycar.test.dto.ListingSearch;
import com.heycar.test.models.Dealer;
import com.heycar.test.models.Listing;
import com.heycar.test.models.Provider;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
public final class Util {
    
    private Util() {} // prevent this class from being initialized
    
    /**
     * Mock Listings
     * @return 
     */
    public static List<Listing> mockListings() {
        List<Listing> listings = new ArrayList<>();
        Listing listing1 = new Listing.ListingBuilder().setCode("a")
                                                       .setColor("red")
                                                       .setKw(123)
                                                       .setMake("mercedes")
                                                       .setModel("e300")
                                                       .setPrice(new BigDecimal(1000))
                                                       .setYear(2015).build();
        listings.add(listing1);
        return listings;
    }
    
    /**
     * Mock Listing Search 
     * @return 
     */
    public static List<ListingSearch> mockListingSearch() {
        List<ListingSearch> listings = new ArrayList<>();
        ListingSearch listing = new ListingSearch("bb", "volks", "passat", 28, 2016, "red", new BigDecimal(5000));
        listings.add(listing);
        return listings;
    }
    
    public static Specification<Provider> stubProviderSpecification() {
        return (Root<Provider> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {
            return cb.equal(root.get("key"), "value");
        };
    }
    
    public static Listing mockListing() {
        return new Listing.ListingBuilder().setCode("a")
                                                       .setColor("red")
                                                       .setKw(123)
                                                       .setMake("mercedes")
                                                       .setModel("e300")
                                                       .setPrice(new BigDecimal(1000))
                                                       .setYear(2015).build();
    }
    
    public static Provider stubProvider() {
        return new Provider(1L, new Dealer(1L, "Benz", null, null), "Benz Provider", null, null);
    }
    
    public static Specification<Listing> stubListingSpecification() {
        return (Root<Listing> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {
            return cb.equal(root.get("key"), "value");
        };
    }
}
