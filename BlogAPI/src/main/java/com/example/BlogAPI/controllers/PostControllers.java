package com.example.BlogAPI.controllers;

import com.example.BlogAPI.dto.PostDto;
import com.example.BlogAPI.exceptions.PostNotDeletedException;
import com.example.BlogAPI.exceptions.PostNotFoundException;
import com.example.BlogAPI.exceptions.PostNotSavedException;
import com.example.BlogAPI.helper.Constants;
import com.example.BlogAPI.models.Post;
import com.example.BlogAPI.services.PostServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PostControllers {

    @Autowired
    private PostServices postServices;

    @GetMapping(path = Constants.POST_API)
    public ResponseEntity<?> getAllPost(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize) {
        try {
            return ResponseEntity.ok(postServices.getAllPost(pageNumber, pageSize));
        } catch (Exception exception) {
            throw new PostNotFoundException();
        }
    }

    @GetMapping(path = Constants.POST_BY_AUTHOR_API)
    public ResponseEntity<?> getAllPostByAuthor(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @PathVariable Long userId) {
        return ResponseEntity.ok(postServices.getAllPostByAuthor(pageNumber, pageSize, userId));
    }

    @GetMapping(path = Constants.POST_BY_CATEGORY_API)
    public ResponseEntity<?> getAllPostByCategory(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @PathVariable Long categoryId) {
        return ResponseEntity.ok(postServices.getAllPostByCategory(pageNumber, pageSize, categoryId));
    }

    @PostMapping(path = Constants.POST_API)
    public ResponseEntity<?> addPost(@RequestBody PostDto postDto) {
        try {
            Post post = postServices.addPost(postDto);
            return ResponseEntity.ok(post);
        } catch (Exception exception) {
            throw new PostNotSavedException();
        }
    }

    @PutMapping(path = Constants.POST_API)
    public ResponseEntity<?> updatePost(@RequestBody PostDto postDto) {
        try {
            Post post = postServices.updatePost(postDto);
            return ResponseEntity.ok(post);
        } catch (Exception exception) {
            throw new PostNotSavedException();
        }
    }

    @DeleteMapping(path = Constants.POST_DELETE_API)
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        try {
            postServices.deletePost(postId);
            return ResponseEntity.ok("Post deleted successfully");
        } catch (Exception exception) {
            throw new PostNotDeletedException();
        }
    }

}
