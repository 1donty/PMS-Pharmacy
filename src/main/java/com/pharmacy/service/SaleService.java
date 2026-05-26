package com.pharmacy.service;

import com.pharmacy.entity.Drug;
import com.pharmacy.entity.Inventory;
import com.pharmacy.entity.Sale;
import com.pharmacy.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private DrugService drugService;

    @Autowired
    private InventoryService inventoryService;

    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }

    public Optional<Sale> getSaleById(Long id) {
        return saleRepository.findById(id);
    }

    public Sale createSale(Sale sale, Long drugId, int quantity) {
        Drug drug = drugService.getDrugById(drugId)
                .orElseThrow(() -> new RuntimeException("药品不存在"));

        Optional<Inventory> inventoryOpt = inventoryService.getInventoryByDrugId(drugId);
        if (inventoryOpt.isEmpty()) {
            throw new RuntimeException("该药品没有库存记录");
        }

        Inventory inventory = inventoryOpt.get();
        if (inventory.getQuantity() < quantity) {
            throw new RuntimeException("库存不足，当前库存: " + inventory.getQuantity());
        }

        sale.setDrug(drug);
        sale.setQuantity(quantity);
        sale.setUnitPrice(drug.getPrice());
        sale.setTotalAmount(drug.getPrice().multiply(new BigDecimal(quantity)));

        inventoryService.adjustQuantity(inventory.getId(), -quantity);

        return saleRepository.save(sale);
    }

    public Sale updateSale(Long id, Sale saleDetails) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("销售记录不存在"));
        sale.setCustomerName(saleDetails.getCustomerName());
        sale.setPaymentMethod(saleDetails.getPaymentMethod());
        sale.setRemarks(saleDetails.getRemarks());
        return saleRepository.save(sale);
    }

    public void deleteSale(Long id) {
        saleRepository.deleteById(id);
    }

    public List<Sale> getSalesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return saleRepository.findByDateRange(startDate, endDate);
    }

    public List<Sale> getSalesByDrugId(Long drugId) {
        return saleRepository.findByDrugId(drugId);
    }

    public List<Sale> searchByCustomerName(String customerName) {
        return saleRepository.searchByCustomerName(customerName);
    }

    public BigDecimal getTotalSalesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        BigDecimal total = saleRepository.calculateTotalSales(startDate, endDate);
        return total != null ? total : BigDecimal.ZERO;
    }

    public List<Sale> getRecentSales() {
        return saleRepository.findRecentSales();
    }

    public List<Sale> getSalesByDrugAndDateRange(Long drugId, LocalDateTime startDate, LocalDateTime endDate) {
        return saleRepository.findByDrugIdAndDateRange(drugId, startDate, endDate);
    }
}
