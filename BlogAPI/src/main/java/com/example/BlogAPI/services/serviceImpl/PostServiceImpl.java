package com.example.BlogAPI.services.serviceImpl;

import com.example.BlogAPI.dto.PostDto;
import com.example.BlogAPI.exceptions.*;
import com.example.BlogAPI.helper.JwtUtils;
import com.example.BlogAPI.models.Category;
import com.example.BlogAPI.models.Post;
import com.example.BlogAPI.models.User;
import com.example.BlogAPI.repository.CategoryRepository;
import com.example.BlogAPI.repository.PostRepository;
import com.example.BlogAPI.repository.UserRepository;
import com.example.BlogAPI.services.PostServices;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class PostServiceImpl implements PostServices {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private JwtUtils jwtUtils;

    private Logger logger = LogManager.getLogger(PostServiceImpl.class);

    @Override
    public Page<Post> getAllPost(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Post> posts = postRepository.findAll(pageable);
        return posts;
    }

    @Override
    public Post addPost(PostDto postDto) {
        try {
            // Post
            Post post = new Post();
            User user = jwtUtils.findUserFromToken();

            Category category = categoryRepository.findById(postDto.getCategoryId()).orElseThrow(CategoryNotFoundException::new);
            post.setCategory(category);

            post.setPostContent(Objects.requireNonNull(postDto.getPostContent()));
            post.setPostTitle(Objects.requireNonNull(postDto.getPostTitle()));
            post.setCreationDate(String.valueOf(LocalDateTime.now()));
            post.setLastModifiedAt(String.valueOf(LocalDateTime.now()));
            logger.info("Post is added successfully");

            if (user != null) {
                post.setAuthor(user);
                user.setNoOfPosts(user.getNoOfPosts() + 1);
                userRepository.save(user);
            } else {
                throw new UserNotFoundException();
            }

            if (category != null) {
                category.setTotalNoOfPosts(category.getTotalNoOfPosts() + 1);
                categoryRepository.save(category);
            } else {
                throw new CategoryNotSavedException();
            }
            return postRepository.save(post);
        } catch (Exception exception) {
            logger.error("Post is not saved.");
            throw new PostNotSavedException();
        }
    }

    @Override
    public Post updatePost(PostDto postDto) {
        try {
            Post post = postRepository.findById(Objects.requireNonNull(postDto.getPostId())).orElseThrow(PostNotFoundException::new);
            if (postDto.getPostTitle() == null) {
                postDto.setPostTitle(post.getPostTitle());
            }
            if (postDto.getPostContent() == null) {
                postDto.setPostTitle(post.getPostTitle());
            }
            if (postDto.getCategoryId() == null) {
                postDto.setCategoryId(post.getCategory().getCategoryId());
            }
            post.setPostTitle(postDto.getPostTitle());
            post.setPostContent(postDto.getPostContent());
            post.setLastModifiedAt(String.valueOf(LocalDateTime.now()));
            //Category
            Category category = categoryRepository.findById(Objects.requireNonNull(postDto.getCategoryId())).orElseThrow(CategoryNotFoundException::new);
            post.setCategory(category);
            logger.info("Post is saved successfully");
            return postRepository.save(post);
        } catch (Exception exception) {
            logger.error("Post is not saved.");
            throw new PostNotSavedException();
        }

    }

    @Override
    public void deletePost(Long postID) {
        if (postID != null) {
            User user = jwtUtils.findUserFromToken();
            Post post = postRepository.findById(postID).get();
            Category category = categoryRepository.findById(post.getCategory().getCategoryId()).get();
            if (post.getAuthor().equals(user)) {
                postRepository.delete(post);
                if (user.getNoOfPosts() > 0)
                    user.setNoOfPosts((user.getNoOfPosts() - 1));
                else
                    user.setNoOfPosts(0);
                userRepository.save(user);
                if (category.getTotalNoOfPosts() > 0)
                    category.setTotalNoOfPosts((category.getTotalNoOfPosts() - 1));
                else
                    category.setTotalNoOfPosts(0);
                categoryRepository.save(category);
            } else {
                throw new PostNotFoundException();
            }
        }
    }

    @Override
    public Page<Post> getAllPostByAuthor(int pageNumber, int pageSize, Long userID) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Post> posts = null;
        if (userID != null) {
            User user = userRepository.findById(userID).orElseThrow(UserNotFoundException::new);
            posts = postRepository.findByAuthor(pageable, user.getUserId());
            return posts;
        } else {
            logger.error("Please enter the valid user id.");
            throw new UserNotFoundException();
        }
    }

    @Override
    public Page<Post> getAllPostByCategory(int pageNumber, int pageSize, Long categoryId) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Post> posts = null;
        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
            posts = postRepository.findByCategory(pageable, category.getCategoryId());
            logger.info("The list of Post is found successfully.");
            return posts;
        } else {
            logger.error("Please enter the valid category id.");
            throw new CategoryNotFoundException();
        }
    }
}
