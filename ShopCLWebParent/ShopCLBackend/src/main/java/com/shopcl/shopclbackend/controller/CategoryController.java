package com.shopcl.shopclbackend.controller;

import com.shopcl.common.entity.Category;
import com.shopcl.shopclbackend.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
public class CategoryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public String listAll(Model model) {
        LOGGER.info("CategoryController | listAll is started");
        List<Category> listCategories = categoryService.listAll();
        model.addAttribute("listCategories", listCategories);
        LOGGER.info("CategoryController | listAll | listCategories : " + listCategories.toString());
        return "categories/categories";
    }

    @GetMapping("/categories/new")
    public String newCategory(Model model) {
        LOGGER.info("CategoryController | newCategory is started");
        return null;
    }

    @GetMapping("/categories/export/csv")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        LOGGER.info("CategoryController | exportToCSV is started");
    }

    @GetMapping("/categories/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        LOGGER.info("CategoryController | exportToExcel is started");
    }

    @GetMapping("/categories/export/pdf")
    public void exportToPDF(HttpServletResponse response) throws IOException {
        LOGGER.info("CategoryController | exportToPDF is started");
    }
}
