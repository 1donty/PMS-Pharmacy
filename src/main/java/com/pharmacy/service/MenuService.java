package com.pharmacy.service;

import com.pharmacy.entity.Menu;
import com.pharmacy.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }

    public List<Menu> getEnabledMenus() {
        return menuRepository.findByEnabledTrueOrderBySortOrderAsc();
    }

    public List<Menu> getParentMenus() {
        return menuRepository.findByParentIdIsNullOrderBySortOrderAsc();
    }

    public List<Menu> getChildMenus(Long parentId) {
        return menuRepository.findByParentId(parentId);
    }

    public Optional<Menu> getMenuById(Long id) {
        return menuRepository.findById(id);
    }

    @Transactional
    public Menu saveMenu(Menu menu) {
        return menuRepository.save(menu);
    }

    @Transactional
    public Menu updateMenu(Long id, Menu menuDetails) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("菜单不存在"));
        
        menu.setName(menuDetails.getName());
        menu.setUrl(menuDetails.getUrl());
        menu.setIcon(menuDetails.getIcon());
        menu.setSortOrder(menuDetails.getSortOrder());
        menu.setEnabled(menuDetails.getEnabled());
        menu.setParentId(menuDetails.getParentId());
        menu.setPermission(menuDetails.getPermission());
        
        return menuRepository.save(menu);
    }

    @Transactional
    public void deleteMenu(Long id) {
        menuRepository.deleteById(id);
    }
}