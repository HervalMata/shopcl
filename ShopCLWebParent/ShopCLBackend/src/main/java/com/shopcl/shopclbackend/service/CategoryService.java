package com.shopcl.shopclbackend.service;

import com.shopcl.common.entity.Category;
import com.shopcl.shopclbackend.repository.CategoryRepository;
import com.shopcl.shopclbackend.service.impl.ICategoryService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> listAll() {
        List<Category> categoryList = new ArrayList<>();
        categoryRepository.findAll().forEach(categoryList::add);
        return categoryList;
    }

    @Override
    public List<Category> listCategoriesUsedInForm() {
        List<Category> categoriesUsedInForm = new ArrayList<>();
        Iterable<Category> categoriesInDB = categoryRepository.findAll();
        for (Category category : categoriesInDB) {
            categoriesUsedInForm.add(new Category(category.getName()));
        }
        return categoriesUsedInForm;
    }
}
