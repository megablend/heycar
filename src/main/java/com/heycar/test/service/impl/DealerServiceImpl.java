/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heycar.test.service.impl;

import com.heycar.test.models.Dealer;
import com.heycar.test.repos.DealerRepo;
import com.heycar.test.service.DealerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
@Service
@Slf4j
public class DealerServiceImpl implements DealerService {
    
    private final DealerRepo dealerRepo;
    
    @Autowired
    public DealerServiceImpl(DealerRepo dealerRepo) {
        this.dealerRepo = dealerRepo;
    }

    /** {@inheritDoc} */
    @Override
    public Dealer findDealerById(Long id) {
        return dealerRepo.findById(id).orElseGet(() -> null);
    }
}
