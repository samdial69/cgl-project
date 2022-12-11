package com.example.cglproject.resources.parameter;


import com.example.cglproject.models.Parameter;
import com.example.cglproject.services.parameter.ParameterServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/parameters")
@Slf4j
public class ParameterResource {
    private final ParameterServiceImpl service;

    public ParameterResource(ParameterServiceImpl service) {
        this.service = service;
    }

    @GetMapping("/")
    public String getParameter(Model model){
        Optional<Parameter> parameter = this.service.findById(6L);
        if(parameter.isEmpty()) {
            model.addAttribute("parameter", null);
        }
        model.addAttribute("parameter",parameter.get());
        log.info("model {}",model.getAttribute("parameter"));
        return "parameter/index";
    }

    @GetMapping("/create")
    public String form(Model model){
        model.addAttribute("parameter",new Parameter());
        return "parameter/add";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("parameter") Parameter parameter){
        log.info("Parameter: {}",parameter);
        service.save(parameter);
        return "redirect:/parameters/";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model){
        Optional<Parameter> parameter = this.service.findById(id);
        if(parameter.isEmpty()) {
            model.addAttribute("parameter", null);
        }
        model.addAttribute("parameter",parameter.get());
        return "parameter/edit";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable("id") Long id,@ModelAttribute("parameter") Parameter parameter){
        log.info("Parameter updated by id {}: {}",id,parameter);
        service.update(id,parameter);
        return "redirect:/parameters/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        log.info("Parameter delete by id: {}",id);
        service.delete(id);
        return "redirect:/parameters/";
    }

}
