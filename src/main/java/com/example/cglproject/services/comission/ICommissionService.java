package com.example.cglproject.services.comission;

import com.example.cglproject.models.Commission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ICommissionService {

    Page<Commission> getAllCommissions(Pageable pageable);

    List<Commission> getAllCommissions();

    Optional<Commission> findById(Long id);

    Commission create(Commission commission);

    Commission update(Long id, Commission commission);

    boolean delete(Long id);
    boolean delete(Commission commission);
    boolean delete(List<Commission> commissions);
}
