package com.example.BlogAPI.services;

import com.example.BlogAPI.dto.PostDto;
import com.example.BlogAPI.models.Post;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface PostServices {

    public Post addPost(PostDto postDto);

    public Post updatePost(PostDto postDto);

    public void deletePost(Long postId);

    public Page<Post> getAllPostByAuthor(int pageNumber, int pageSize, Long userID);

    public Page<Post> getAllPost(int pageNumber, int pageSize);

    public Page<Post> getAllPostByCategory(int pageNumber, int pageSize, Long categoryId);

}
