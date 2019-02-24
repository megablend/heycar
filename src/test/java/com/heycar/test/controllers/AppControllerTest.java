/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heycar.test.controllers;

import com.heycar.test.config.ApplicationProperties;
import com.heycar.test.dto.ListingSearch;
import com.heycar.test.exception.InvalidRowException;
import com.heycar.test.models.Dealer;
import com.heycar.test.models.Listing;
import com.heycar.test.models.Provider;
import com.heycar.test.service.DealerService;
import com.heycar.test.service.FileProcessorService;
import com.heycar.test.service.ListingService;
import com.heycar.test.service.ProviderService;
import com.heycar.test.service.QueueService;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.apache.commons.io.IOUtils;
import org.hamcrest.Matchers;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
@RunWith(SpringRunner.class)
@WebMvcTest(AppController.class)
@Slf4j
public class AppControllerTest {
    
    @Autowired
    private MockMvc mvc;
    @Autowired
    private WebApplicationContext wac; 
    @MockBean
    private ProviderService providerService;
    @MockBean
    private DealerService dealerService;
    @MockBean
    private ApplicationProperties appProperties;
    @MockBean
    private QueueService queueService;
    @MockBean
    private ListingService listingService;
    
    @MockBean
    private FileProcessorService fileProcessorService;
    private String fileContent;
    
    @Before
    public void setup() { 
        try {
            mvc = MockMvcBuilders.webAppContextSetup(wac).build();
            fileContent = IOUtils.toString(this.getClass().getResourceAsStream("/textcsv.csv"), "UTF-8");
        } catch (IOException ex) {
            log.error("Unable to read csv file ", ex);
        }
    }
    
    /**
     * Test case for valid CSV file
     * @throws Exception 
     */
    @Test
    public void uploadCsv_whenValidCsv_thenReturnOk() throws Exception {
        /**
         * {
                "responseCode": "00",
                "responseMessage": "Listings successfully uploaded"
           }
         */
        String res = "{\"responseCode\":\"00\",\"responseMessage\":\"Listings successfully uploaded\"}";
        
        //mock dependencies
        when(dealerService.findDealerById(any())).thenReturn(new Dealer());
        when(providerService.getByProviderNameAndDealer(any(), any())).thenReturn(new Provider());
        doNothing().when(fileProcessorService).processFile(any(), any());
        
         MockMultipartFile mockMultipartFile = new MockMultipartFile("file","testcsv.csv",
                                                            "application/octet-stream", fileContent.getBytes());
        
         MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.fileUpload("/upload_csv/1")
                                                                        .file(mockMultipartFile)
                                                                        .param("provider", "Chevrolet Provider");
         
         MvcResult result = mvc.perform(requestBuilder)
//                            .andDo(print())
                            .andExpect(status().isOk())
                            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                            .andExpect(jsonPath("$.responseCode", Matchers.is("00")))
                            .andExpect(jsonPath("$.responseMessage", Matchers.is("Listings successfully uploaded"))).andReturn();
         
         // verify that the services are called
         verify(dealerService, times(1)).findDealerById(any());
         verify(providerService, times(1)).getByProviderNameAndDealer(any(), any());
         verify(fileProcessorService, times(1)).processFile(any(), any());
         assertEquals(result.getResponse().getContentAsString(), res);
    }
    
    /**
     * When no file/provider detail provided
     * @throws Exception 
     */
    @Test
    public void uploadCsv_whenNoFileAndNoProvider_thenReturnBadGateway() throws Exception {
        
        MockMultipartFile mockMultipartFile = new MockMultipartFile("filezzzz","testcsv.csv",
                                                            "application/octet-stream", fileContent.getBytes());
        
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.fileUpload("/upload_csv/1")
                                                                        .file(mockMultipartFile)
                                                                        .param("provider", "Chevrolet Provider");
        /**
         * {
                "timestamp": "2019-02-24T19:16:19.616+0000",
                "message": "Invalid Parameter(s) Provided",
                "details": [
                    "null, Please upload the vehicles listing file"
                ]
           }
         */
        mvc.perform(requestBuilder)
//            .andDo(print())
            .andExpect(status().isBadGateway())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.timestamp", Matchers.notNullValue()))
            .andExpect(jsonPath("$.message", Matchers.notNullValue()))
            .andExpect(jsonPath("$.details", Matchers.notNullValue()));
        
        MockMultipartFile mockProviderMultipartFile = new MockMultipartFile("file","testcsv.csv",
                                                            "application/octet-stream", fileContent.getBytes());
        
        MockHttpServletRequestBuilder providerRequestBuilder = MockMvcRequestBuilders.fileUpload("/upload_csv/1")
                                                                        .file(mockProviderMultipartFile);
        /**
         * {
                "timestamp": "2019-02-24T19:28:51.540+0000",
                "message": "Invalid Parameter(s) Provided",
                "details": [
                    "null, Please enter the provider for this listing"
                ]
           }
         */
        mvc.perform(providerRequestBuilder)
//            .andDo(print())
            .andExpect(status().isBadGateway())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.timestamp", Matchers.notNullValue()))
            .andExpect(jsonPath("$.message", Matchers.notNullValue()))
            .andExpect(jsonPath("$.details", Matchers.notNullValue()));
    }
    
