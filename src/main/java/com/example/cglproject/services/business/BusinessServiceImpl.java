package com.example.cglproject.services.business;

import com.example.cglproject.models.Business;
import com.example.cglproject.models.BusinessProvider;
import com.example.cglproject.models.Commission;
import com.example.cglproject.models.Parameter;
import com.example.cglproject.repositories.BusinessProviderRepository;
import com.example.cglproject.repositories.BusinessRepository;
import com.example.cglproject.services.comission.ICommissionService;
import com.example.cglproject.services.parameter.IParameterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class BusinessServiceImpl implements IBusinessService {

    private BusinessRepository repository;
    private BusinessProviderRepository providerRepository;
    private ICommissionService commissionService;
    private IParameterService parameterService;

    public BusinessServiceImpl(BusinessRepository repository, ICommissionService commissionService, BusinessProviderRepository providerRepository, IParameterService parameterService) {
        this.repository = repository;
        this.commissionService = commissionService;
        this.providerRepository = providerRepository;
        this.parameterService = parameterService;
    }

    @Override
    public Page<Business> getAllBusinesses(Pageable pageable) {
        log.info("Fetching all businesses returning pages");
        return this.repository.findAll(pageable);
    }

    @Override
    public List<Business> getAllBusinesses() {
        log.info("Fetching all businesses");
        return this.repository.findAll();
    }

    @Override
    public Optional<Business> findById(Long id) {
        log.info("Fetching a business by id : {}",id);
        return this.repository.findById(id);
    }

    @Override
    public Business create(Business business) {
        log.info("Creating a new business : {}",business);
        return this.repository.save(business);
    }

    @Override
    public Business create(String title, double initialCommission, long providerId) {
        BusinessProvider provider = this.providerRepository.findById(providerId).get();
        LocalDate now = LocalDate.now();
        // creating the basic business
        Business business = new Business();
        business.setCreatedAt(now);
        business.setTitle(title);
        business.setProvider(provider);
        // creating the commissions
        Parameter parameter = this.parameterService.getApplicationParameters();
        int sponsorsToCommission = parameter.getLevelOfSponsorship();
        double initialCommissionMultiplier = parameter.getPercentageOfInitialCommission() / 100;
        double nextCommissionMultiplier = parameter.getPercentageOfNextCommission() / 100;
        // loop initialization
        BusinessProvider currentSponsor = provider;
        double allSponsorCommissions = 0;
        double nextCommission = initialCommission * initialCommissionMultiplier;
        List<Commission> commissions = new ArrayList<Commission>();
        Commission currentCommission;
        while (sponsorsToCommission > 0) {
            currentSponsor = currentSponsor.getSponsor();
            if (currentSponsor == null) {
                sponsorsToCommission = 0;
            } else {
                if (currentSponsor.isAffiliated(parameter)) {
                    currentCommission = new Commission();
                    currentCommission.setBusiness(business);
                    currentCommission.setRecipient(currentSponsor);
                    currentCommission.setCommission(nextCommission);
                    allSponsorCommissions += nextCommission;
                    commissions.add(currentCommission);
                }
                sponsorsToCommission--;
                nextCommission = nextCommission * nextCommissionMultiplier; // first one is multiplied with a different number
            }
        }
        // commission of the initial provider
        Commission providerCommission = new Commission();
        providerCommission.setBusiness(business);
        providerCommission.setRecipient(provider);
        providerCommission.setCommission(initialCommission - allSponsorCommissions);
        commissions.add(providerCommission);
        business.setCommissions(commissions);
        // persisting
        this.repository.save(business);
        for (Commission commission : commissions) {
            this.commissionService.create(commission);
        }
        return business;
    }

    @Override
    public Business update(Long id, Business business) {
        Optional<Business> currentBusiness = this.findById(id);
        if(currentBusiness.isPresent()){
            log.info("Updating business by id : {}",id);
            business.setId(currentBusiness.get().getId());
            return this.repository.save(business);
        }
        return null;
    }

    @Override
    public boolean delete(Long id) {
        Optional<Business> currentBusiness = this.findById(id);
        if(currentBusiness.isPresent()){
            this.delete(currentBusiness.get());
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(Business business) {
        Optional<Business> currentBusiness = this.findById(business.getId());
        if(currentBusiness.isPresent()){
            log.info("Deleting business by id : {}",business.getId());
            List<Commission> commissions = business.getCommissions();
            this.commissionService.delete(commissions);
            this.repository.delete(business);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(List<Business> businesses) {
        for (Business business : businesses) {
            this.delete(business);
        }
        return true;
    }
}
