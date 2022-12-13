package com.example.cglproject.repositories;

import com.example.cglproject.models.BusinessProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessProviderRepository extends JpaRepository<BusinessProvider, Long> {

}
