/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heycar.test.repos;

import com.heycar.test.dto.ListingSearch;
import com.heycar.test.models.Listing;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
public interface ListingRepo extends JpaRepository<Listing, Long>, JpaSpecificationExecutor<Listing> {
    /**
     * Gets query details by a search parameter
     * @param searchParameter
     * @return 
     */
    @Query("SELECT new com.heycar.test.dto.ListingSearch(l.code, l.model, l.kw, l.year, l.color, l.price)"
            + " FROM Listing l WHERE l.make LIKE %?1% OR l.model LIKE %?1% OR l.year LIKE %?1% OR l.color LIKE %?1%")
    List<ListingSearch> getListingByParameter(String searchParameter);
}
