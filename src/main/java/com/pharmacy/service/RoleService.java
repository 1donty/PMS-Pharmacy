package com.pharmacy.service;

import com.pharmacy.entity.Menu;
import com.pharmacy.entity.Role;
import com.pharmacy.repository.MenuRepository;
import com.pharmacy.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MenuRepository menuRepository;

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Optional<Role> getRoleById(Long id) {
        return roleRepository.findById(id);
    }

    public Optional<Role> getRoleByName(String name) {
        return roleRepository.findByName(name);
    }

    public boolean existsByName(String name) {
        return roleRepository.existsByName(name);
    }

    public boolean existsByCode(String code) {
        return roleRepository.existsByCode(code);
    }

    @Transactional
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Transactional
    public Role updateRole(Long id, Role roleDetails) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("角色不存在"));
        
        if (!role.getName().equals(roleDetails.getName()) && existsByName(roleDetails.getName())) {
            throw new RuntimeException("角色名称已存在");
        }
        
        role.setName(roleDetails.getName());
        role.setCode(roleDetails.getCode());
        role.setDescription(roleDetails.getDescription());
        
        return roleRepository.save(role);
    }

    @Transactional
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }

    @Transactional
    public void updateRoleMenus(Long roleId, Set<Long> menuIds) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("角色不存在"));
        
        role.getMenus().clear();
        for (Long menuId : menuIds) {
            menuRepository.findById(menuId).ifPresent(role::addMenu);
        }
        roleRepository.save(role);
    }
}