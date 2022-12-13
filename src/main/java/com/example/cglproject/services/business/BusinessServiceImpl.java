package com.example.cglproject.services.business;

import com.example.cglproject.models.Business;
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
public class BusinessServiceImpl implements IBusinessService {

    private final BusinessRepository repository;

    public BusinessServiceImpl(BusinessRepository repository) {
        this.repository = repository;
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
            log.info("Deleting business by id : {}",id);
            this.repository.deleteById(id);
            return true;
        }
        return false;
    }
}
