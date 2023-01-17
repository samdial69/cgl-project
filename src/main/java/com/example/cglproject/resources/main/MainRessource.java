package com.example.cglproject.resources.main;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@Slf4j
public class MainRessource {

    @GetMapping("/")
    public String getHome(Model model){
        log.info("model {}",model.getAttribute("main"));
        return "home";
    }

    @GetMapping("/connexion")
    public String getConnexion(Model model){
        log.info("model {}",model.getAttribute("connexion"));
        return "login/connexion";
    }

    @GetMapping("/inscription")
    public String getInscription(Model model){
        log.info("model {}",model.getAttribute("inscription"));
        return "login/inscription";
    }

    @GetMapping("/mdp-oublie")
    public String getMdpOublie(Model model){
        log.info("model {}",model.getAttribute("mot-de-passe-oublie"));
        return "login/mot-de-passe-oublie";
    }

    @GetMapping("/mdp-oublie-code-verification")
    public String getMdpOublieCV(Model model){
        log.info("model {}",model.getAttribute("mot-de-passe-oublie-code-verification"));
        return "login/mot-de-passe-oublie-code-verification";
    }

    @GetMapping("/mdp-oublie-nouveau-mdp")
    public String getMdpOublieNMdp(Model model){
        log.info("model {}",model.getAttribute("mot-de-passe-oublie-nouveau-mdp"));
        return "login/mot-de-passe-oublie-nouveau-mdp";
    }

    @GetMapping("/error404")
    public String getError(Model model){
        log.info("model {}",model.getAttribute("error404"));
        return "errorPage/error404";
    }

    @GetMapping("/business/tous-les-affaires")
    public String getAllBusiness(Model model){
        log.info("model {}",model.getAttribute("tous les affaires"));
        return "tous-les-affaires";
    }

    @GetMapping("/business/apporteur-daffaires")
    public String getAllBusinessProviders(Model model){
        log.info("model {}",model.getAttribute("tous les affaires"));
        return "apporteur-daffaires";
    }

    @GetMapping("/business/un-AppDaff-directes")
    public String getOneBusinessProviderD(Model model){
        log.info("model {}",model.getAttribute("un apporteur d'affaire : les affaires directes"));
        return "apporteur-daffaires-liste-des-affaires-directes";
    }



}
