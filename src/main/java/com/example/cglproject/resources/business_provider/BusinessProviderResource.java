package com.example.cglproject.resources.business_provider;

import com.example.cglproject.models.BusinessProvider;
import com.example.cglproject.services.business_provider.BusinessProviderServiceImpl;
import com.example.cglproject.services.parameter.IParameterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/business_providers")
@Slf4j
public class BusinessProviderResource {
    private final BusinessProviderServiceImpl service;
    private IParameterService parameterService;
    public BusinessProviderResource(BusinessProviderServiceImpl service, IParameterService parameterService) {
        this.service = service;
        this.parameterService = parameterService;
    }

    @GetMapping("/")
    public String getAllBusinessProviders(Model model) {
        log.info("Getting all business providers");
        List<BusinessProvider> businessProviders = service.getAllBusinessProviders();
        model.addAttribute("providers", this.service.getAllBusinessProviders());
        model.addAttribute("parameter", parameterService.getApplicationParameters());
        return "businessProvidersPages/apporteur-daffaires";
    }

    @GetMapping("/{id}")
    public String getBusinessProviderById(@PathVariable("id") Long id, Model model) {
        Optional<BusinessProvider> optional = this.service.getById(id);
        if (optional.isPresent()) {
            BusinessProvider provider = optional.get();
            log.info("Getting business provider by id: {}", id);
            model.addAttribute("provider", provider);
            model.addAttribute("commissions", provider.getCommissions());
            model.addAttribute("parameter", parameterService.getApplicationParameters());
            return "businessProvidersPages/apporteur-daffaires-liste-de-tous-les-affaires";
        }
        log.info("Business provider with id: {} not found", id);
        model.addAttribute("provider", Optional.empty());
        return "/error/error404";
    }


    @GetMapping("/create")
    public String form(Model model){
        model.addAttribute("provider",new BusinessProvider());
        model.addAttribute("providers",this.service.getAllBusinessProviders() );
        return "businessProvidersPages/add-business-providers";
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
            model.addAttribute("providers", this.service.getAllBusinessProviders());
            return "businessProvidersPages/edit-business-providers";
        }
        log.info("Business provider with id: {} not found", id);
         model.addAttribute("provider", Optional.empty());
        return "/error/error404";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable("id") Long id, @ModelAttribute("provider") BusinessProvider provider){
        log.info("Business provider: {}",provider);
        BusinessProvider updatedProvider =  service.update(id, provider);
        if (updatedProvider == null) {
            return "errors/error404";
        }
        //return "redirect:/business_providers/";
     
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        log.info("Deleting business provider with id: {}",id);
        boolean isDeleted =  service.delete(id);
        if (!isDeleted) {
            return "errors/error404";
        }
        //return "redirect:/business_providers/";
        return "redirect:/";
    }

    @GetMapping("/DirecteBusinesses/{id}")
    public String getDirectBusinesses(@PathVariable("id") Long id, Model model) {
        log.info("Showing direct business of the business provider with id: {}", id);
        Optional<BusinessProvider> provider = this.service.getById(id);
        model.addAttribute("provider", provider.get());
        model.addAttribute("businesses", this.service.findByIdBusinessProvider(id));
        return "businessProvidersPages/apporteur-daffaires-liste-des-affaires-directes";
    }

    @GetMapping("/ParrainsOfProvider/{id}")
    public String getSponsorsOfBusinessProvider(@PathVariable("id") Long id, Model model) {
        log.info("Showing sponsors of the business provider with id: {}", id);
        Optional<BusinessProvider> provider = this.service.getById(id);
        model.addAttribute("provider", provider.get());
        model.addAttribute("providers", this.service.getProviderAndAllSponsors(id));
        model.addAttribute("parameter", parameterService.getApplicationParameters());
        return "businessProvidersPages/apporteur-daffaires-liste-des-parrains";
    }

}
