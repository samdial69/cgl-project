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
        return "parameterPages/parameter";
    }

    @GetMapping("/create")
    public String form(Model model){
        model.addAttribute("parameter",new Parameter(0L, 5,5,50,1,3));
        return "parameterPages/newParameter";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("parameter") Parameter parameter){
        log.info("Parameter: {}",parameter);
        service.save(parameter);
        return "redirect:/";
    }

    @GetMapping("/edit")
    public String edit(Model model){
        Optional<Parameter> parameter = Optional.ofNullable(this.service.getApplicationParameters());
        if(parameter.isEmpty()) {
            return "redirect:/parametres/create";
        }
        model.addAttribute("parameter",parameter.get());
        return "parameterPages/parameter";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable("id") Long id,@ModelAttribute("parameter") Parameter parameter){
        log.info("Parameter updated by id {}: {}",id,parameter);
        service.update(id,parameter);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        log.info("Parameter delete by id: {}",id);
        service.delete(id);
        return "redirect:/";
    }

}
