package com.pharmacy.controller;

import com.pharmacy.entity.Drug;
import com.pharmacy.service.DrugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/drugs")
public class DrugController {

    @Autowired
    private DrugService drugService;

    @GetMapping
    public String listDrugs(Model model) {
        List<Drug> drugs = drugService.getAllDrugs();
        model.addAttribute("drugs", drugs);
        return "drugs/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("drug", new Drug());
        return "drugs/form";
    }

    @PostMapping
    public String createDrug(@Valid @ModelAttribute Drug drug, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "drugs/form";
        }
        if (drugService.existsByDrugCode(drug.getDrugCode())) {
            model.addAttribute("error", "药品编码已存在");
            return "drugs/form";
        }
        drugService.saveDrug(drug);
        return "redirect:/drugs";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Drug> drug = drugService.getDrugById(id);
        if (drug.isPresent()) {
            model.addAttribute("drug", drug.get());
            return "drugs/form";
        }
        return "redirect:/drugs";
    }

    @PostMapping("/{id}")
    public String updateDrug(@PathVariable Long id, @Valid @ModelAttribute Drug drug, BindingResult result) {
        if (result.hasErrors()) {
            return "drugs/form";
        }
        drugService.updateDrug(id, drug);
        return "redirect:/drugs";
    }

    @GetMapping("/{id}/delete")
    public String deleteDrug(@PathVariable Long id) {
        drugService.deleteDrug(id);
        return "redirect:/drugs";
    }

    @GetMapping("/search")
    public String searchDrugs(@RequestParam String keyword, Model model) {
        List<Drug> drugs = drugService.searchDrugs(keyword);
        model.addAttribute("drugs", drugs);
        model.addAttribute("keyword", keyword);
        return "drugs/list";
    }

    @GetMapping("/category/{category}")
    public String getByCategory(@PathVariable String category, Model model) {
        List<Drug> drugs = drugService.getDrugsByCategory(category);
        model.addAttribute("drugs", drugs);
        model.addAttribute("category", category);
        return "drugs/list";
    }

    @GetMapping("/prescription")
    public String getPrescriptionDrugs(Model model) {
        List<Drug> drugs = drugService.getPrescriptionDrugs();
        model.addAttribute("drugs", drugs);
        return "drugs/list";
    }

    @GetMapping("/api/all")
    @ResponseBody
    public List<Drug> getAllDrugsApi() {
        return drugService.getAllDrugs();
    }
}
