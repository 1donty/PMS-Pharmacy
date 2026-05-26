package com.pharmacy.controller;

import com.pharmacy.entity.Drug;
import com.pharmacy.entity.Inventory;
import com.pharmacy.service.DrugService;
import com.pharmacy.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private DrugService drugService;

    @GetMapping
    public String listInventory(Model model) {
        List<Inventory> inventory = inventoryService.getAllInventory();
        model.addAttribute("inventoryList", inventory);
        return "inventory/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("inventory", new Inventory());
        List<Drug> drugs = drugService.getAllDrugs();
        model.addAttribute("drugs", drugs);
        return "inventory/form";
    }

    @PostMapping
    public String createInventory(@Valid @ModelAttribute Inventory inventory,
                                  @RequestParam Long drugId,
                                  BindingResult result, Model model) {
        if (result.hasErrors()) {
            List<Drug> drugs = drugService.getAllDrugs();
            model.addAttribute("drugs", drugs);
            return "inventory/form";
        }
        inventoryService.addInventory(inventory, drugId);
        return "redirect:/inventory";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Inventory> inventory = inventoryService.getInventoryById(id);
        if (inventory.isPresent()) {
            model.addAttribute("inventory", inventory.get());
            List<Drug> drugs = drugService.getAllDrugs();
            model.addAttribute("drugs", drugs);
            return "inventory/form";
        }
        return "redirect:/inventory";
    }

    @PostMapping("/{id}")
    public String updateInventory(@PathVariable Long id, @Valid @ModelAttribute Inventory inventory, BindingResult result) {
        if (result.hasErrors()) {
            List<Drug> drugs = drugService.getAllDrugs();
            return "inventory/form";
        }
        inventoryService.updateInventory(id, inventory);
        return "redirect:/inventory";
    }

    @GetMapping("/{id}/delete")
    public String deleteInventory(@PathVariable Long id) {
        inventoryService.deleteInventory(id);
        return "redirect:/inventory";
    }

    @GetMapping("/{id}/adjust")
    public String showAdjustForm(@PathVariable Long id, Model model) {
        Optional<Inventory> inventory = inventoryService.getInventoryById(id);
        if (inventory.isPresent()) {
            model.addAttribute("inventory", inventory.get());
            return "inventory/adjust";
        }
        return "redirect:/inventory";
    }

    @PostMapping("/{id}/adjust")
    public String adjustQuantity(@PathVariable Long id, @RequestParam int adjustment) {
        inventoryService.adjustQuantity(id, adjustment);
        return "redirect:/inventory";
    }

    @GetMapping("/low-stock")
    public String getLowStock(Model model) {
        List<Inventory> inventory = inventoryService.getLowStockInventory();
        model.addAttribute("inventoryList", inventory);
        model.addAttribute("alert", "低库存警告");
        return "inventory/list";
    }

    @GetMapping("/expired")
    public String getExpired(Model model) {
        List<Inventory> inventory = inventoryService.getExpiredInventory();
        model.addAttribute("inventoryList", inventory);
        model.addAttribute("alert", "过期药品");
        return "inventory/list";
    }

    @GetMapping("/expiring")
    public String getExpiring(@RequestParam(defaultValue = "30") int days, Model model) {
        List<Inventory> inventory = inventoryService.getExpiringInventory(days);
        model.addAttribute("inventoryList", inventory);
        model.addAttribute("alert", "即将过期药品 (未来" + days + "天)");
        return "inventory/list";
    }

    @GetMapping("/api/all")
    @ResponseBody
    public List<Inventory> getAllInventoryApi() {
        return inventoryService.getAllInventory();
    }
}
