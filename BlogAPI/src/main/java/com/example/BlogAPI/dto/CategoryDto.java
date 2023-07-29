package com.example.BlogAPI.dto;

import lombok.Data;
import org.springframework.data.annotation.ReadOnlyProperty;

@Data
public class CategoryDto {

    private Long categoryId;
    private String categoryName;
    @ReadOnlyProperty
    private int totalNoOfPosts;

}
