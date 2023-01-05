package com.example.cglproject.repositories;

import com.example.cglproject.models.Business;
import com.example.cglproject.models.BusinessProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusinessProviderRepository extends JpaRepository<BusinessProvider, Long> {


}
