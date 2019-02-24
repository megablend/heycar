/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heycar.test.controllers;

import com.heycar.test.config.ApplicationProperties;
import com.heycar.test.dto.ListingRequest;
import com.heycar.test.dto.Responses;
import com.heycar.test.models.Dealer;
import com.heycar.test.models.Provider;
import com.heycar.test.response.ApiResponseFactory;
import com.heycar.test.response.ResponseFactory;
import com.heycar.test.service.DealerService;
import com.heycar.test.service.FileProcessorService;
import com.heycar.test.service.ListingService;
import com.heycar.test.service.ProviderService;
import com.heycar.test.service.QueueService;
import com.heycar.test.util.Util;
import static com.heycar.test.util.Util.validFileExtension;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
@RestController
@Slf4j
@Validated
@Api(value = "ApplicationController", description = "To manage all the endpoints for hey car vehicle listing", tags = {"Manage Vehicle Listings"})
public class AppController {
    
    @Autowired
    private ProviderService providerService;
    @Autowired
    private DealerService dealerService;
    @Autowired
    private ApplicationProperties appProperties;
    @Autowired
    private QueueService queueService;
    @Autowired
    private ListingService listingService;
    
    @Autowired
    private FileProcessorService fileProcessorService;
    
    /**
     * Upload CSV for processing
     * @param dealerId
     * @param file
     * @param providerName
     * @return
     * @throws Exception 
     */
    @PostMapping("/upload_csv/{dealerId}")
    @ApiOperation(value = "Upload Listing in CSV Format", notes = "This endpoint manages the upload of listings by providers in CSV format", nickname = "Upload Vehicle Listings in CSV")
    public ResponseEntity uploadCsv(@PathVariable @Min(value = 1, message = "Dealer ID must not be less than one (1)") Long dealerId, 
                                    @RequestParam(value = "file", required = false) @NotNull(message = "Please upload the vehicles listing file") MultipartFile file, 
                                    @RequestParam(value = "provider", required = false) @NotEmpty(message = "Please enter the provider for this listing") String providerName) throws Exception {
        // do some validation checks
        if (file.isEmpty() || null == file.getOriginalFilename() || file.getOriginalFilename().trim().isEmpty()) 
            return ResponseEntity.badRequest().body(ResponseFactory.createResponse(new ApiResponseFactory(Responses.NO_FILE_UPLOADED, "Please upload a CSV file")));
        
        if (!validFileExtension(file))
            return ResponseEntity.badRequest().body(ResponseFactory.createResponse(new ApiResponseFactory(Responses.NO_FILE_UPLOADED, "The file must be in .csv format")));
        
        // check if this provider and dealer exists
        Dealer dealer = dealerService.findDealerById(dealerId);
        if (null == dealer)
            return ResponseEntity.badRequest().body(ResponseFactory.createResponse(new ApiResponseFactory(Responses.DEALER_DOES_NOT_EXIST, "This dealer does not exist")));
        
        Provider provider = providerService.getByProviderNameAndDealer(dealer, providerName);
        if (null == provider)
            return ResponseEntity.badRequest().body(ResponseFactory.createResponse(new ApiResponseFactory(Responses.PROVIDER_DOES_NOT_EXIST, new StringBuilder("This provider does not exist for ").append(dealer.getName()).toString())));
        
        fileProcessorService.processFile(file.getInputStream(), provider);
        return ResponseEntity.ok(ResponseFactory.createResponse(new ApiResponseFactory(Responses.SUCCESS,"Listings successfully uploaded")));
    }
    
    /**
     * Saves dealers listings
     * @param request
     * @param result
     * @return 
     */
    @PostMapping("/vehicle_listings")
    @ApiOperation(value = "Upload Listing", notes = "This endpoint manages the upload of listings by providers", nickname = "Upload Vehicle Listings")
    public ResponseEntity uploadListing(@Valid @RequestBody ListingRequest request, BindingResult result) {
        
        // check if this provider and dealer exists
        Dealer dealer = dealerService.findDealerById(request.getDealer());
        
        if (null == dealer)
            return ResponseEntity.badRequest().body(ResponseFactory.createResponse(new ApiResponseFactory(Responses.DEALER_DOES_NOT_EXIST, "This dealer does not exist")));
        
        if (null == providerService.getByProviderNameAndDealer(dealer, request.getProvider()))
            return ResponseEntity.badRequest().body(ResponseFactory.createResponse(new ApiResponseFactory(Responses.PROVIDER_DOES_NOT_EXIST, new StringBuilder("This provider does not exist for ").append(dealer.getName()).toString())));
        
        // check if the listing is more than the allowed number of listings per request
        if (request.getListings().size() > appProperties.getMaximumNumberOfListings())
            return ResponseEntity.badRequest().body(ResponseFactory.createResponse(new ApiResponseFactory(Responses.MAXIMUM_LIST_EXCEEDED, new StringBuilder("You have exceeded the maximum allowable number of listings per request:  ").append(appProperties.getMaximumNumberOfListings()).toString())));
        
        // save the listings
        queueService.sendToTopic(appProperties.getActivemqSaveListingTopic(), Util.convertObjectToJson(request));
        return ResponseEntity.ok(ResponseFactory.createResponse(new ApiResponseFactory(Responses.SUCCESS,"Listings successfully saved")));
    }
    
    
    /**
     * Show listing by queries
     * @param query {optional} -  required for searching specific parameters
     * @return 
     */
    @GetMapping("/search")
    @ApiOperation(value = "Show All Listings", notes = "This endpoint shows all listings", nickname = "Show all listings")
    public ResponseEntity showAllListing(@RequestParam(value = "fullQuery", required = false) String query) {
        if (null == query)
            return ResponseEntity.ok(listingService.getAllListings());
        else 
            return ResponseEntity.ok(listingService.searchByQueryParameter(query));
    }
}
