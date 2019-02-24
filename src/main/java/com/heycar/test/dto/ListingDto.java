/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heycar.test.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
public class ListingDto {
    @Pattern(regexp = "[a-zA-Z0-9]+", message = "Please provide a valid listing code")
    private String code;
    
    @Pattern(regexp = "[a-zA-Z0-9\\-, ]+", message = "Please provide a valid vehicle make")
    private String make;
    
    @NotBlank(message = "Please provide the vehicle model")
    private String model;
    
    @JsonProperty("kW")
    @Min(value = 1, message = "The power in PS must not be less than one (1)")
    private int kW;
    
    @Pattern(regexp = "^\\d{4}$", message = "Please provide a valid year")
    private int year;
    
    @Pattern(regexp = "[a-zA-Z\\- ]+", message = "Please provide a valid color")
    private String color;
    
    @NotNull(message = "Please provide the price for this listing")
    private BigDecimal price;
}
