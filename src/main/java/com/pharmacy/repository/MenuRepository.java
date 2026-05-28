package com.pharmacy.repository;

import com.pharmacy.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    List<Menu> findByParentId(Long parentId);

    List<Menu> findByEnabledTrueOrderBySortOrderAsc();

    List<Menu> findByParentIdIsNullOrderBySortOrderAsc();

    List<Menu> findByParentIdIsNull();
}