package com.example.cglproject.services.business_provider;

import com.example.cglproject.models.Business;
import com.example.cglproject.models.BusinessProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IBusinesProviderService {

    Page<BusinessProvider> getAllBusinessProviders(Pageable pageable);

    List<BusinessProvider> getAllBusinessProviders();

    Optional<BusinessProvider> getById(Long id);
    BusinessProvider create(BusinessProvider businessProvider);
    BusinessProvider update(Long id, BusinessProvider businessProvider);

    boolean delete(Long id);


    List<Business> findByIdBusinessProvider(Long IdBusinessProvider);

    List<Business> findByIdBusinessProviderAndAffile(Long IdBusinessProvider);
}
