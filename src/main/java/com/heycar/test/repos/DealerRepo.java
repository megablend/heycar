/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heycar.test.repos;

import com.heycar.test.models.Dealer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
public interface DealerRepo extends JpaRepository<Dealer, Long>, JpaSpecificationExecutor<Dealer> {
    
}
