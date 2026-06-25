# PMS-Pharmacy 药店药品管理系统

基于 Spring Boot 3 的药店药品管理系统，提供药品管理、库存管理、销售管理、用户与权限管理等功能。

## 技术栈

| 技术 | 版本 |
|------|------|
| Java | 17 |
| Spring Boot | 3.2.0 |
| Spring Security | 3.2.0 |
| Spring Data JPA | 3.2.0 |
| Thymeleaf | 3.2.0 |
| Bootstrap | 5.3.0 |
| SQLite | 3.45.1.0 |

## 功能模块

- **药品管理** — 药品信息的增删改查，支持药品编码、规格、生产企业、分类、处方药标识等字段
- **库存管理** — 库存入库、盘点、调整，记录批次号、生产日期、有效期，自动判断过期药品
- **销售管理** — 药品销售记录，自动计算总金额，支持客户姓名和支付方式
- **用户管理** — 用户注册登录，账号启用/禁用，个人信息管理
- **角色管理** — 角色创建与权限分配，支持多角色
- **菜单管理** — 动态菜单配置，支持父级菜单、排序、权限标识

## 快速开始

### 环境要求

- JDK 17+
- Maven 3.6+

### 运行项目

```bash
# 克隆仓库
git clone https://atomgit.com/springcloudlab/A2501-pharmacy-medicine.git
cd A2501-pharmacy-medicine

# 启动项目（Windows）
mvnw spring-boot:run

# 启动项目（Linux / macOS）
./mvnw spring-boot:run
```

启动后访问 **http://localhost:8080**。

首次启动会自动创建 `example_db.sqlite` 数据库文件并初始化表结构。

### 打包部署

```bash
mvnw clean package -DskipTests
java -jar target/pms-pharmacy-1.0.0.jar
```

## 项目结构

```
src/main/java/com/pharmacy/
├── PharmacyApplication.java        # 启动入口
├── config/
│   ├── DataInitializer.java        # 数据初始化
│   └── SecurityConfig.java         # Spring Security 配置
├── controller/
│   ├── AuthController.java         # 登录注册
│   ├── DrugController.java         # 药品管理
│   ├── HomeController.java         # 首页
│   ├── InventoryController.java    # 库存管理
│   ├── MenuController.java         # 菜单管理
│   ├── RoleController.java         # 角色管理
│   ├── SaleController.java         # 销售管理
│   └── UserController.java         # 用户管理
├── entity/
│   ├── Drug.java                   # 药品实体
│   ├── Inventory.java              # 库存实体
│   ├── Menu.java                   # 菜单实体
│   ├── Role.java                   # 角色实体
│   ├── Sale.java                   # 销售实体
│   └── User.java                   # 用户实体
├── repository/                     # 数据访问层 (JPA)
├── service/                        # 业务逻辑层
└── resources/
    ├── application.properties      # 应用配置
    └── templates/                  # Thymeleaf 模板页面
        ├── drugs/                  # 药品页面
        ├── inventory/              # 库存页面
        ├── menus/                  # 菜单页面
        ├── roles/                  # 角色页面
        ├── sales/                  # 销售页面
        ├── users/                  # 用户页面
        ├── layout.html             # 公共布局
        ├── login.html              # 登录页
        └── register.html           # 注册页
```

## 数据库

使用 SQLite 文件数据库，数据文件为根目录下的 `example_db.sqlite`，无需额外安装数据库。

启动时 JPA 自动建表（`ddl-auto=update`），数据库表结构：

| 表名 | 说明 |
|------|------|
| drugs | 药品信息表 |
| inventory | 库存表 |
| sales | 销售记录表 |
| users | 用户表 |
| roles | 角色表 |
| menus | 菜单表 |
| user_roles | 用户-角色关联表 |
| role_menus | 角色-菜单关联表 |

## 配置说明

主要配置项位于 `src/main/resources/application.properties`：

| 配置项 | 默认值 | 说明 |
|--------|--------|------|
| server.port | 8080 | 服务端口 |
| spring.datasource.url | jdbc:sqlite:example_db.sqlite | 数据库文件路径 |
| spring.jpa.hibernate.ddl-auto | update | 自动更新表结构 |
| spring.jpa.show-sql | true | 打印 SQL 日志 |

## 许可证

Apache-2.0 License
