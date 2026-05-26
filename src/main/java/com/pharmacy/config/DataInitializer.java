package com.pharmacy.config;

import com.pharmacy.entity.Drug;
import com.pharmacy.entity.Inventory;
import com.pharmacy.repository.DrugRepository;
import com.pharmacy.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private DrugRepository drugRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Override
    public void run(String... args) throws Exception {
        if (drugRepository.count() == 0) {
            Drug drug1 = new Drug();
            drug1.setName("阿莫西林胶囊");
            drug1.setDrugCode("AMXLS001");
            drug1.setSpecification("0.25g*24粒/盒");
            drug1.setManufacturer("华北制药股份有限公司");
            drug1.setPrice(new BigDecimal("18.50"));
            drug1.setCategory("西药");
            drug1.setUnit("盒");
            drug1.setPrescriptionRequired(true);
            drug1.setDescription("青霉素类抗生素，用于治疗敏感细菌引起的感染");
            drugRepository.save(drug1);

            Drug drug2 = new Drug();
            drug2.setName("布洛芬缓释胶囊");
            drug2.setDrugCode("BLFHS001");
            drug2.setSpecification("0.3g*20粒/盒");
            drug2.setManufacturer("中美天津史克制药有限公司");
            drug2.setPrice(new BigDecimal("22.80"));
            drug2.setCategory("西药");
            drug2.setUnit("盒");
            drug2.setPrescriptionRequired(false);
            drug2.setDescription("解热镇痛药，用于缓解疼痛和发热");
            drugRepository.save(drug2);

            Drug drug3 = new Drug();
            drug3.setName("感冒灵颗粒");
            drug3.setDrugCode("GMLKL001");
            drug3.setSpecification("10g*9袋/盒");
            drug3.setManufacturer("北京同仁堂股份有限公司");
            drug3.setPrice(new BigDecimal("15.00"));
            drug3.setCategory("中成药");
            drug3.setUnit("盒");
            drug3.setPrescriptionRequired(false);
            drug3.setDescription("解热镇痛，用于感冒引起的头痛、发热等症状");
            drugRepository.save(drug3);

            Drug drug4 = new Drug();
            drug4.setName("维生素C片");
            drug4.setDrugCode("WSSSP001");
            drug4.setSpecification("100mg*100片/瓶");
            drug4.setManufacturer("华中药业股份有限公司");
            drug4.setPrice(new BigDecimal("8.50"));
            drug4.setCategory("保健品");
            drug4.setUnit("瓶");
            drug4.setPrescriptionRequired(false);
            drug4.setDescription("补充维生素C，增强免疫力");
            drugRepository.save(drug4);

            Drug drug5 = new Drug();
            drug5.setName("藿香正气水");
            drug5.setDrugCode("HXZQ001");
            drug5.setSpecification("10ml*10支/盒");
            drug5.setManufacturer("太极集团四川绵阳制药有限公司");
            drug5.setPrice(new BigDecimal("12.00"));
            drug5.setCategory("中成药");
            drug5.setUnit("盒");
            drug5.setPrescriptionRequired(false);
            drug5.setDescription("用于暑湿感冒，头痛胸闷等症状");
            drugRepository.save(drug5);

            // 初始化库存数据
            Inventory inv1 = new Inventory();
            inv1.setDrug(drug1);
            inv1.setQuantity(500);
            inv1.setAlertQuantity(100);
            inv1.setProductionDate(LocalDate.of(2024, 1, 15));
            inv1.setExpiryDate(LocalDate.of(2026, 1, 14));
            inv1.setBatchNumber("AMXL202401");
            inv1.setSupplier("华北制药股份有限公司");
            inventoryRepository.save(inv1);

            Inventory inv2 = new Inventory();
            inv2.setDrug(drug2);
            inv2.setQuantity(300);
            inv2.setAlertQuantity(50);
            inv2.setProductionDate(LocalDate.of(2024, 2, 20));
            inv2.setExpiryDate(LocalDate.of(2026, 2, 19));
            inv2.setBatchNumber("BLFH202402");
            inv2.setSupplier("中美天津史克制药有限公司");
            inventoryRepository.save(inv2);

            Inventory inv3 = new Inventory();
            inv3.setDrug(drug3);
            inv3.setQuantity(450);
            inv3.setAlertQuantity(80);
            inv3.setProductionDate(LocalDate.of(2024, 3, 10));
            inv3.setExpiryDate(LocalDate.of(2026, 3, 9));
            inv3.setBatchNumber("GMLK202403");
            inv3.setSupplier("北京同仁堂股份有限公司");
            inventoryRepository.save(inv3);

            Inventory inv4 = new Inventory();
            inv4.setDrug(drug4);
            inv4.setQuantity(1000);
            inv4.setAlertQuantity(200);
            inv4.setProductionDate(LocalDate.of(2024, 1, 1));
            inv4.setExpiryDate(LocalDate.of(2026, 12, 31));
            inv4.setBatchNumber("WSSS202401");
            inv4.setSupplier("华中药业股份有限公司");
            inventoryRepository.save(inv4);

            Inventory inv5 = new Inventory();
            inv5.setDrug(drug5);
            inv5.setQuantity(280);
            inv5.setAlertQuantity(60);
            inv5.setProductionDate(LocalDate.of(2024, 4, 5));
            inv5.setExpiryDate(LocalDate.of(2026, 4, 4));
            inv5.setBatchNumber("HXZQ202404");
            inv5.setSupplier("太极集团四川绵阳制药有限公司");
            inventoryRepository.save(inv5);

            System.out.println("Sample data initialized successfully!");
        }
    }
}
