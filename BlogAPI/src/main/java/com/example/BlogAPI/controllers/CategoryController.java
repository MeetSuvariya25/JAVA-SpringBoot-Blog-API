package com.example.BlogAPI.controllers;

import com.example.BlogAPI.dto.CategoryDto;
import com.example.BlogAPI.exceptions.CategoryNotDeletedException;
import com.example.BlogAPI.exceptions.CategoryNotFoundException;
import com.example.BlogAPI.exceptions.CategoryNotSavedException;
import com.example.BlogAPI.helper.Constants;
import com.example.BlogAPI.models.Category;
import com.example.BlogAPI.services.CategoryServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryController {

    @Autowired
    private CategoryServices categoryServices;

    @GetMapping(path = Constants.CATEGORY_API)
    public ResponseEntity<?> getAllCategory() {
        try {
            List<Category> categories = categoryServices.getAllCategory();
            return ResponseEntity.ok(categories);
        } catch (Exception exception) {
            throw new CategoryNotFoundException();
        }
    }

    @PostMapping(path = Constants.CATEGORY_API)
    public ResponseEntity<?> addCategory(@RequestBody CategoryDto categoryDto) {
        try {
            Category category = categoryServices.addCategory(categoryDto);
            return ResponseEntity.ok(category);
        } catch (Exception exception) {
            throw new CategoryNotSavedException();
        }
    }

    @PutMapping(path = Constants.CATEGORY_UPDATE_API)
    public ResponseEntity<?> updateCategory(@PathVariable Long categoryId, @RequestBody CategoryDto categoryDto) {
        try {
            Category category = categoryServices.updateCategory(categoryId, categoryDto);
            return ResponseEntity.ok(category);
        } catch (Exception exception) {
            throw new CategoryNotSavedException();
        }
    }

    @DeleteMapping(path = Constants.CATEGORY_UPDATE_API)
    public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId) {
        try {
            categoryServices.deleteCategory(categoryId);
            return ResponseEntity.ok("Category deleted successfully.");
        } catch (Exception e) {
            throw new CategoryNotDeletedException();
        }
    }


}
