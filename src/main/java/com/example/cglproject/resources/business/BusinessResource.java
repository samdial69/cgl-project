package com.example.cglproject.resources.business;

import com.example.cglproject.models.Business;
import com.example.cglproject.services.business.BusinessServiceImpl;
import com.example.cglproject.services.business_provider.IBusinesProviderService;
import com.example.cglproject.services.comission.ICommissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/businesses")
@Slf4j
public class BusinessResource {
    private final BusinessServiceImpl service;

    private IBusinesProviderService providerService;

    private ICommissionService commissionService;

    public BusinessResource(BusinessServiceImpl service, IBusinesProviderService providerService, ICommissionService commissionService) {
        this.service = service;
        this.providerService = providerService;
        this.commissionService = commissionService;
    }

    @GetMapping("/")
    public String getBusinesses(Model model){
        List<Business> businesses = this.service.getAllBusinesses();
        log.info("Get all businesses");
        model.addAttribute("businesses",businesses);
        return "businessPages/tous-les-affaires";
    }


    // voir oour garder ou effacer
    @GetMapping("/{id}")
    public String getBusinessById(@PathVariable("id") Long id, Model model){
        Optional<Business> business = this.service.findById(id);
        if (business.isEmpty()) {
            model.addAttribute("business", null);
            return "error/error404";
        }
        log.info("Get business by id {}",id);
        model.addAttribute("business",business.get());
        model.addAttribute("provider", business.get().getProvider());
        model.addAttribute("commissions", business.get().getCommissions());
        return "businessPages/edit-business";
    }

    @GetMapping("/create")
    public String form(Model model){
        model.addAttribute("business",new Business());
        model.addAttribute("providers", providerService.getAllBusinessProviders());
        return "businessPages/add-business";
    }

    @PostMapping("/create")
    public String create(@RequestParam String title, @RequestParam long providerId, @RequestParam double initialCommission){
        log.info("Business: {} {} {}", title, initialCommission, providerId);
        this.service.create(title, initialCommission, providerId);
        return "redirect:/businesses/";
    }


    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model){
        Optional<Business> business = this.service.findById(id);
        if (business.isEmpty()) {
            model.addAttribute("business", Optional.empty());
            return "error/error404";
        }
        model.addAttribute("business",business.get());
        model.addAttribute("provider", business.get().getProvider());
        log.info("Get business by id {}",id);
        return "businessPages/edit-business";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable("id") Long id,@ModelAttribute("business") Business business){
        log.info("Business updated by id {}: {}",id,business);
        Business updatedBusiness = service.update(id,business);
        if (updatedBusiness == null) {
            return "error/error404";
        }

        return "redirect:/businesses/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        log.info("Delete business by id {}",id);
        boolean isDeleted = service.delete(id);
        if (!isDeleted) {
            return "error/error404";
        }
        return "redirect:/businesses/";
    }


}
