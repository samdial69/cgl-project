package com.example.cglproject.services.comission;

import com.example.cglproject.models.Commission;
import com.example.cglproject.repositories.CommissionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.activation.MimeTypeParameterList;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class CommissionServiceImpl implements ICommissionService {

    private final CommissionRepository repository;

    public CommissionServiceImpl(CommissionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Commission> getAllCommissions(Pageable pageable) {
        log.info("Fetching all commissions returning pages");
        return this.repository.findAll(pageable);
    }

    @Override
    public List<Commission> getAllCommissions() {
        log.info("Fetching all commissions");
        return this.repository.findAll();
    }

    @Override
    public Optional<Commission> findById(Long id) {
        log.info("Fetching a commission by id : {}",id);
        return this.repository.findById(id);
    }

    @Override
    public Commission create(Commission commission) {
        log.info("Creating a new commission : {}",commission);
        return this.repository.save(commission);
    }

    @Override
    public Commission update(Long id, Commission commission) {
        Optional<Commission> currentCommission = this.findById(id);
        if(currentCommission.isPresent()){
            log.info("Updating commission by id : {}",id);
            commission.setId(currentCommission.get().getId());
            return this.repository.save(commission);
        }
        return null;
    }

    @Override
    public boolean delete(Long id) {
        Optional<Commission> currentCommission = this.findById(id);
        if(currentCommission.isPresent()){
            log.info("Deleting commission by id : {}",id);
            this.repository.deleteById(id);
            return true;
        }
        return false;
    }

}
