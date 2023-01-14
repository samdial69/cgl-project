package com.example.cglproject.controller;

import com.example.cglproject.models.Business;
import com.example.cglproject.models.BusinessProvider;
import com.example.cglproject.repositories.BusinessRepository;
import com.example.cglproject.services.business.IBusinessService;
import com.example.cglproject.services.business_provider.IBusinesProviderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class BusinessController {

    private BusinessRepository eRepo;
    private IBusinesProviderService providerService;
    private IBusinessService businessService;

    public BusinessController(BusinessRepository eRepo, IBusinesProviderService providerService, IBusinessService businessService) {
        this.eRepo = eRepo;
        this.providerService = providerService;
        this.businessService = businessService;
    }

    @GetMapping({"/all-businesses"})
    public ModelAndView getAllBusiness() {
        ModelAndView mav = new ModelAndView("tous-les-affaires");
        mav.addObject("businesses", eRepo.findAll());
        return mav;
    }

    @GetMapping("/addBusinessForm")
    public ModelAndView addBusinessForm() {
        ModelAndView mav = new ModelAndView("add-business");
        List<BusinessProvider> businessProviders = providerService.getAllBusinessProviders();
        mav.addObject("businessProviders", businessProviders);
        return mav;
    }

    @PostMapping("/saveBusiness")
    public String saveBusiness(@RequestParam String title, @RequestParam long providerId, @RequestParam double initialCommission) {
        this.businessService.create(title, initialCommission, providerId);
        return "redirect:/all-businesses";
    }

    @GetMapping("/showUpdateForm")
    public ModelAndView showUpdateForm(@RequestParam Long businessId) {
        ModelAndView mav = new ModelAndView("add-business");
        Business business = eRepo.findById(businessId).get();
        mav.addObject("business", business);
        return mav;
    }

    @GetMapping("/deleteBusiness")
    public String deleteBusiness(@RequestParam Long businessId) {
        providerService.delete(businessId);
        return "redirect:/all-businesses";
    }
}
