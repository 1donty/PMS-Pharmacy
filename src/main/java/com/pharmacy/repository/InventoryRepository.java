package com.pharmacy.repository;

import com.pharmacy.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByDrugId(Long drugId);

    @Query("SELECT i FROM Inventory i WHERE i.quantity <= i.alertQuantity")
    List<Inventory> findLowStockInventory();

    @Query("SELECT i FROM Inventory i WHERE i.expiryDate IS NOT NULL AND i.expiryDate < :date")
    List<Inventory> findExpiredInventory(@Param("date") LocalDate date);

    @Query("SELECT i FROM Inventory i WHERE i.expiryDate IS NOT NULL AND i.expiryDate BETWEEN :startDate AND :endDate")
    List<Inventory> findExpiringInventory(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    List<Inventory> findByBatchNumber(String batchNumber);

    List<Inventory> findBySupplier(String supplier);
}
