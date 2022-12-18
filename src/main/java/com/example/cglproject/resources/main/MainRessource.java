package com.example.cglproject.resources.main;

import com.example.cglproject.models.Parameter;
import com.example.cglproject.services.parameter.ParameterServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
@Slf4j
public class MainRessource {

    @GetMapping("/")
    public String getHome(Model model){
        log.info("model {}",model.getAttribute("main"));
        return "home";
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
}