    /**
     * Test case for invalid dealer ID
     * @throws Exception 
     */
    @Test
    public void uploadCsv_whenInvalidDealerIdProvided_thenReturnBadGateway() throws Exception {
        
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file","testcsv.csv",
                                                            "application/octet-stream", fileContent.getBytes());
        
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.fileUpload("/upload_csv/-100")
                                                                        .file(mockMultipartFile)
                                                                        .param("provider", "Chevrolet Provider");
        /**
         * {
                "timestamp": "2019-02-24T19:32:07.802+0000",
                "message": "Invalid Parameter(s) Provided",
                "details": [
                    "-100, Dealer ID must not be less than one (1)"
                ]
           }
         */
        mvc.perform(requestBuilder)
//            .andDo(print())
            .andExpect(status().isBadGateway())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.timestamp", Matchers.notNullValue()))
            .andExpect(jsonPath("$.message", Matchers.is("Invalid Parameter(s) Provided")))
            .andExpect(jsonPath("$.details[0]", Matchers.is("-100, Dealer ID must not be less than one (1)")));
    }
    
    /**
     * Test case for non-existent dealers
     * @throws Exception 
     */
    @Test
    public void uploadCsv_whenDealerDoesNotExist_thenReturnBadRequest() throws Exception {
        
        /**
         * {
                "responseCode": "01",
                "responseMessage": "This dealer does not exist"
           }
         */
        String res = "{\"responseCode\":\"01\",\"responseMessage\":\"This dealer does not exist\"}";
        
        // mock dependencies
        when(dealerService.findDealerById(any())).thenReturn(null);
        
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file","testcsv.csv",
                                                            "application/octet-stream", fileContent.getBytes());
        
         MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.fileUpload("/upload_csv/1")
                                                                        .file(mockMultipartFile)
                                                                        .param("provider", "Chevrolet Provider");
         
         MvcResult result = mvc.perform(requestBuilder)
//                            .andDo(print())
                            .andExpect(status().isBadRequest())
                            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                            .andExpect(jsonPath("$.responseCode", Matchers.is("01")))
                            .andExpect(jsonPath("$.responseMessage", Matchers.is("This dealer does not exist"))).andReturn();
         
         // verify that the services are called
         verify(dealerService, times(1)).findDealerById(any());
         assertEquals(result.getResponse().getContentAsString(), res);
    }
    
    /**
     * Test case for non-existent provider
     * @throws Exception 
     */
    @Test
    public void uploadCsv_whenProviderDoesNotExist_thenReturnBadrequest() throws Exception {
        /**
         * {
                "responseCode": "02",
                "responseMessage": "This provider does not exist for Benz"
           }
         */
        String res = "{\"responseCode\":\"02\",\"responseMessage\":\"This provider does not exist for Benz\"}";
        
        // mock dependencies
        when(dealerService.findDealerById(any())).thenReturn(new Dealer(Long.valueOf(1), "Benz", null, null));
        when(providerService.getByProviderNameAndDealer(any(), any())).thenReturn(null);
        
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file","testcsv.csv",
                                                            "application/octet-stream", fileContent.getBytes());
        
         MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.fileUpload("/upload_csv/1")
                                                                        .file(mockMultipartFile)
                                                                        .param("provider", "Chevrolet Provider");
         
         MvcResult result = mvc.perform(requestBuilder)
