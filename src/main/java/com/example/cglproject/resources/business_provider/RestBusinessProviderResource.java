package com.example.cglproject.resources.business_provider;

import com.example.cglproject.models.BusinessProvider;
import com.example.cglproject.services.business_provider.BusinessProviderServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/business-providers")
@Slf4j
public class RestBusinessProviderResource {
    private final BusinessProviderServiceImpl service;
    public RestBusinessProviderResource(BusinessProviderServiceImpl service) {
        this.service = service;
    }

    @GetMapping("/")
    public ResponseEntity<Page<BusinessProvider>> getAllBusinessProviders(@PageableDefault Pageable pageable) {
        log.info("Getting all business providers");
        return new ResponseEntity<>(this.service.getAllBusinessProviders(pageable), HttpStatus.OK);
    }
}
