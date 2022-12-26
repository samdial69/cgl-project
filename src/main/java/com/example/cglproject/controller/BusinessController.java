package com.example.cglproject.controller;

import com.example.cglproject.models.Business;
import com.example.cglproject.repositories.BusinessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BusinessController {
    @Autowired
    private BusinessRepository eRepo;

    @GetMapping({"/all-businesses"})
    public ModelAndView getAllBusiness() {
        ModelAndView mav = new ModelAndView("tous-les-affaires");
        mav.addObject("businesses", eRepo.findAll());
        return mav;
    }

    @GetMapping("/addBusinessForm")
    public ModelAndView addBusinessForm() {
        ModelAndView mav = new ModelAndView("businessOPages/add-businesses");
        Business newBusiness = new Business();
        mav.addObject("business", newBusiness);
        return mav;
    }

    @PostMapping("/saveBusiness")
    public String saveBusiness(@ModelAttribute Business business) {
        eRepo.save(business);
        return "redirect:/all-businesses";
    }

    @GetMapping("/showUpdateForm")
    public ModelAndView showUpdateForm(@RequestParam Long businessId) {
        ModelAndView mav = new ModelAndView("businessOPages/add-businesses");
        Business business = eRepo.findById(businessId).get();
        mav.addObject("business", business);
        return mav;
    }

    @GetMapping("/deleteBusiness")
    public String deleteBusiness(@RequestParam Long businessId) {
        eRepo.deleteById(businessId);
        return "redirect:/all-businesses";
    }
}
