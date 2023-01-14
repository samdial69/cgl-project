package com.example.cglproject.controller;

import com.example.cglproject.models.BusinessProvider;
import com.example.cglproject.repositories.BusinessProviderRepository;
import com.example.cglproject.services.business_provider.IBusinesProviderService;
import com.example.cglproject.services.parameter.IParameterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Slf4j
public class BusinessProviderController {

    private BusinessProviderRepository eRepo;
    private IBusinesProviderService businesProviderService;
    private IParameterService parameterService;

    public BusinessProviderController(BusinessProviderRepository eRepo, IParameterService parameterService, IBusinesProviderService businesProviderService) {
        this.eRepo = eRepo;
        this.parameterService = parameterService;
        this.businesProviderService = businesProviderService;
    }

    @GetMapping({"/all-business-providers"})
    public ModelAndView getAllBusinessProviders(){
        ModelAndView mav = new ModelAndView("apporteur-daffaires");
        mav.addObject("businessProviders", eRepo.findAll());
        mav.addObject("parameter", parameterService.getApplicationParameters());
        return mav;
    }

    @GetMapping("/addBusinessProviderForm")
    public ModelAndView addBusinessProviderForm() {
        ModelAndView mav = new ModelAndView("businessProvidersOPages/add-business-providers");
        BusinessProvider newBusinessProvider = new BusinessProvider();
        mav.addObject("businessProvider", newBusinessProvider);
        mav.addObject("businessProviders", eRepo.findAll());
        return mav;
    }

    @PostMapping("/saveBusinessProvider")
    public String saveBusinessProvider(@ModelAttribute BusinessProvider businessProvider) {
        eRepo.save(businessProvider);
        return "redirect:/all-business-providers";
    }

    @GetMapping("/showUpdateBPForm")
    public ModelAndView showUpdateBPForm(@RequestParam Long businessPId) {
        ModelAndView mav = new ModelAndView("businessProvidersOPages/add-business-providers");
        BusinessProvider businessProvider = eRepo.findById(businessPId).get();
        mav.addObject("businessProvider", businessProvider);
        return mav;
    }



    @GetMapping("/deleteBusinessProvider")
    public String deleteBusinessProvider(@RequestParam Long businessPId) {
        this.businesProviderService.delete(businessPId);
        return "redirect:/all-business-providers";
    }

}
