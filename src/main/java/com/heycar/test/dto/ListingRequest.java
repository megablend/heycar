/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heycar.test.dto;

import com.heycar.test.models.Listing;
import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListingRequest {
    @Min(1)
    private Long dealer;
    @NotBlank(message = "Please provide the provider for this listing")
    private String provider;
    @NotBlank(message = "Invalid collection of listings provided")
    private List<Listing> listings;
}
