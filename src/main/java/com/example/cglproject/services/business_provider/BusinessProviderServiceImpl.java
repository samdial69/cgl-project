package com.example.cglproject.services.business_provider;

import com.example.cglproject.models.Business;
import com.example.cglproject.models.BusinessProvider;
import com.example.cglproject.models.Commission;
import com.example.cglproject.repositories.BusinessProviderRepository;
import com.example.cglproject.repositories.BusinessRepository;
import com.example.cglproject.services.business.IBusinessService;
import com.example.cglproject.services.comission.ICommissionService;
import com.example.cglproject.services.parameter.IParameterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class BusinessProviderServiceImpl implements IBusinesProviderService {
    private BusinessProviderRepository repository;
    private IBusinessService businessService;
    private ICommissionService commissionService;

    private IParameterService parameterService;

    public BusinessProviderServiceImpl(BusinessProviderRepository repository, IBusinessService businessService, ICommissionService commissionService, IParameterService parameterService) {
        this.repository = repository;
        this.businessService = businessService;
        this.commissionService = commissionService;
        this.parameterService = parameterService;
    }
    @Override
    public Page<BusinessProvider> getAllBusinessProviders(Pageable pageable) {
        log.info("Getting all business providers with pagination");
        return this.repository.findAll(pageable);
    }

    @Override
    public List<BusinessProvider> getAllBusinessProviders() {
        log.info("Getting all business providers");
        return this.repository.findAll();
    }

    @Override
    public Optional<BusinessProvider> getById(Long id) {
        log.info("Getting business provider by id: {}", id);
        return this.repository.findById(id);
    }

    // TODO: supprimer l'apporteur original et ne donner que les parrains
    @Override
    public List<BusinessProvider> getProviderAndAllSponsors(Long id) {
        List<BusinessProvider> result = new ArrayList<BusinessProvider>();
        Optional<BusinessProvider> optional = this.getById(id);
        if (optional.isPresent()) {
            BusinessProvider provider = optional.get().getSponsor();
            while (provider != null) {
                result.add(provider);
                provider = provider.getSponsor();
            }
        }
        return result;
    }

    @Override
    public BusinessProvider create(BusinessProvider businessProvider) {
        log.info("Creating business provider: {}", businessProvider);
        if (businessProvider.getSponsor().getId() == null) {
            businessProvider.setSponsorNull();
        }
        return this.repository.save(businessProvider);
    }

    @Override
    public BusinessProvider update(Long id, BusinessProvider businessProvider) {
        Optional<BusinessProvider> currentBusinessProvider = this.repository.findById(id);
        if (currentBusinessProvider.isPresent()) {
            log.info("Updating business provider: {}", businessProvider);
            businessProvider.setId(currentBusinessProvider.get().getId());
            if (businessProvider.getSponsor().getId() == null) {
                businessProvider.setSponsorNull();
            }
            return this.repository.save(businessProvider);
        }
        return null;
    }

    @Override
    public boolean delete(Long id) {
        Optional<BusinessProvider> currentBusinessProvider = this.repository.findById(id);
        if (currentBusinessProvider.isPresent()) {
            this.delete(currentBusinessProvider.get());
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(BusinessProvider businessProvider) {
        BusinessProvider sponsor = businessProvider.getSponsor();
        // deleting the commissions he received
        List<Commission> commissions = businessProvider.getCommissions();
        this.commissionService.delete(commissions);
        // reassign the businesses he provided to his sponsor, or deleting them if he didn't have a sponsor
        List<Business> businesses = businessProvider.getBusinesses();
        if (businesses != null) {
            if (sponsor == null) {
                this.businessService.delete(businesses);
            } else {
                for (Business business : businesses) {
                    business.setProvider(sponsor);
                    this.businessService.update(business.getId(), business);
                }
            }
        }
        // assign his own sponsor to the providers he sponsored
        List<BusinessProvider> sponsored = businessProvider.getSponsored();
        for (BusinessProvider sponsoredProvider : sponsored) {
            if (sponsor == null) {
                sponsoredProvider.setSponsorNull();
            } else {
                sponsoredProvider.setSponsor(sponsor);
            }
            this.update(sponsoredProvider.getId(), sponsoredProvider);
        }
        // now deleting the actual business provider
        this.repository.delete(businessProvider);
        return true;
    }

    @Override
    public List<Business> findByIdBusinessProvider(Long IdBusinessProvider) {
        Optional<BusinessProvider> optionalProvider = this.repository.findById(IdBusinessProvider);
        if ( ! optionalProvider.isEmpty()) {
            BusinessProvider provider = optionalProvider.get();
            return provider.getBusinesses();
        } else {
            return null;
        }
    }

    /*@Override
    public List<Business> findByIdBusinessProviderAndAffile(Long IdBusinessProvider) {
        Optional<BusinessProvider> optionalProvider = this.service.findById(IdBusinessProvider);
        if ( ! optionalProvider.isEmpty()) {
            BusinessProvider provider = optionalProvider.get();
            if (provider.isAffiliated(parameterService.getApplicationParameters())) {
                return provider.getBusinesses();
            }
        }
        return null;
    }*/

}
