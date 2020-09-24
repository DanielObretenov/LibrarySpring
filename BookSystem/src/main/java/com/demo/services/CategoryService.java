package com.demo.services;

import com.demo.entities.Category;

import java.io.IOException;

public interface CategoryService {
    void seedCategories() throws IOException;
    Category getCategoryById(Long id);
    int getCategoryCount();
}
