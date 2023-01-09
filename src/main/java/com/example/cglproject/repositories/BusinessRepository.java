package com.example.cglproject.repositories;

import com.example.cglproject.models.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {
    @Query("SELECT IdBusinessProvider FROM Business IdBusinessProvider WHERE IdBusinessProvider.IdBusinessProvider = ?1")
    List<Business> findByIdBusinessProvider(Long IdBusinessProvider);

    @Query(value = "SELECT * FROM business Bus INNER JOIN business_provider BP ON BP.id = Bus.id_business_provider WHERE Bus.id_business_provider = ?1 AND BP.is_sponsored = true", nativeQuery = true)
    List<Business> findByIdBusinessProviderAndIsSponsored(Long IdBusinessProvider);
}
