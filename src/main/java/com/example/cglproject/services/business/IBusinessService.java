package com.example.cglproject.services.business;

import com.example.cglproject.models.Business;
import com.example.cglproject.models.BusinessProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IBusinessService {

    Page<Business> getAllBusinesses(Pageable pageable);

    List<Business> getAllBusinesses();

    Optional<Business> findById(Long id);

    Business create(Business business);

    Business create(String title, double initialCommission, long providerId);

    Business update(Long id, Business business);

    boolean delete(Long id);
    boolean delete(Business business);
    boolean delete(List<Business> businesses);

}
