package com.example.cglproject.resources;


import com.example.cglproject.models.Parameter;
import com.example.cglproject.services.parameter.ParameterServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/parameters")
@Slf4j
public class ParameterResource {

    private final ParameterServiceImpl service;

    public ParameterResource(ParameterServiceImpl service) {
        this.service = service;
    }

    @GetMapping("/")
    public String getParameter(){
        return "Parameter";
    }

    @GetMapping("/create")
    public String form(Model model){
        model.addAttribute("parameter",new Parameter());
        return "AddParameter";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("parameter") Parameter parameter){
        log.info("Parameter: {}",parameter);
        service.save(parameter);
        return "Parameter";
    }

}
