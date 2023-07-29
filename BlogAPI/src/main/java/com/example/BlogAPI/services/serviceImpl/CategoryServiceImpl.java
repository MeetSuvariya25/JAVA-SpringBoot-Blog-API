package com.example.BlogAPI.services.serviceImpl;

import com.example.BlogAPI.dto.CategoryDto;
import com.example.BlogAPI.exceptions.CategoryNotFoundException;
import com.example.BlogAPI.exceptions.CategoryNotSavedException;
import com.example.BlogAPI.models.Category;
import com.example.BlogAPI.repository.CategoryRepository;
import com.example.BlogAPI.services.CategoryServices;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryServices {

    @Autowired
    private CategoryRepository categoryRepository;
    private Logger logger = LogManager.getLogger(CategoryServiceImpl.class);

    @Override
    public List<Category> getAllCategory() {
        try {
            List<Category> categories = categoryRepository.findAll();
            logger.info("Categories found successfully.");
            return categories;
        } catch (Exception exception) {
            logger.error("Category not found.");
            throw new CategoryNotFoundException();
        }
    }

    @Override
    public Category addCategory(CategoryDto categoryDto) {
        if (categoryDto.getCategoryName() != null) {
            Category category = new Category();
            category.setCategoryName(categoryDto.getCategoryName());
            category.setTotalNoOfPosts(0);
            logger.info("Category saved successfully.");
            return categoryRepository.save(category);
        } else {
            logger.error("Enter the proper name of the Category.");
            throw new CategoryNotSavedException();
        }
    }

    @Override
    public Category updateCategory(Long categoryId, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(categoryId).get();
        if (category != null) {
            if (categoryDto.getCategoryName() == null) {
                categoryDto.setCategoryName(category.getCategoryName());
            }
            category.setCategoryName(categoryDto.getCategoryName());
            logger.info("Category updated successfully");
            return categoryRepository.save(category);
        } else {
            logger.error("Category not Found.");
            throw new CategoryNotFoundException();
        }
    }

    @Override
    public Category deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).get();
        if (category != null) {
            categoryRepository.delete(category);
            return category;
        } else {
            logger.error("Category not Found.");
            throw new CategoryNotFoundException();
        }
    }
}
