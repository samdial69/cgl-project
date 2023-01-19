package com.example.cglproject.resources.commission;

import com.example.cglproject.models.Commission;
import com.example.cglproject.services.comission.CommissionServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/commissions")
@Slf4j
public class CommissionRessource {
    private final CommissionServiceImpl service;


    public CommissionRessource(CommissionServiceImpl service) {
        this.service = service;
    }

    @GetMapping("/")
    public String getCommission(Model model){
        log.info("Get all commission");
        model.addAttribute("commissions",this.service.getAllCommissions());
        return "commission/index";
    }

    @GetMapping("/{id}")
    public String getCommissionById(@PathVariable("id") Long id, Model model){
        log.info("Get Commission by id {}", id);
        model.addAttribute("commission",this.service.findById(id).get());
        return "commission/show";
    }

    @GetMapping("/create")
    public String form(Model model){
        model.addAttribute("commission", new Commission());
        return "commission/add";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("commission") Commission commission){
        log.info("Commission: {}",commission);
        service.create(commission);
        return "redirect:/commissions/";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model){
        Optional<Commission> commission = this.service.findById(id);
        if (commission.isEmpty()) {
            model.addAttribute("commission", null);
        }else {
            model.addAttribute("commission", commission.get());
        }
        log.info("Get commission by id {}",id);
        return "commission/edit";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable("id") Long id,@ModelAttribute("commission") Commission commission){
        log.info("Commission updated by id {}: {}",id,commission);
        service.update(id,commission);
        return "redirect:/commissions/";
    }

    @DeleteMapping(value= "/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        log.info("Delete commission by id {}",id);
        service.delete(id);
        return "redirect:/commissions/";
    }

}
