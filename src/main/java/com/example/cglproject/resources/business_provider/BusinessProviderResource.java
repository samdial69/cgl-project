package com.example.cglproject.resources.business_provider;

import com.example.cglproject.models.Business;
import com.example.cglproject.models.BusinessProvider;
import com.example.cglproject.services.business.BusinessServiceImpl;
import com.example.cglproject.services.business_provider.BusinessProviderServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/business_providers")
@Slf4j
public class BusinessProviderResource {
    private final BusinessProviderServiceImpl service;
    public BusinessProviderResource(BusinessProviderServiceImpl service, BusinessServiceImpl serviceBusiness) {
        this.service = service;
    }

    @GetMapping("/")
    public String getAllBusinessProviders(Model model) {
        log.info("Getting all business providers");
        model.addAttribute("providers", this.service.getAllBusinessProviders());
        return "business_provider/index";
    }

    @GetMapping("/{id}")
    public String getBusinessProviderById(@PathVariable("id") Long id, Model model) {
        Optional<BusinessProvider> provider = this.service.getById(id);
        if (provider.isPresent()) {
            log.info("Getting business provider by id: {}", id);
            model.addAttribute("provider", provider.get());
            return "business_provider/show";
        }
        log.info("Business provider with id: {} not found", id);
        //TODO add error 404 page instead of index
        return "business_provider/index";
    }

    @GetMapping("/create")
    public String form(Model model){
        model.addAttribute("provider",new BusinessProvider());
        return "business_provider/add";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("provider") BusinessProvider provider){
        log.info("Business provider: {}",provider);
        service.create(provider);
        return "redirect:/business_providers/";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model){
        Optional<BusinessProvider> provider = this.service.getById(id);
        if (provider.isPresent()) {
            log.info("Getting business provider by id: {}", id);
            model.addAttribute("provider", provider.get());
            return "business_provider/edit";
        }
        //TODO add error 404 page instead of index
        log.info("Business provider with id: {} not found", id);
        return "business_provider/index";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable("id") Long id, @ModelAttribute("provider") BusinessProvider provider){
        log.info("Business provider: {}",provider);
        service.update(id, provider);
        return "redirect:/business_providers/";
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        log.info("Deleting business provider with id: {}",id);
        service.delete(id);
        return "redirect:/business_providers/";
    }

    @GetMapping("/viewBusinessProvider")
    public ModelAndView ViewBusinessProvider(@RequestParam Long businessPId) {
        ModelAndView mav = new ModelAndView("apporteur-daffaires-liste-de-tous-les-affaires");
        Optional<BusinessProvider> businessProvider = this.service.getById(businessPId);
        mav.addObject("businessProvider", businessProvider.get());
        mav.addObject("businesses", this.service.findByIdBusinessProvider(businessPId));
        return mav;
    }

    @GetMapping("/viewBusinessProviderDirecte")
    public ModelAndView ViewBusinessProviderDirecte(@RequestParam Long businessPId) {
        ModelAndView mav = new ModelAndView("apporteur-daffaires-liste-des-affaires-directes");
        Optional<BusinessProvider> businessProvider = this.service.getById(businessPId);
        mav.addObject("businessProvider", businessProvider.get());
        mav.addObject("businesses", this.service.findByIdBusinessProviderAndAffile(businessPId));
        return mav;
    }

    @GetMapping("/viewBusinessProviderParrains")
    public ModelAndView ViewBusinessProviderParrains(@RequestParam Long businessPId) {
        ModelAndView mav = new ModelAndView("apporteur-daffaires-liste-des-parrains");
        Optional<BusinessProvider> businessProvider = this.service.getById(businessPId);
        mav.addObject("businessProvider", businessProvider.get());
        mav.addObject("businesses", this.service.findByIdBusinessProviderAndAffile(businessPId));
        return mav;
    }
}
