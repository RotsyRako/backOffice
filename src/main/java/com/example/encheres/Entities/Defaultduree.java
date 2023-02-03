package com.example.encheres.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="defaultduree")
public class Defaultduree {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column
    double duree;
    
    
    public Defaultduree() {
    }
    public Defaultduree(Long id, double duree) {
        this.id = id;
        this.duree = duree;
    }   
    public double getDuree() {
        return duree;
    }
    public void setDuree(double duree) {
        this.duree = duree;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    
}
