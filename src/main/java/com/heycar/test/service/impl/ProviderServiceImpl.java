/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heycar.test.service.impl;

import com.heycar.test.dto.SearchCriteria;
import com.heycar.test.models.Dealer;
import com.heycar.test.models.Provider;
import com.heycar.test.repos.ProviderRepo;
import com.heycar.test.service.ProviderService;
import com.heycar.test.service.QuerySpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
@Service
@Slf4j
public class ProviderServiceImpl implements ProviderService {
    
    private final ProviderRepo providerRepo;
    private final QuerySpecification querySpecification;
    
    @Autowired
    public ProviderServiceImpl(QuerySpecification querySpecification, ProviderRepo providerRepo) {
        this.querySpecification = querySpecification;
        this.providerRepo = providerRepo;
    }

    /** {@inheritDoc} */
    @Override
    public Provider getByProviderNameAndDealer(Dealer dealer, String providerName) {
        Specification<Provider> dealerSpec = (Specification<Provider>) querySpecification.buildQuery(new SearchCriteria("dealer", ":", dealer));
        Specification<Provider> providerSpec = (Specification<Provider>) querySpecification.buildQuery(new SearchCriteria("name", ":", providerName));
        return providerRepo.findOne(Specification.where(dealerSpec).and(providerSpec)).orElseGet(() -> null);
    }
    
}
