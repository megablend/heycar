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
    
    @Column(name = "code", length = 10)
    private String code;
    
    @Column(name = "make", length = 50)
    private String make;
    
    @Column(name = "model", length = 50)
    private String model;
    
    @Column(name = "power_in_ps", length = 10)
    private int kw;
    
    @Column(name = "model_year", length = 4)
    private int year;
    
    @Column(name = "color", length = 50)
    private String color;
    
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
    
    private Listing(ListingBuilder builder) {
        this.code = builder.code;
        this.make = builder.make;
        this.model = builder.model;
        this.kw = builder.kw;
        this.year = builder.year;
        this.color = builder.color;
        this.price = builder.price;
        this.provider = builder.provider;
    }
    
    public static class ListingBuilder {
        private String code;
        private String make;
        private String model;
        private int kw;
        private int year;
        private String color;
        private BigDecimal price;
        private Provider provider;
        
        public ListingBuilder() {}

        public ListingBuilder setCode(String code) {
            this.code = code;
            return this;
        }

        public ListingBuilder setMake(String make) {
            this.make = make;
            return this;
        }

        public ListingBuilder setModel(String model) {
            this.model = model;
            return this;
        }

        public ListingBuilder setKw(int kw) {
            this.kw = kw;
            return this;
        }

        public ListingBuilder setYear(int year) {
            this.year = year;
            return this;
        }

        public ListingBuilder setColor(String color) {
            this.color = color;
            return this;
        }

        public ListingBuilder setPrice(BigDecimal price) {
            this.price = price;
            return this;
        }

        public ListingBuilder setProvider(Provider provider) {
            this.provider = provider;
            return this;
        }
        
        public Listing build() {
            return new Listing(this);
        }
    }
    
    @PrePersist
    public void updateDate() {
        this.updatedAt = new Timestamp(System.currentTimeMillis());
        if (null == createdAt)
            this.createdAt = new Timestamp(System.currentTimeMillis());
    }
}
