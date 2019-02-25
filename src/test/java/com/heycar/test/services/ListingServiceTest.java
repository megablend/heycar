/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heycar.test.services;

import static com.heycar.test.utils.Util.mockListing;
import static com.heycar.test.utils.Util.mockListingSearch;
import static com.heycar.test.utils.Util.mockListings;
import static com.heycar.test.utils.Util.stubListingSpecification;
import com.heycar.test.dto.ListingSearch;
import com.heycar.test.models.Listing;
import com.heycar.test.models.Provider;
import com.heycar.test.repos.ListingRepo;
import com.heycar.test.service.ListingService;
import com.heycar.test.service.QuerySpecification;
import com.heycar.test.service.impl.ListingServiceImpl;
import java.util.List;
import java.util.Optional;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.data.jpa.domain.Specification;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
public class ListingServiceTest {
    
    private ListingRepo listingRepo;
    private QuerySpecification querySpecification;
    private ListingService listingService;
    
    @Before
    public void setup() {
        listingRepo = mock(ListingRepo.class);
        querySpecification = mock(QuerySpecification.class);
        listingService = new ListingServiceImpl(listingRepo, querySpecification);
    }
    
    /**
     * Test case get listing by code and provider
     * @throws Exception 
     */
    @Test
    public void getListingByCodeAndProvider_whenCodeAndProvider_thenReturnListing() throws Exception {
        when((Specification<Listing>) querySpecification.buildQuery(any())).thenReturn(stubListingSpecification());
        when(listingRepo.findOne(isA(Specification.class))).thenReturn(Optional.of(mockListing()));
        
        // make the service call
        Listing listing = listingService.getListingByCodeAndProvider("aa", new Provider());
        
        assertNotNull(listing);
        assertEquals(listing.getCode(), mockListing().getCode());
        assertEquals(listing.getMake(), mockListing().getMake());
        assertThat(listing.getYear(), is(2015));
    }
    
    /**
     * Test case for get all listings
     * @throws Exception 
     */
    @Test
    public void getAllListings_thenReturnListOfListings() throws Exception {
        when(listingRepo.findAll()).thenReturn(mockListings());
        
        // make the service call
        List<Listing> listings = listingService.getAllListings();
        
        assertNotNull(listings);
        assertFalse(listings.isEmpty());
        assertThat(listings, containsInAnyOrder( // confirm that each of the elements returned the appropriate expected attributes
                hasProperty("code", is("a"))
                ));
        assertThat(listings.size(), is(mockListings().size())); // confirm that the sizes are the same
    }
    
    /**
     * Test case for searching listing
     * @throws Exception 
     */
    @Test
    public void searchByQueryParameter_whenSearchParameter_thenReturnListingSearch() throws Exception {
        when(listingRepo.getListingByParameter(any())).thenReturn(mockListingSearch());
        
        // make service call
        List<ListingSearch> listings = listingService.searchByQueryParameter("benz");
        
        assertNotNull(listings);
        assertFalse(listings.isEmpty());
        assertThat(listings, containsInAnyOrder( // confirm that each of the elements returned the appropriate expected attributes
                hasProperty("code", is("bb"))
                ));
        assertThat(listings.size(), is(mockListingSearch().size())); // confirm that the sizes are the same
    }
}
