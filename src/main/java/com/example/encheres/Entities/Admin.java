package com.example.encheres.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name="Admin")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idadmin_seq")
	@SequenceGenerator(name = "idadmin_seq", sequenceName = "idadmin_seq", allocationSize = 1)
    @Column(name = "idadmin")
    private  int idAdmin;
    @Column(name =  "pseudo")
    private  String pseudo;
    @Column(name =  "mdp")
    private String mdp;  
    
    public Admin() {
    }
    public Admin(int idAdmin, String pseudo, String mdp) {
        this.idAdmin = idAdmin;
        this.pseudo = pseudo;
        this.mdp = mdp;
    }
    public long getIdAdmin() {
        return idAdmin;
    }
    public void setIdAdmin(int idAdmin) {
        this.idAdmin = idAdmin;
    }
    public String getPseudo() {
        return pseudo;
    }
    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }
    public String getMdp() {
        return mdp;
    }
    public void setMdp(String mdp) {
        this.mdp = mdp;
    }
   
}
