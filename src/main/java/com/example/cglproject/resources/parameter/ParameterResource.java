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
        model.addAttribute("parameters",parameters);
        log.info("model {}",model.getAttribute("parameter"));
        return "parameterPages/parameter";
    }

    @GetMapping("/create")
    public String form(Model model){
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
            model.addAttribute("parameter", null);
            //return "errors/error404";
            return "redirect:/parametres/create";
        }
        model.addAttribute("parameter",parameter.get());
        return "parameterPages/parameter";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable("id") Long id,@ModelAttribute("parameter") Parameter parameter){
        log.info("Parameter updated by id {}: {}",id,parameter);

        Parameter param = service.update(id,parameter);
        if (param == null) {
            return "errors/error404";
        }
        //return "redirect:/parameters/";
        return "redirect:/";

    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        log.info("Parameter delete by id: {}",id);
        boolean isDeleted = service.delete(id);
        if (!isDeleted) {
            return "errors/error404";
        }
        //return "redirect:/parameters/";
        return "redirect:/";
    }

}