//                            .andDo(print())
                            .andExpect(status().isBadRequest())
                            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                            .andExpect(jsonPath("$.responseCode", Matchers.is("02")))
                            .andExpect(jsonPath("$.responseMessage", Matchers.is("This provider does not exist for Benz"))).andReturn();
         
         // verify that the services are called
         verify(dealerService, times(1)).findDealerById(any());
         verify(providerService, times(1)).getByProviderNameAndDealer(any(), any());
         assertEquals(result.getResponse().getContentAsString(), res);
    }
    
    /**
     * Throw InvalidRowException when and an invalid row is detected
     * @throws Exception 
     */
    @Test
    public void uploadCsv_whenInvalidRow_thenReturnBadGateway() throws Exception {
        
        //mock dependencies
        when(dealerService.findDealerById(any())).thenReturn(new Dealer());
        when(providerService.getByProviderNameAndDealer(any(), any())).thenReturn(new Provider());
        Mockito.doThrow(InvalidRowException.class).when(fileProcessorService).processFile(any(), any());
        
         MockMultipartFile mockMultipartFile = new MockMultipartFile("file","testcsv.csv",
                                                            "application/octet-stream", fileContent.getBytes());
        
         MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.fileUpload("/upload_csv/1")
                                                                        .file(mockMultipartFile)
                                                                        .param("provider", "Chevrolet Provider");
         
         mvc.perform(requestBuilder)
//            .andDo(print())
            .andExpect(status().isBadGateway());
         
         // verify that the services are called
         verify(dealerService, times(1)).findDealerById(any());
         verify(providerService, times(1)).getByProviderNameAndDealer(any(), any());
         verify(fileProcessorService, times(1)).processFile(any(), any());
    }
    
    /**
     * Send valid listings via the API
     * @throws Exception 
     */
    @Test
    public void uploadListing_withValidRequest_thenReturnOk() throws Exception {
        
        /**
         * {
                "responseCode": "00",
                "responseMessage": "Listings successfully saved"
           }
         */
        String res = "{\"responseCode\":\"00\",\"responseMessage\":\"Listings successfully saved\"}";
        
        /**
         * {
                    "dealer": 1,
                    "provider": "Chevrolet Provider",
                    "listings": [
                            {
                                    "code": "a",
                                    "make": "renault",
                                    "model": "megane",
                                    "kW": 132,
                                    "year": 2014,
                                    "color": "red",
                                    "price": 13990
                            }
                    ]
           }
         */
        String requestStr = "{ \"dealer\": 1, \"provider\": \"Chevrolet Provider\", \"listings\": [ { \"code\": \"a\", \"make\": \"renault\", \"model\": \"megane\", \"kW\": 132, \"year\": 2014, \"color\": \"red\", \"price\": 13990 } ] }";
        // mock depednencies
        when(dealerService.findDealerById(any())).thenReturn(new Dealer());
        when(providerService.getByProviderNameAndDealer(any(), any())).thenReturn(new Provider());
        when(appProperties.getMaximumNumberOfListings()).thenReturn(10);
        doNothing().when(queueService).sendToTopic(any(), any());
        
        MvcResult result = mvc.perform(post("/vehicle_listings")
                            .contentType(MediaType.APPLICATION_JSON)
//                            .accept(MediaType.APPLICATION_JSON)
                            .content(requestStr))
//                            .andDo(print())
                            .andExpect(status().isOk())
                            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                            .andExpect(jsonPath("$.responseCode", Matchers.is("00")))
                            .andExpect(jsonPath("$.responseMessage", Matchers.is("Listings successfully saved"))).andReturn();
        
        verify(dealerService, times(1)).findDealerById(any());
        verify(providerService, times(1)).getByProviderNameAndDealer(any(), any());
        verify(appProperties, times(1)).getMaximumNumberOfListings();
        verify(queueService, times(1)).sendToTopic(any(), any());
        assertEquals(result.getResponse().getContentAsString(), res);
    }
    
    /**
     * Test case for API invalid request
     * @throws Exception 
     */
    @Test
    public void uploadListing_withInvalidRequest_thenReturnBadGateway() throws Exception {
        /**
         * {
                    "dealer": 1,
                    "provider": "Chevrolet Provider",
                    "listings": []
           }
         */
        String requestStr = "{ \"dealer\": 1, \"provider\": \"Chevrolet Provider\", \"listings\": [] }";
        
        mvc.perform(post("/vehicle_listings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestStr))
//            .andDo(print())
            .andExpect(status().isBadGateway())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.timestamp", Matchers.notNullValue()))
            .andExpect(jsonPath("$.message", Matchers.is("Invalid Parameter(s) Provided")))
            .andExpect(jsonPath("$.details[0]", Matchers.is("[], Invalid listings provided")));
    }
    
    /**
     * Test case for maximum listing limit
     * @throws Exception 
     */
    @Test
    public void uploadListing_whenLimitExceeded_thenReturnBadRequest() throws Exception {
        
        /**
         * {
                "responseCode": "04",
                "responseMessage": "Listings successfully saved"
           }
         */
        String res = "{\"responseCode\":\"04\",\"responseMessage\":\"You have exceeded the maximum allowable number of listings per request:  1\"}";
        
        /**
         * {
                    "dealer": 1,
                    "provider": "Chevrolet Provider",
                    "listings": [
                            {
                                    "code": "a",
                                    "make": "renault",
                                    "model": "megane",
                                    "kW": 132,
                                    "year": 2014,
                                    "color": "red",
                                    "price": 13990
                            },
                            {
                                    "code": "abb",
                                    "make": "renault",
                                    "model": "megane",
                                    "kW": 132,
                                    "year": 2014,
                                    "color": "red",
                                    "price": 13990
                            }
                    ]
           }
         */
        String requestStr = "{ \"dealer\": 1, \"provider\": \"Chevrolet Provider\", \"listings\": [ { \"code\": \"a\", \"make\": \"renault\", \"model\": \"megane\", \"kW\": 132, \"year\": 2014, \"color\": \"red\", \"price\": 13990 }, { \"code\": \"abb\", \"make\": \"renault\", \"model\": \"megane\", \"kW\": 132, \"year\": 2014, \"color\": \"red\", \"price\": 13990 } ] }";
        
        // mock depednencies
        when(dealerService.findDealerById(any())).thenReturn(new Dealer());
        when(providerService.getByProviderNameAndDealer(any(), any())).thenReturn(new Provider());
        when(appProperties.getMaximumNumberOfListings()).thenReturn(1);
        
        MvcResult result = mvc.perform(post("/vehicle_listings")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestStr))
                            .andDo(print())
                            .andExpect(status().isBadRequest())
                            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                            .andExpect(jsonPath("$.responseCode", Matchers.is("04")))
                            .andExpect(jsonPath("$.responseMessage", Matchers.is("You have exceeded the maximum allowable number of listings per request:  1"))).andReturn();
        
        verify(dealerService, times(1)).findDealerById(any());
        verify(providerService, times(1)).getByProviderNameAndDealer(any(), any());
        verify(appProperties, times(2)).getMaximumNumberOfListings();
        assertEquals(result.getResponse().getContentAsString(), res);
    }
    
    /**
     * Test case for show all listings
     * @throws Exception 
     */
    @Test
    public void showAllListing_whenAll_thenReturnOk() throws Exception {
        // mock dependencies
        when(listingService.getAllListings()).thenReturn(mockListings());
        
        mvc.perform(get("/search")
                            .contentType(MediaType.APPLICATION_JSON))
                            .andDo(print())
                            .andExpect(status().isOk())
                            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                            .andExpect(jsonPath("$.[0].code", Matchers.is("a")))
                            .andExpect(jsonPath("$.[0].model", Matchers.is("e300")));
        
    }
    
    /**
     * Test Case for Search Filter
     * @throws Exception 
     */
    @Test
    public void showAllListing_whenSearchQuery_thenReturnOk() throws Exception {
        // mock dependencies
        when(listingService.searchByQueryParameter(any())).thenReturn(mockListingSearch());
        
        mvc.perform(get("/search?fullQuery=zzzz")
                            .contentType(MediaType.APPLICATION_JSON))
                            .andDo(print())
                            .andExpect(status().isOk())
                            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                            .andExpect(jsonPath("$.[0].code", Matchers.is("bb")))
                            .andExpect(jsonPath("$.[0].make", Matchers.is("volks")));
    }
    
    /**
     * Mock Listings
     * @return 
     */
    private List<Listing> mockListings() {
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
    private List<ListingSearch> mockListingSearch() {
        List<ListingSearch> listings = new ArrayList<>();
        ListingSearch listing = new ListingSearch("bb", "volks", "passat", 28, 2016, "red", new BigDecimal(5000));
        listings.add(listing);
        return listings;
    }
}
