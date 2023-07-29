package com.example.BlogAPI.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Entity(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails {


    @Id
    @Column(name = "user_id")
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "user_sequence",
            strategy = GenerationType.SEQUENCE
    )
    private Long userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "email")
    private String userEmail;

    @Column(name = "password")
    //@JsonIgnore
    private String password;

    @Column(name = "total_no_of_posts", nullable = true)
    private int noOfPosts;

    @OneToMany(
            mappedBy = "author",
            cascade = CascadeType.REMOVE
    )
    @JsonIgnore
    private List<Post> posts;

    //@JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities;
        SimpleGrantedAuthority authority;
        authority = new SimpleGrantedAuthority("ROLE_USER");
        authorities = Arrays.asList(authority);
        return authorities;
    }

    //@JsonIgnore
    @Override
    public String getUsername() {
        return userEmail;
    }

    //@JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //@JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //@JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //@JsonIgnore
    @Override
    public boolean isEnabled() {
        return false;
    }
}
