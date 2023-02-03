package com.example.encheres.Entities;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Immutable
@Subselect("SELECT SUM(enchere.commission*miseMaxDate.mise) as valeurCommission, enchere.idcategorie as idcategorie, categorieproduit.intitulecategorieproduit as nomproduit FROM enchere JOIN misemaxdate ON miseMaxDate.idenchere = enchere.idenchere JOIN categorieproduit ON enchere.idcategorie = categorieproduit.idcategorieproduit WHERE enchere.statut = 'clos' GROUP BY idcategorie,nomproduit ORDER BY valeurCommission DESC")

@Table(name = "commissionCategorie")
public class CommissionCategorie {
    @Id
    @Column(name = "valeurcommission")
    private double valeurCommission;
    @Column(name = "idcategorie")
    private int idcategorie;
    @Column(name = "nomproduit")
    private String nomproduit;

    public CommissionCategorie() {
    }

    public CommissionCategorie(double valeurCommission, int idcategorie, String nomproduit) {
        this.valeurCommission = valeurCommission;
        this.idcategorie = idcategorie;
        this.nomproduit = nomproduit;
    }

    public double getValeurCommission() {
        return this.valeurCommission;
    }

    public void setValeurCommission(double valeurCommission) {
        this.valeurCommission = valeurCommission;
    }

    public int getIdcategorie() {
        return this.idcategorie;
    }

    public void setIdcategorie(int idcategorie) {
        this.idcategorie = idcategorie;
    }

    public String getNomproduit() {
        return this.nomproduit;
    }

    public void setNomproduit(String nomproduit) {
        this.nomproduit = nomproduit;
    }
}
