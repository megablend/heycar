/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heycar.test.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
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
@Entity
@Table(name = "listings", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"code", "provider_id"})
}, indexes = {
    @Index(columnList = "make", name = "listings_make_index"),
    @Index(columnList = "model", name = "listings_model_index"),
    @Index(columnList = "color", name = "listings_color_index"),
    @Index(columnList = "model_year", name = "listings_model_year_index")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true, value = {"id", "provider", "createdAt", "updatedAt"})
public class Listing implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Pattern(regexp = "[a-zA-Z0-9]+", message = "Please provide a valid listing code")
    @Column(name = "code", length = 10)
    private String code;
    
    @Pattern(regexp = "[a-zA-Z0-9\\-, ]+", message = "Please provide a valid vehicle make")
    @Column(name = "make", length = 50)
    private String make;
    
    @NotBlank(message = "Please provide the vehicle model")
    @Column(name = "model", length = 50)
    private String model;
    
    @Min(value = 1, message = "The power in PS must not be less than one (1)")
    @Column(name = "power_in_ps", length = 10)
    private int kw;
    
    @Pattern(regexp = "^\\d{4}$", message = "Please provide a valid year")
    @Column(name = "model_year", length = 4)
    private int year;
    
    @Pattern(regexp = "[a-zA-Z\\- ]+", message = "Please provide a valid color")
    @Column(name = "color", length = 50)
    private String color;
    
    @NotNull(message = "Please provide the price for this listing")
    @Column(name = "price")
    private BigDecimal price;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "provider_id", referencedColumnName = "id")
    private Provider provider;
    
    @Column(name = "created_at")
    @Temporal(TemporalType.DATE)
    private Date createdAt;
    
    @Column(name = "updated_at")
    @Temporal(TemporalType.DATE)
    private Date updatedAt;
    
    @PrePersist
    public void updateDate() {
        this.updatedAt = new Timestamp(System.currentTimeMillis());
        if (null == createdAt)
            this.createdAt = new Timestamp(System.currentTimeMillis());
    }
}
