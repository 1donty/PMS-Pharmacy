package com.pharmacy.service;

import com.pharmacy.entity.Drug;
import com.pharmacy.repository.DrugRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DrugService {

    @Autowired
    private DrugRepository drugRepository;

    public List<Drug> getAllDrugs() {
        return drugRepository.findAll();
    }

    public Optional<Drug> getDrugById(Long id) {
        return drugRepository.findById(id);
    }

    public Optional<Drug> getDrugByCode(String drugCode) {
        return drugRepository.findByDrugCode(drugCode);
    }

    public Drug saveDrug(Drug drug) {
        return drugRepository.save(drug);
    }

    public Drug updateDrug(Long id, Drug drugDetails) {
        Drug drug = drugRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("药品不存在"));
        if (!drug.getDrugCode().equals(drugDetails.getDrugCode()) && existsByDrugCode(drugDetails.getDrugCode())) {
            throw new RuntimeException("药品编码已存在");
        }
        drug.setDrugCode(drugDetails.getDrugCode());
        drug.setName(drugDetails.getName());
        drug.setSpecification(drugDetails.getSpecification());
        drug.setManufacturer(drugDetails.getManufacturer());
        drug.setPrice(drugDetails.getPrice());
        drug.setCategory(drugDetails.getCategory());
        drug.setUnit(drugDetails.getUnit());
        drug.setPrescriptionRequired(drugDetails.getPrescriptionRequired());
        drug.setDescription(drugDetails.getDescription());
        return drugRepository.save(drug);
    }

    public void deleteDrug(Long id) {
        drugRepository.deleteById(id);
    }

    public List<Drug> searchDrugs(String keyword) {
        return drugRepository.searchByKeyword(keyword);
    }

    public List<Drug> getDrugsByCategory(String category) {
        return drugRepository.findByCategory(category);
    }

    public List<Drug> getPrescriptionDrugs() {
        return drugRepository.findByPrescriptionRequired(true);
    }

    public boolean existsByDrugCode(String drugCode) {
        return drugRepository.existsByDrugCode(drugCode);
    }
}
