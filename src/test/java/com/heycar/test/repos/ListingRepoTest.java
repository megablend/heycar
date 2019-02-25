/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heycar.test.repos;

import com.heycar.test.models.Dealer;
import com.heycar.test.models.Listing;
import com.heycar.test.models.Provider;
import static com.heycar.test.utils.Util.stubProvider;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan("com.heycar.test")
@TestPropertySource(locations="classpath:application-test.properties")
@Slf4j
public class ListingRepoTest {
    
    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private ListingRepo listingRepo;
    @Autowired
    private DealerRepo dealerRepo;
    @Autowired
    private ProviderRepo providerRepo;
    
    @Before
    public void setup() {
        Dealer dealer = new Dealer(null, "Benz", new Date(), new Date());
        Dealer savedDealer = testEntityManager.persist(dealer);
        log.info("The dealer ID is {}", savedDealer.getId());
        
        // save provider details
        Provider provider = new Provider(null, savedDealer, "Benz Provider", new Date(), new Date());
        testEntityManager.persist(provider);
    }
    
    /**
     * Test save all
     * @throws Exception 
     */
    @Test
    public void saveAll_thenReturnList() throws Exception {
        Listing listing1 = new Listing.ListingBuilder().setCode("bb")
                                                       .setColor("red")
                                                       .setKw(132)
                                                       .setMake("Benz")
                                                       .setModel("e300")
                                                       .setPrice(new BigDecimal(1000))
                                                       .setProvider(stubProvider())
                                                       .setYear(2016).build();
        
        Listing listing2 = new Listing.ListingBuilder().setCode("aa")
                                                       .setColor("yellow")
                                                       .setKw(132)
                                                       .setMake("vw")
                                                       .setModel("passat")
                                                       .setPrice(new BigDecimal(1500))
                                                       .setProvider(stubProvider())
                                                       .setYear(2012).build();
        List<Listing> listings = Arrays.asList(listing1, listing2);
        List<Listing> savedListings = new ArrayList<>();
        
        // save listings
        listings.stream().forEach(l -> savedListings.add(testEntityManager.persist(l)));
        
        assertThat(savedListings, containsInAnyOrder( // confirm that each of the elements returned the appropriate expected attributes
                hasProperty("code", is("bb")),
                hasProperty("code", is("aa"))
                ));
        assertNotNull(savedListings);
    }
    
    /**
     * Test save all and throw exception when the code and provider exists
     * @throws Exception 
     */
    @Test(expected = PersistenceException.class)
    public void saveAll_whenCodeAndProviderExists_thenThrowPersistenceException() throws Exception {
        Listing listing1 = new Listing.ListingBuilder().setCode("bb")
                                                       .setColor("red")
                                                       .setKw(132)
                                                       .setMake("Benz")
                                                       .setModel("e300")
                                                       .setPrice(new BigDecimal(1000))
                                                       .setProvider(stubProvider())
                                                       .setYear(2016).build();
        
        Listing listing2 = new Listing.ListingBuilder().setCode("bb")
                                                       .setColor("red")
                                                       .setKw(132)
                                                       .setMake("Benz")
                                                       .setModel("e300")
                                                       .setPrice(new BigDecimal(1000))
                                                       .setProvider(stubProvider())
                                                       .setYear(2016).build();
        List<Listing> listings = Arrays.asList(listing1, listing2);
        List<Listing> savedListings = new ArrayList<>();
        
        // save listings
        listings.stream().forEach(l -> savedListings.add(testEntityManager.persist(l)));
    }
}
