package com.shopcl.shopclbackend.repository;

import com.shopcl.common.entity.Category;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Long> {
}
