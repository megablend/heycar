/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heycar.test.services;

import com.heycar.test.models.Dealer;
import com.heycar.test.models.Provider;
import com.heycar.test.repos.ProviderRepo;
import com.heycar.test.service.ProviderService;
import com.heycar.test.service.QuerySpecification;
import com.heycar.test.service.impl.ProviderServiceImpl;
import java.util.Optional;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
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
public class ProviderServiceTest {
    
    private ProviderRepo providerRepo;
    private QuerySpecification querySpecification;
    private ProviderService providerService;
    
    @Before
    public void setup() {
        providerRepo = mock(ProviderRepo.class);
        querySpecification = mock(QuerySpecification.class);
        providerService = new ProviderServiceImpl(querySpecification, providerRepo);
    }
    
    /**
     * Test case for get provider by name and dealer
     * @throws Exception 
     */
    @Test
    public void getByProviderNameAndDealer_whenDealerAndProviderName_thenReturnProvider() throws Exception {
        when((Specification<Provider>) querySpecification.buildQuery(any())).thenReturn(stubProviderSpecification());
        when(providerRepo.findOne(isA(Specification.class))).thenReturn(Optional.of(stubProvider()));
        
        // make the service call
        Provider provider = providerService.getByProviderNameAndDealer(new Dealer(), "Benz Provider");
        
        assertNotNull(provider);
        assertEquals("Benz", provider.getDealer().getName());
        assertEquals(provider.getName(), stubProvider().getName());
        assertThat(provider.getId(), is(1L));
    }
    
    public Specification<Provider> stubProviderSpecification() {
        return (Root<Provider> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {
            return cb.equal(root.get("key"), "value");
        };
    }
    
    public Provider stubProvider() {
        return new Provider(1L, new Dealer(1L, "Benz", null, null), "Benz Provider", null, null);
    }
}
