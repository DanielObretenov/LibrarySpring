package com.demo.services.impl;

import com.demo.entities.Category;
import com.demo.repositories.CategoryRepository;
import com.demo.services.CategoryService;
import com.demo.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.demo.constants.GlobalConstants.*;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final FileUtil fileUtil;
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(FileUtil fileUtil, CategoryRepository categoryRepository) {
        this.fileUtil = fileUtil;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void seedCategories() throws IOException {
        String[] categoriesArray = fileUtil.readFileContent(CATEGORIES_FILE_PATH);
        List<String> categoryNames = this.categoryRepository.findAll().stream()
                .map(Category::getName)
                .collect(Collectors.toList());
            for (String name : categoriesArray) {
                if (!categoryNames.contains(name)){
                    Category category = new Category(name);
                    categoryRepository.saveAndFlush(category);
                }

            }

    }

    @Override
    public Category getCategoryById(Long id) {
        return this.categoryRepository.getOne(id);
    }

    @Override
    public int getCategoryCount() {
        return (int) this.categoryRepository.count();
    }

}
