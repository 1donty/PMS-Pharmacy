package com.pharmacy.controller;

import com.pharmacy.entity.Drug;
import com.pharmacy.entity.Inventory;
import com.pharmacy.entity.Sale;
import com.pharmacy.service.DrugService;
import com.pharmacy.service.InventoryService;
import com.pharmacy.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/sales")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @Autowired
    private DrugService drugService;

    @Autowired
    private InventoryService inventoryService;

    @GetMapping
    public String listSales(Model model) {
        List<Sale> sales = saleService.getAllSales();
        model.addAttribute("sales", sales);
        return "sales/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("sale", new Sale());
        List<Drug> drugs = drugService.getAllDrugs();
        model.addAttribute("drugs", drugs);
        List<Inventory> inventoryList = inventoryService.getAllInventory();
        model.addAttribute("inventoryList", inventoryList);
        return "sales/form";
    }

    @PostMapping
    public String createSale(@ModelAttribute Sale sale,
                             @RequestParam Long drugId,
                             @RequestParam Integer quantity,
                             Model model) {
        try {
            saleService.createSale(sale, drugId, quantity);
            return "redirect:/sales";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            List<Drug> drugs = drugService.getAllDrugs();
            model.addAttribute("drugs", drugs);
            List<Inventory> inventoryList = inventoryService.getAllInventory();
            model.addAttribute("inventoryList", inventoryList);
            return "sales/form";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Sale> sale = saleService.getSaleById(id);
        if (sale.isPresent()) {
            model.addAttribute("sale", sale.get());
            return "sales/form";
        }
        return "redirect:/sales";
    }

    @PostMapping("/{id}")
    public String updateSale(@PathVariable Long id, @ModelAttribute Sale sale) {
        saleService.updateSale(id, sale);
        return "redirect:/sales";
    }

    @GetMapping("/{id}/delete")
    public String deleteSale(@PathVariable Long id) {
        saleService.deleteSale(id);
        return "redirect:/sales";
    }

    @GetMapping("/search")
    public String searchSales(@RequestParam String customerName, Model model) {
        List<Sale> sales = saleService.searchByCustomerName(customerName);
        model.addAttribute("sales", sales);
        model.addAttribute("keyword", customerName);
        return "sales/list";
    }

    @GetMapping("/date-range")
    public String getSalesByDateRange(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
                                      Model model) {
        List<Sale> sales = saleService.getSalesByDateRange(startDate, endDate);
        BigDecimal total = saleService.getTotalSalesByDateRange(startDate, endDate);
        model.addAttribute("sales", sales);
        model.addAttribute("totalAmount", total);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        return "sales/list";
    }

    @GetMapping("/recent")
    public String getRecentSales(Model model) {
        List<Sale> sales = saleService.getRecentSales();
        model.addAttribute("sales", sales);
        return "sales/list";
    }

    @GetMapping("/drug/{drugId}")
    public String getSalesByDrug(@PathVariable Long drugId, Model model) {
        List<Sale> sales = saleService.getSalesByDrugId(drugId);
        model.addAttribute("sales", sales);
        return "sales/list";
    }

    @GetMapping("/api/all")
    @ResponseBody
    public List<Sale> getAllSalesApi() {
        return saleService.getAllSales();
    }
}
