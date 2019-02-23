/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heycar.test.service;

import com.heycar.test.dto.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
public interface QuerySpecification {
    /**
     * Builds a query specification object based on a user entity
     * @param criteria
     * @return 
     */
    Specification<? extends Object> buildQuery(SearchCriteria criteria);
}
