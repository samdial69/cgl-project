package com.example.cglproject.resources.commission;

import com.example.cglproject.models.Commission;
import com.example.cglproject.services.comission.CommissionServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/commissions")
@Slf4j
public class RestCommissionController {
    private final CommissionServiceImpl service;


    public RestCommissionController(CommissionServiceImpl service) {
        this.service = service;
    }

    @GetMapping("/")
    public ResponseEntity<Page<Commission>> getAllCommission(@PageableDefault Pageable pageable){
        return new ResponseEntity<>(this.service.getAllCommissions(pageable), HttpStatus.OK);
    }
}
