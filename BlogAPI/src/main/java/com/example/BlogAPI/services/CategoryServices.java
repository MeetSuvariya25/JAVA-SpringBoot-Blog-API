package com.example.BlogAPI.services;

import com.example.BlogAPI.dto.CategoryDto;
import com.example.BlogAPI.models.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryServices {

    public List<Category> getAllCategory();

    public Category addCategory(CategoryDto categoryDto);

    public Category updateCategory(Long categoryId, CategoryDto categoryDto);

    public Category deleteCategory(Long categoryId);
}
