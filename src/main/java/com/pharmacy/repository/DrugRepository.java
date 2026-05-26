package com.pharmacy.repository;

import com.pharmacy.entity.Drug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DrugRepository extends JpaRepository<Drug, Long> {

    Optional<Drug> findByDrugCode(String drugCode);

    boolean existsByDrugCode(String drugCode);

    @Query("SELECT d FROM Drug d WHERE d.name LIKE %:keyword% OR d.drugCode LIKE %:keyword% OR d.manufacturer LIKE %:keyword%")
    List<Drug> searchByKeyword(@Param("keyword") String keyword);

    List<Drug> findByCategory(String category);

    List<Drug> findByPrescriptionRequired(Boolean prescriptionRequired);
}
