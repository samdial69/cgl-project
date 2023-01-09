package com.example.cglproject.services.business_provider;

import com.example.cglproject.models.Business;
import com.example.cglproject.models.BusinessProvider;
import com.example.cglproject.repositories.BusinessProviderRepository;
import com.example.cglproject.repositories.BusinessRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class BusinessProviderServiceImpl implements IBusinesProviderService {
    private final BusinessProviderRepository service;
    private final BusinessRepository serviceBusiness;

    public BusinessProviderServiceImpl(BusinessProviderRepository service, BusinessRepository serviceBusiness) {
        this.service = service;
        this.serviceBusiness = serviceBusiness;
    }
    @Override
    public Page<BusinessProvider> getAllBusinessProviders(Pageable pageable) {
        log.info("Getting all business providers with pagination");
        return this.service.findAll(pageable);
    }

    @Override
    public List<BusinessProvider> getAllBusinessProviders() {
        log.info("Getting all business providers");
        return this.service.findAll();
    }

    @Override
    public Optional<BusinessProvider> getById(Long id) {
        log.info("Getting business provider by id: {}", id);
        return this.service.findById(id);
    }

    @Override
    public BusinessProvider create(BusinessProvider businessProvider) {
        log.info("Creating business provider: {}", businessProvider);
        return this.service.save(businessProvider);
    }

    @Override
    public BusinessProvider update(Long id, BusinessProvider businessProvider) {
        Optional<BusinessProvider> currentBusinessProvider = this.service.findById(id);
        if (currentBusinessProvider.isPresent()) {
            log.info("Updating business provider: {}", businessProvider);
            businessProvider.setId(currentBusinessProvider.get().getId());
            return this.service.save(businessProvider);
        }
        return null;
    }

    @Override
    public boolean delete(Long id) {
        Optional<BusinessProvider> currentBusinessProvider = this.service.findById(id);
        if (currentBusinessProvider.isPresent()) {
            log.info("Deleting business provider: {}", currentBusinessProvider.get());
            this.service.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<Business> findByIdBusinessProvider(Long IdBusinessProvider) {
        return this.serviceBusiness.findByIdBusinessProvider(IdBusinessProvider);
    }

    @Override
    public List<Business> findByIdBusinessProviderAndAffile(Long IdBusinessProvider) {
        return this.serviceBusiness.findByIdBusinessProviderAndIsSponsored(IdBusinessProvider);
    }

}
