package com.example.encheres.Entities;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Immutable
@Subselect("WITH mise_latest AS ( SELECT idenchere, MAX(datemise) AS latest_datemise FROM mise GROUP BY idenchere ) SELECT EXTRACT(YEAR FROM date_trunc('year', ml.latest_datemise)) AS year, SUM(m.mise * e.commission) AS revenu FROM mise m JOIN mise_latest ml ON m.idenchere = ml.idenchere AND m.datemise = ml.latest_datemise JOIN enchere e ON m.idenchere = e.idenchere GROUP BY year ORDER BY year")

@Table(name = "yearlyrevenu")
public class YearlyRevenu {
    @Id
    @Column(name = "year")
    private int year;
    @Column(name = "revenu")
    private double revenu;

    public YearlyRevenu() {
    }

    public YearlyRevenu(int year, double revenu) {
        this.year = year;
        this.revenu = revenu;
    }

    public int getYear() {
        return this.year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getRevenu() {
        return this.revenu;
    }

    public void setRevenu(double revenu) {
        this.revenu = revenu;
    }

}
