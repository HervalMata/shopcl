package com.shopcl.shopclbackend.controller;

import com.shopcl.common.entity.Role;
import com.shopcl.common.entity.User;
import com.shopcl.shopclbackend.error.UserNotFoundException;
import com.shopcl.shopclbackend.service.UserService;
import com.shopcl.shopclbackend.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String listFirstPAge(Model model) {
        LOGGER.info("UserController | listFirstPage is started");
        return listByPage(1, model, "firstName", "asc", null);
    }

    @GetMapping("/users/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
                             @Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword) {
        LOGGER.info("UserController | listByPage is started");
        LOGGER.info("UserController | listByPage | sortField : " + sortField);
        LOGGER.info("UserController | listByPage | sortDir : " + sortDir);
        Page<User> page = userService.listByPage(pageNum, sortField, sortDir, keyword);
        List<User> listUsers = page.getContent();
        LOGGER.info("UserController | listByPage | listUsers.size() : " + listUsers.size());
        long startCount = (pageNum - 1) * UserService.USERS_PER_PAGE + 1;
        long endCount = startCount + UserService.USERS_PER_PAGE - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }
        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
        LOGGER.info("UserController | listByPage | currentPage : " + pageNum);
        LOGGER.info("UserController | listByPage | totalPages : " + page.getTotalPages());
        LOGGER.info("UserController | listByPage | startCount : " + startCount);
        LOGGER.info("UserController | listByPage | endCount : " + endCount);
        LOGGER.info("UserController | listByPage | totalItems : " + page.getTotalElements());
        LOGGER.info("UserController | listByPage | sortField : " + sortField);
        LOGGER.info("UserController | listByPage | sortDir : " + sortDir);
        LOGGER.info("UserController | listByPage | reverseSortDir : " + reverseSortDir);
        LOGGER.info("UserController | listByPage | keyword : " + keyword);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listUsers", listUsers);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyword", keyword);
        return "users/users";
    }

    @GetMapping("/users/new")
    public String newUser(Model model) {
        LOGGER.info("UserController | newUser is called");
        List<Role> listRoles = userService.listRoles();
        LOGGER.info("UserController | newUser | listRoles.size() : " + listRoles.size());
        User user = new User();
        user.setEnabled(true);
        LOGGER.info("UserController | newUser | user : " + user.toString());
        model.addAttribute("user", user);
        model.addAttribute("listRoles", listRoles);
        model.addAttribute("pageTitle", "Cadastrar Novo Usuário");
        return "users/user_form";
    }

    @PostMapping("/users/save")
    public String saveUser(User user, RedirectAttributes redirectAttributes,
                           @RequestParam("image") MultipartFile multipartFile) throws IOException {
        LOGGER.info("UserController | saveUser is called");
        LOGGER.info("UserController | saveUser | multipartFile : " + multipartFile);
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            LOGGER.info("UserController | saveUser | fileName : " + fileName);
            user.setPhotos(fileName);
            User savedUser = userService.save(user);
            LOGGER.info("UserController | saveUser | savedUser : " + savedUser.toString());
            String uploadDir = "user-photos/" + savedUser.getId();
            LOGGER.info("UserController | saveUser | uploadDir : " + uploadDir);
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            LOGGER.info("UserController | saveUser | user.getPhotos() : " + user.getPhotos());
            if (user.getPhotos().isEmpty()) user.setPhotos(null);
            userService.save(user);
            LOGGER.info("UserController | saveUser | save completed");
        }

        redirectAttributes.addFlashAttribute("message", "O usuário foi salvo com sucesso.");
        return DirectUtil.getRedirectURLtoAffectedUser(user);
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable(name = "id") Long id, Model model, RedirectAttributes redirectAttributes) {
        LOGGER.info("UserController | editUser is called");
        try {
            User user = userService.get(id);
            LOGGER.info("UserController | editUser | user : " + user.toString());
            List<Role> listRoles = userService.listRoles();
            LOGGER.info("UserController | editUser | listRoles.size() : " + listRoles.size());
            model.addAttribute("user", user);
            model.addAttribute("listRoles", listRoles);
            model.addAttribute("pageTitle", "Editar Usuário (ID: " + id + ")");
            return "users/user_form";
        } catch (UserNotFoundException ex) {
            LOGGER.error("UserController | editUser | ex.getMessage() : " + ex.getMessage());
            redirectAttributes.addFlashAttribute("messageError", ex.getMessage());
            return "redirect:/users";
        }
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Long id, RedirectAttributes redirectAttributes) {
        LOGGER.info("UserController | deleteUser is called");
        try {
            userService.delete(id);
            LOGGER.info("UserController | deleteUser | delete completed");
            redirectAttributes.addFlashAttribute("messageSuccess", "O usuário com ID " + id + " foi removido com sucesso");
        } catch (UserNotFoundException ex) {
            LOGGER.error("UserController | deleteUser | ex.getMessage() : " + ex.getMessage());
            redirectAttributes.addFlashAttribute("messageError", ex.getMessage());
        }
        return "redirect:/users";
    }

    @GetMapping("/users/{id}/enabled/{status}")
    public String updateUserEnabledStatus(@PathVariable(name = "id") Long id, @PathVariable(name = "status") boolean enabled, RedirectAttributes redirectAttributes) {
        LOGGER.info("UserController | updateUserEnabledStatus is called");
        userService.updateUserEnabledStatus(id, enabled);
        LOGGER.info("UserController | updateUserEnabledStatus completed");
        String status = enabled ? "Habilitado" : "Desabilitado";
        LOGGER.info("UserController | updateUserEnabledStatus | status : " + status);
        String message = "O usuário com ID " + id + " foi " + status;
        LOGGER.info("UserController | updateUserEnabledStatus | message : " + message);
        if (message.contains("Habilitado")) {
            redirectAttributes.addFlashAttribute("messageSuccess", message);
        } else {
            redirectAttributes.addFlashAttribute("messageError", message);
        }
        return "redirect:/users";
    }

    @GetMapping("/users/export/csv")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        LOGGER.info("UserController | exportToCSV is called");
        List<User> listUsers = userService.listAll();
        LOGGER.info("UserController | exportToCSV | listUsers.size() : " + listUsers.size());
        UserCsvExporter exporter = new UserCsvExporter();
        LOGGER.info("UserController | exportToCSV | export is starting");
        exporter.export(listUsers, response);
    }

    @GetMapping("/users/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        LOGGER.info("UserController | exportToExcel is called");
        List<User> listUsers = userService.listAll();
        LOGGER.info("UserController | exportToExcel | listUsers.size() : " + listUsers.size());
        UserExcelExporter exporter = new UserExcelExporter();
        LOGGER.info("UserController | exportToExcel | export is starting");
        exporter.export(listUsers, response);
    }

    @GetMapping("/users/export/pdf")
    public void exportToPDF(HttpServletResponse response) throws IOException {
        LOGGER.info("UserController | exportToPDF is called");
        List<User> listUsers = userService.listAll();
        LOGGER.info("UserController | exportToPDF | listUsers.size() : " + listUsers.size());
        UserPdfExporter exporter = new UserPdfExporter();
        LOGGER.info("UserController | exportToPDF | export is starting");
        exporter.export(listUsers, response);
    }
}
