package com.example.BlogAPI.repository;

import com.example.BlogAPI.models.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Override
    Page<Post> findAll(Pageable pageable);

    @Query(
            value = "SELECT * FROM post WHERE category= ?1",
            nativeQuery = true
    )
    Page<Post> findByCategory(Pageable pageable, Long categoryId);

    @Query(
            value = "SELECT * FROM post WHERE author= ?1",
            nativeQuery = true
    )
    Page<Post> findByAuthor(Pageable pageable, Long userId);
}
