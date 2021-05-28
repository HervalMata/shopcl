package com.shopcl.shopclbackend.controller;

import com.shopcl.common.entity.Role;
import com.shopcl.common.entity.User;
import com.shopcl.shopclbackend.error.UserNotFoundException;
import com.shopcl.shopclbackend.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String listAll(Model model) {
        List<User> listUsers = userService.listAll();
        model.addAttribute("listUsers", listUsers);
        return "users";
    }

    @GetMapping("/users/new")
    public String newUser(Model model) {
        List<Role> listRoles = userService.listRoles();
        User user = new User();
        user.setEnabled(true);
        model.addAttribute("user", user);
        model.addAttribute("listRoles", listRoles);
        model.addAttribute("pageTitle", "Cadastrar Novo Usuário");
        return "user_form";
    }

    @PostMapping("/users/save")
    public String saveUser(User user, RedirectAttributes redirectAttributes) {
        System.out.println(user);
        userService.save(user);
        redirectAttributes.addFlashAttribute("message", "O usuário foi salvo com sucesso.");
        return "redirect:/users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable(name = "id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.get(id);
            List<Role> listRoles = userService.listRoles();
            model.addAttribute("user", user);
            model.addAttribute("listRoles", listRoles);
            model.addAttribute("pageTitle", "Editar Usuário (ID: " + id + ")");
            return "user_form";
        } catch (UserNotFoundException ex) {
            redirectAttributes.addFlashAttribute("messageError", ex.getMessage());
            return "redirect:/users";
        }
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.delete(id);
            redirectAttributes.addFlashAttribute("messageSuccess", "O usuário com ID " + id + " foi removido com sucesso");
        } catch (UserNotFoundException ex) {
            redirectAttributes.addFlashAttribute("messageError", ex.getMessage());
        }
        return "redirect:/users";
    }
}
