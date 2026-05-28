package com.pharmacy.controller;

import com.pharmacy.entity.Menu;
import com.pharmacy.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/menus")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping
    public String listMenus(Model model) {
        List<Menu> menus = menuService.getAllMenus();
        model.addAttribute("menus", menus);
        return "menus/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("menu", new Menu());
        model.addAttribute("parentMenus", menuService.getParentMenus());
        return "menus/form";
    }

    @PostMapping
    public String createMenu(@ModelAttribute Menu menu, Model model) {
        menuService.saveMenu(menu);
        return "redirect:/menus";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Menu> menu = menuService.getMenuById(id);
        if (menu.isPresent()) {
            model.addAttribute("menu", menu.get());
            model.addAttribute("parentMenus", menuService.getParentMenus());
            return "menus/form";
        }
        return "redirect:/menus";
    }

    @PostMapping("/{id}")
    public String updateMenu(@PathVariable Long id, @ModelAttribute Menu menu) {
        menuService.updateMenu(id, menu);
        return "redirect:/menus";
    }

    @GetMapping("/{id}/delete")
    public String deleteMenu(@PathVariable Long id) {
        menuService.deleteMenu(id);
        return "redirect:/menus";
    }
}