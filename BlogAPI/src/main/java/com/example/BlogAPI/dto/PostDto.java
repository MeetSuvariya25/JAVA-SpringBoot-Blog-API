package com.example.BlogAPI.dto;

import lombok.Data;

@Data
public class PostDto {

    private Long postId;
    private String postTitle;
    private String postContent;
    private String creationDate;
    private String lastModifiedAt;
    private Long userID;
    private Long categoryId;
}
