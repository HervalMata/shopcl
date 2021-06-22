package com.shopcl.shopclbackend.service.impl;

import com.shopcl.common.entity.Category;

import java.util.List;0

public interface ICategoryService {

    public List<Category> listAll();
    public List<Category> listCategoriesUsedInForm();
}
