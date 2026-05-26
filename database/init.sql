-- 创建数据库
CREATE DATABASE IF NOT EXISTS pharmacy_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE pharmacy_db;

-- 药品信息表
CREATE TABLE IF NOT EXISTS drugs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT '药品名称',
    drug_code VARCHAR(50) NOT NULL UNIQUE COMMENT '药品编码',
    specification VARCHAR(100) COMMENT '规格',
    manufacturer VARCHAR(200) COMMENT '生产企业',
    price DECIMAL(10, 2) NOT NULL COMMENT '单价',
    category VARCHAR(50) COMMENT '类别',
    unit VARCHAR(20) COMMENT '单位',
    prescription_required BOOLEAN DEFAULT FALSE COMMENT '是否处方药',
    description VARCHAR(500) COMMENT '说明',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_drug_code (drug_code),
    INDEX idx_name (name),
    INDEX idx_category (category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='药品信息表';

-- 库存表
CREATE TABLE IF NOT EXISTS inventory (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    drug_id BIGINT NOT NULL UNIQUE,
    quantity INT NOT NULL DEFAULT 0 COMMENT '库存数量',
    alert_quantity INT DEFAULT 100 COMMENT '警戒库存',
    production_date DATETIME COMMENT '生产日期',
    expiry_date DATETIME COMMENT '有效期至',
    batch_number VARCHAR(100) COMMENT '批号',
    supplier VARCHAR(200) COMMENT '供应商',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (drug_id) REFERENCES drugs(id) ON DELETE CASCADE,
    INDEX idx_drug_id (drug_id),
    INDEX idx_expiry_date (expiry_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存表';

-- 销售记录表
CREATE TABLE IF NOT EXISTS sales (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    drug_id BIGINT NOT NULL,
    quantity INT NOT NULL COMMENT '销售数量',
    unit_price DECIMAL(10, 2) NOT NULL COMMENT '销售单价',
    total_amount DECIMAL(10, 2) NOT NULL COMMENT '总金额',
    sale_date DATETIME NOT NULL COMMENT '销售日期',
    customer_name VARCHAR(100) COMMENT '客户名称',
    payment_method VARCHAR(50) COMMENT '支付方式',
    remarks VARCHAR(500) COMMENT '备注',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (drug_id) REFERENCES drugs(id),
    INDEX idx_sale_date (sale_date),
    INDEX idx_drug_id (drug_id),
    INDEX idx_customer_name (customer_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='销售记录表';

-- 插入示例数据
INSERT INTO drugs (name, drug_code, specification, manufacturer, price, category, unit, prescription_required, description) VALUES
('阿莫西林胶囊', 'AMXLS001', '0.25g*24粒/盒', '华北制药股份有限公司', 18.50, '西药', '盒', TRUE, '青霉素类抗生素，用于治疗敏感细菌引起的感染'),
('布洛芬缓释胶囊', 'BLFHS001', '0.3g*20粒/盒', '中美天津史克制药有限公司', 22.80, '西药', '盒', FALSE, '解热镇痛药，用于缓解疼痛和发热'),
('感冒灵颗粒', 'GMLKL001', '10g*9袋/盒', '北京同仁堂股份有限公司', 15.00, '中成药', '盒', FALSE, '解热镇痛，用于感冒引起的头痛、发热等症状'),
('维生素C片', 'WSSSP001', '100mg*100片/瓶', '华中药业股份有限公司', 8.50, '保健品', '瓶', FALSE, '补充维生素C，增强免疫力'),
('藿香正气水', 'HXZQ001', '10ml*10支/盒', '太极集团四川绵阳制药有限公司', 12.00, '中成药', '盒', FALSE, '用于暑湿感冒，头痛胸闷等症状');

-- 插入库存数据
INSERT INTO inventory (drug_id, quantity, alert_quantity, production_date, expiry_date, batch_number, supplier) VALUES
(1, 500, 100, '2024-01-15', '2026-01-14', 'AMXL202401', '华北制药股份有限公司'),
(2, 300, 50, '2024-02-20', '2026-02-19', 'BLFH202402', '中美天津史克制药有限公司'),
(3, 450, 80, '2024-03-10', '2026-03-09', 'GMLK202403', '北京同仁堂股份有限公司'),
(4, 1000, 200, '2024-01-01', '2026-12-31', 'WSSS202401', '华中药业股份有限公司'),
(5, 280, 60, '2024-04-05', '2026-04-04', 'HXZQ202404', '太极集团四川绵阳制药有限公司');
