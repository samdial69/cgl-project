package com.example.cglproject.resources.business;

import com.example.cglproject.models.Business;
import com.example.cglproject.models.BusinessProvider;
import com.example.cglproject.repositories.BusinessProviderRepository;
import com.example.cglproject.services.business.BusinessServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("/businesses")
@Slf4j
public class BusinessResource {
    private final BusinessServiceImpl service;

    public BusinessResource(BusinessServiceImpl service) {
        this.service = service;
    }

    @GetMapping("/")
    public String getBusinesses(Model model){
        log.info("Get all businesses");
        model.addAttribute("businesses",this.service.getAllBusinesses());
        return "business/index";
    }

    @GetMapping("/{id}")
    public String getBusinessById(@PathVariable("id") Long id, Model model){
        log.info("Get business by id {}",id);
        model.addAttribute("business",this.service.findById(id).get());
        return "business/show";
    }

    @GetMapping("/create")
    public String form(Model model){
        model.addAttribute("business",new Business());
        return "business/add";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("business") Business business){
        log.info("Business: {}",business);
        service.create(business);
        return "redirect:/businesses/";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model){
        Optional<Business> business = this.service.findById(id);
        if (business.isEmpty()) {
            model.addAttribute("business", null);
        }else {
            model.addAttribute("business", business.get());
        }
        log.info("Get business by id {}",id);
        return "business/edit";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable("id") Long id,@ModelAttribute("business") Business business){
        log.info("Business updated by id {}: {}",id,business);
        service.update(id,business);
        return "redirect:/businesses/";
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        log.info("Delete business by id {}",id);
        service.delete(id);
        return "redirect:/businesses/";
    }


}
