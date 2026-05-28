package com.pharmacy.controller;

import com.pharmacy.entity.Menu;
import com.pharmacy.entity.Role;
import com.pharmacy.service.MenuService;
import com.pharmacy.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    @GetMapping
    public String listRoles(Model model) {
        List<Role> roles = roleService.getAllRoles();
        model.addAttribute("roles", roles);
        return "roles/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("role", new Role());
        model.addAttribute("menus", menuService.getAllMenus());
        return "roles/form";
    }

    @PostMapping
    public String createRole(@ModelAttribute Role role, @RequestParam(value = "menuIds", required = false) List<Long> menuIds, Model model) {
        if (roleService.existsByName(role.getName())) {
            model.addAttribute("error", "角色名称已存在");
            model.addAttribute("menus", menuService.getAllMenus());
            return "roles/form";
        }
        
        Set<Menu> menus = new HashSet<>();
        if (menuIds != null) {
            for (Long menuId : menuIds) {
                menuService.getMenuById(menuId).ifPresent(menus::add);
            }
        }
        role.setMenus(menus);
        roleService.saveRole(role);
        return "redirect:/roles";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Role> role = roleService.getRoleById(id);
        if (role.isPresent()) {
            model.addAttribute("role", role.get());
            model.addAttribute("menus", menuService.getAllMenus());
            return "roles/form";
        }
        return "redirect:/roles";
    }

    @PostMapping("/{id}")
    public String updateRole(@PathVariable Long id, @ModelAttribute Role role, @RequestParam(value = "menuIds", required = false) List<Long> menuIds, Model model) {
        try {
            Role existingRole = roleService.getRoleById(id).orElse(null);
            if (existingRole == null) {
                return "redirect:/roles";
            }
            
            if (!existingRole.getName().equals(role.getName()) && roleService.existsByName(role.getName())) {
                model.addAttribute("error", "角色名称已存在");
                model.addAttribute("menus", menuService.getAllMenus());
                return "roles/form";
            }
            
            existingRole.setName(role.getName());
            existingRole.setCode(role.getCode());
            existingRole.setDescription(role.getDescription());
            
            Set<Menu> menus = new HashSet<>();
            if (menuIds != null) {
                for (Long menuId : menuIds) {
                    menuService.getMenuById(menuId).ifPresent(menus::add);
                }
            }
            existingRole.setMenus(menus);
            roleService.saveRole(existingRole);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("menus", menuService.getAllMenus());
            return "roles/form";
        }
        return "redirect:/roles";
    }

    @GetMapping("/{id}/delete")
    public String deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return "redirect:/roles";
    }
}