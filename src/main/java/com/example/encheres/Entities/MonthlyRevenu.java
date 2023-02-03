package com.example.encheres.Entities;

import java.time.Month;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Immutable
@Subselect("WITH mise_latest AS (SELECT idenchere, MAX(datemise) AS latest_datemise FROM mise GROUP BY idenchere ) SELECT EXTRACT(MONTH FROM date_trunc('month', ml.latest_datemise)) AS month, SUM(m.mise * e.commission) AS revenu FROM mise m JOIN mise_latest ml ON m.idenchere = ml.idenchere AND m.datemise = ml.latest_datemise JOIN enchere e ON m.idenchere = e.idenchere GROUP BY month ORDER BY month")

@Table(name = "monthlyrevenu")
public class MonthlyRevenu {
    @Id
    @Column(name = "month")
    private int month;
    @Column(name = "revenu")
    private double revenu;

    public MonthlyRevenu() {
    }

    public MonthlyRevenu(int month, double revenu) {
        this.month = month;
        this.revenu = revenu;
    }

    public int getMonth() {
        return this.month;
    }

    public String getMois() {
        return Month.of(month).toString();
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public double getRevenu() {
        return this.revenu;
    }

    public void setRevenu(double revenu) {
        this.revenu = revenu;
    }
}
