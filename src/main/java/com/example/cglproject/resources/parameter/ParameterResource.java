package com.example.cglproject.resources.parameter;


import com.example.cglproject.models.Parameter;
import com.example.cglproject.services.parameter.ParameterServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/parametres")
@Slf4j
public class ParameterResource {
    private final ParameterServiceImpl service;

    public ParameterResource(ParameterServiceImpl service) {
        this.service = service;
    }

    @GetMapping("/")
    public String getParameter(Model model){
        List<Parameter> parameters = this.service.findAll();
        if(parameters.isEmpty()) {
            model.addAttribute("parameters", null);
            //TODO return error page
        }
        model.addAttribute("parameters",parameters);
        log.info("model {}",model.getAttribute("parameter"));
        return "parametres";
    }

    @GetMapping("/create")
    public String form(Model model){
        model.addAttribute("parameter",new Parameter());
        return "parametres";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("parameter") Parameter parameter){
        log.info("Parameter: {}",parameter);
        service.save(parameter);
        return "redirect:/parametres";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model){
        Optional<Parameter> parameter = this.service.findById(id);
        if(parameter.isEmpty()) {
            model.addAttribute("parameter", null);
            //TODO return error page
        }
        model.addAttribute("parameter",parameter.get());
        return "parametres";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable("id") Long id,@ModelAttribute("parameter") Parameter parameter){
        log.info("Parameter updated by id {}: {}",id,parameter);
        service.update(id,parameter);
        return "redirect:/parametres/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        log.info("Parameter delete by id: {}",id);
        service.delete(id);
        return "redirect:/parametres/";
    }

}
