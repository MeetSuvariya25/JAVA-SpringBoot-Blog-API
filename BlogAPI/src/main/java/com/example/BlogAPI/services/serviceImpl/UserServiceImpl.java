package com.example.BlogAPI.services.serviceImpl;

import com.example.BlogAPI.config.PasswordEncoder;
import com.example.BlogAPI.dto.UserDto;
import com.example.BlogAPI.exceptions.PasswordNullException;
import com.example.BlogAPI.exceptions.SQLIntegrityConstraintViolationException;
import com.example.BlogAPI.exceptions.SamePasswordException;
import com.example.BlogAPI.helper.JwtUtils;
import com.example.BlogAPI.models.Category;
import com.example.BlogAPI.models.Post;
import com.example.BlogAPI.models.User;
import com.example.BlogAPI.repository.CategoryRepository;
import com.example.BlogAPI.repository.PostRepository;
import com.example.BlogAPI.repository.UserRepository;
import com.example.BlogAPI.services.UserServices;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserServices {

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;


    @Override
    public User saveUser(User user) {

        String encodedPass = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPass);

        if (userRepository.findByUserEmail(user.getUserEmail()).isPresent()) {
            logger.error("Duplicate Email found. This email is already registered.");
            throw new SQLIntegrityConstraintViolationException();
        }
        logger.info("User account has been created.");
        return userRepository.save(user);

    }

    @Override
    public User changePassword(UserDto userDto) {
        User user = jwtUtils.findUserFromToken();
        if (userDto.getPassword() == null || userDto.getNewPassword() == null) {
            logger.error("Provide the current and new password");
            throw new PasswordNullException();
        }
        if (userDto.getPassword().equals(userDto.getNewPassword())) {
            logger.error("Both password is same.");
            throw new SamePasswordException();
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserEmail(), userDto.getPassword()));
        String encodedPass = bCryptPasswordEncoder.encode(userDto.getNewPassword());
        user.setPassword(encodedPass);
        return userRepository.save(user);
    }

    @Override
    public User updareUserDetails(UserDto userDto) {
        User user = jwtUtils.findUserFromToken();
        if (userDto.getName() == null) {
            userDto.setName(user.getUsername());
        }

        if (user != null) {
            user.setUserName(userDto.getName());
        }
        logger.info("User name is updated successfully.");
        return userRepository.save(user);
    }

    @Override
    public User deleteUser() {
        User user = jwtUtils.findUserFromToken();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Post> posts = postRepository.findByAuthor(pageable, user.getUserId());
        for (Post post : posts) {
            Category category = categoryRepository.findById(post.getCategory().getCategoryId()).get();
            if (category.getTotalNoOfPosts() > 0)
                category.setTotalNoOfPosts((category.getTotalNoOfPosts() - 1));
            else
                category.setTotalNoOfPosts(0);
            categoryRepository.save(category);
        }
        userRepository.delete(user);
        return user;
    }
}
