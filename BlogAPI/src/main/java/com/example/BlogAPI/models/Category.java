package com.example.BlogAPI.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category {

    @Id
    @Column(name = "category_id")
    @SequenceGenerator(
            name = "category_sequence",
            sequenceName = "category_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "category_sequence",
            strategy = GenerationType.SEQUENCE
    )
    private Long categoryId;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "total_no_of_posts")
    private int totalNoOfPosts;

    @OneToMany(
            mappedBy = "category",
            cascade = CascadeType.REMOVE
    )
    @JsonIgnore
    private List<Post> posts;

}
