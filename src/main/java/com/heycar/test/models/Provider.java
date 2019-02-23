/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heycar.test.models;

import java.io.Serializable;
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
@Table(name = "providers", indexes = {
    @Index(columnList = "name", name = "providers_name_index")
}, uniqueConstraints = {
    @UniqueConstraint(columnNames = {"dealer_id", "name"}, name = "unique_dealer_id_name")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Provider implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dealer_id", referencedColumnName = "id")
    private Dealer dealer;
    
    @Column(name = "name", length = 100)
    private String name;
    
    @Column(name = "created_at")
    @Temporal(TemporalType.DATE)
    private Date createdAt;
    
    @Column(name = "updated_at")
    @Temporal(TemporalType.DATE)
    private Date updatedAt;
    
    @PrePersist
    public void updateDate() {
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }
}
