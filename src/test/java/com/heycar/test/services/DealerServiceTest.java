/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heycar.test.services;

import com.heycar.test.models.Dealer;
import com.heycar.test.repos.DealerRepo;
import com.heycar.test.service.DealerService;
import com.heycar.test.service.impl.DealerServiceImpl;
import java.util.Optional;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
public class DealerServiceTest {
    
    private DealerRepo dealerRepo;
    private DealerService dealerService;
    
    @Before
    public void setup() {
        dealerRepo = mock(DealerRepo.class);
        dealerService = new DealerServiceImpl(dealerRepo);
    }
    
    /**
     * Test find dealer find ID
     * @throws Exception 
     */
    @Test
    public void findDealerById_whenIdIsProvided_thenReturnDealer() throws Exception {
        // mock dependencies
        when(dealerRepo.findById(isA(Long.class))).thenReturn(Optional.of(new Dealer(Long.valueOf(1), "Benz", null, null)));
        
        // make the service call
        Dealer dealer = dealerService.findDealerById(Long.valueOf(1));
        
        assertNotNull(dealer);
        assertEquals(dealer.getName(), "Benz");
        assertThat(dealer.getId(), is(1L));
    }
}
