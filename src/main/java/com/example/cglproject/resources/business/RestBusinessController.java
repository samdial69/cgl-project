package com.example.cglproject.resources.business;

import com.example.cglproject.models.Business;
import com.example.cglproject.services.business.BusinessServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/businesses")
@Slf4j
public class RestBusinessController {
    private final BusinessServiceImpl service;

    public RestBusinessController(BusinessServiceImpl service) {
        this.service = service;
    }

    @GetMapping("/")
    public ResponseEntity<Page<Business>> getAllBusinesses(@PageableDefault Pageable pageable){
        return new ResponseEntity<>(this.service.getAllBusinesses(pageable), HttpStatus.OK);
    }

}
