package com.example.cglproject.services.business;

import com.example.cglproject.models.Business;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IBusinessService {

    Page<Business> getAllBusinesses(Pageable pageable);

    List<Business> getAllBusinesses();

    Optional<Business> findById(Long id);

    Business create(Business business);

    Business update(Long id, Business business);

    boolean delete(Long id);
}
