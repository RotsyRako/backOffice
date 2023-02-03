package com.example.encheres.controllers;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.encheres.Entities.Admin;
import com.example.encheres.Entities.CategorieProduit;
import com.example.encheres.Entities.CommissionCategorie;
import com.example.encheres.Entities.Defaultduree;
import com.example.encheres.Entities.Enchere;
import com.example.encheres.Entities.EncheresParMois;
import com.example.encheres.Entities.MonthlyRevenu;
import com.example.encheres.Entities.SoldeUtilisateurValide;
import com.example.encheres.Entities.View_allSoldes;
import com.example.encheres.Entities.YearlyRevenu;
import com.example.encheres.repositories.CommissionCategorieRepository;
import com.example.encheres.repositories.EnchereRepository;
import com.example.encheres.repositories.EncheresParMoisRepository;
import com.example.encheres.repositories.MonthlyRevenuRepository;
import com.example.encheres.repositories.SoldeUtilisateurValideRepository;
import com.example.encheres.repositories.View_allSoldesRepository;
import com.example.encheres.repositories.YearlyRevenuRepository;
import com.example.encheres.services.AdminService;

@CrossOrigin
@Controller
@RequestMapping("/BackOffice")
public class AdminController {
    @Autowired
    AdminService adminService;
    @Autowired
    AdminService utilisateurService;
    @Autowired
    View_allSoldesRepository viewSoldesRepository;
    @Autowired
    SoldeUtilisateurValideRepository suvr;
    @Autowired
    EncheresParMoisRepository epm;
    @Autowired
    CommissionCategorieRepository comcat;
    @Autowired
    MonthlyRevenuRepository monthrevre;
    @Autowired
    YearlyRevenuRepository yearr;
    @Autowired
    EnchereRepository enchereRepository;

    @GetMapping("")
    public String LoginPage(Model model) {
        Admin u = new Admin();
        model.addAttribute("user", u);
        return "Login";
    }

    @PostMapping("/Login")
    public String Login(@ModelAttribute("user") Admin user, Model model) {
        long resp = utilisateurService.checkUser(user);
        if (resp != -1) {
            return loadStatistiques(model);
        } else {
            model.addAttribute("error", "Nom d'Admin ou Mot de Passe Erronee");
            return "Login";
        }
    }

    @GetMapping("/Categorie")
    public String viewCategorie(Model model) {
        model.addAttribute("categorie", new CategorieProduit());
        model.addAttribute("categories", adminService.getAllCategorieProduit());
        return "Categorie";
    }

    @GetMapping("/Commission")
    public String viewCommission(Model model) {
        model.addAttribute("com", new Enchere());
        model.addAttribute("coms", adminService.getEnchereEnCours());
        return "Coms";
    }

    @GetMapping("/deleteCategorie/{id}")
    public String deleteThroughId(@PathVariable(value = "id") long id, Model model) {
        adminService.deleteCategorieViaId(id);
        return viewCategorie(model);
    }

    @PostMapping("/saveCategorie")
    public String saveEmployee(@ModelAttribute("categorie") CategorieProduit cp, Model model) {
        adminService.saveCategorie(cp);
        return viewCategorie(model);
    }

    @PostMapping("/saveComs")
    public String saveComs(@ModelAttribute("Enchere") Enchere e, Model model) {
        adminService.saveComs(e);
        return viewCommission(model);
    }

    @PostMapping("/saveDefault")
    public String saveDefault(@ModelAttribute("Defaultduree") Defaultduree e, Model model) {
        adminService.saveDefault(e);
        return updateDefault(model);
    }

    @GetMapping("/updateCategorie/{id}")
    public String updateCategorie(@PathVariable(value = "id") long id, Model model) {
        CategorieProduit cp = adminService.getCategorieById(id);
        model.addAttribute("categorie", cp);
        return "UpdateCategorie";
    }

    @GetMapping("/updateCommission/{id}")
    public String updateCommission(@PathVariable(value = "id") long id, Model model) {
        Enchere e = adminService.getEnchereById(id);
        model.addAttribute("com", e);
        return "UpdateComs";
    }

    @GetMapping("/updateDefaultValue")
    public String updateDefault(Model model) {
        model.addAttribute("def", adminService.geDefaultduree());
        return "UpdateDefault";
    }

    @GetMapping("/LoadAllSoldes")
    public String loadAllSoldes(Model model) {
        try {
            ArrayList<View_allSoldes> tousLesSoldes = (ArrayList<View_allSoldes>) viewSoldesRepository.findAll();
            model.addAttribute("tousLesSoldes", tousLesSoldes);
            return "AllSoldes";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "AllSoldes";
    }

    @GetMapping("/ValiderSoldes/{id}")
    public String validerSolde(@PathVariable(value = "id") int id, Model model) {
        try {
            SoldeUtilisateurValide s = new SoldeUtilisateurValide();
            s.setIdsoldeutilisateur(id);
            LocalDate today = LocalDate.now();
            s.setDatevalidation(java.sql.Date.valueOf(today));
            suvr.save(s);
            model.addAttribute("ok", "Solde valide avec succes !");
        } catch (Exception e) {
            model.addAttribute("error", "Verifiez qu'il n'y a pas d'erreur");
            e.printStackTrace();
        }
        return loadAllSoldes(model);
    }

    @GetMapping("/LoadStatistiques")
    public String loadStatistiques(Model model) {
        try {
            ArrayList<EncheresParMois> tousLesEPM = (ArrayList<EncheresParMois>) epm.findAll();
            ArrayList<CommissionCategorie> tousLesCC = (ArrayList<CommissionCategorie>) comcat.findAll();
            ArrayList<MonthlyRevenu> tousLesMR = (ArrayList<MonthlyRevenu>) monthrevre.findAll();
            ArrayList<YearlyRevenu> tousLesYR = (ArrayList<YearlyRevenu>) yearr.findAll();
            model.addAttribute("tousLesEPM", tousLesEPM);
            model.addAttribute("tousLesCC", tousLesCC);
            model.addAttribute("tousLesMR", tousLesMR);
            model.addAttribute("tousLesYR", tousLesYR);
            return "AllEPM";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Accueil";
    }

    @GetMapping("/LoadEncheresParMois")
    public String LoadEncheresParMois(Model model) {
        try {
            ArrayList<EncheresParMois> tousLesEPM = (ArrayList<EncheresParMois>) epm.findAll();
            model.addAttribute("tousLesEPM", tousLesEPM);
            return "AllEPM";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "AllEPM";
    }

}
