package com.pharmacy.service;

import com.pharmacy.entity.Drug;
import com.pharmacy.entity.Inventory;
import com.pharmacy.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private DrugService drugService;

    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    public Optional<Inventory> getInventoryById(Long id) {
        return inventoryRepository.findById(id);
    }

    public Optional<Inventory> getInventoryByDrugId(Long drugId) {
        return inventoryRepository.findByDrugId(drugId);
    }

    public Inventory addInventory(Inventory inventory, Long drugId) {
        Drug drug = drugService.getDrugById(drugId)
                .orElseThrow(() -> new RuntimeException("药品不存在"));
        inventory.setDrug(drug);
        return inventoryRepository.save(inventory);
    }

    public Inventory updateInventory(Long id, Inventory inventoryDetails) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("库存记录不存在"));
        inventory.setQuantity(inventoryDetails.getQuantity());
        inventory.setAlertQuantity(inventoryDetails.getAlertQuantity());
        inventory.setProductionDate(inventoryDetails.getProductionDate());
        inventory.setExpiryDate(inventoryDetails.getExpiryDate());
        inventory.setBatchNumber(inventoryDetails.getBatchNumber());
        inventory.setSupplier(inventoryDetails.getSupplier());
        return inventoryRepository.save(inventory);
    }

    public Inventory adjustQuantity(Long id, int adjustment) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("库存记录不存在"));
        int newQuantity = inventory.getQuantity() + adjustment;
        if (newQuantity < 0) {
            throw new RuntimeException("库存不足");
        }
        inventory.setQuantity(newQuantity);
        return inventoryRepository.save(inventory);
    }

    public void deleteInventory(Long id) {
        inventoryRepository.deleteById(id);
    }

    public List<Inventory> getLowStockInventory() {
        return inventoryRepository.findLowStockInventory();
    }

    public List<Inventory> getExpiredInventory() {
        return inventoryRepository.findExpiredInventory(LocalDate.now());
    }

    public List<Inventory> getExpiringInventory(int days) {
        LocalDate now = LocalDate.now();
        LocalDate future = now.plusDays(days);
        return inventoryRepository.findExpiringInventory(now, future);
    }

    public List<Inventory> getInventoryByBatchNumber(String batchNumber) {
        return inventoryRepository.findByBatchNumber(batchNumber);
    }

    public List<Inventory> getInventoryBySupplier(String supplier) {
        return inventoryRepository.findBySupplier(supplier);
    }
}
