package com.shopcl.shopclbackend.service.impl;

import com.shopcl.common.entity.Category;
import com.shopcl.shopclbackend.error.CategoryNotFoundException;

import java.util.List;

public interface ICategoryService {

    public List<Category> listAll();
    public List<Category> listCategoriesUsedInForm();
    public Category save(Category category);
    public Category get(Long id) throws CategoryNotFoundException;
}
