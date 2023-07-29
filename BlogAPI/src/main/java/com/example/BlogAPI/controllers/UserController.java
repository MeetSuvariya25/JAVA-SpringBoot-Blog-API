package com.example.BlogAPI.controllers;

import com.example.BlogAPI.dto.UserDto;
import com.example.BlogAPI.exceptions.PasswordIncorrectException;
import com.example.BlogAPI.exceptions.UserNotFoundException;
import com.example.BlogAPI.exceptions.UserNotSavedException;
import com.example.BlogAPI.helper.Constants;
import com.example.BlogAPI.helper.JwtUtils;
import com.example.BlogAPI.models.User;
import com.example.BlogAPI.repository.UserRepository;
import com.example.BlogAPI.services.UserServices;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class UserController {

    User user;
    private static final Logger logger = LogManager.getLogger(UserController.class);
    @Autowired
    private UserServices userServices;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;


    @PostMapping(path = Constants.LOGIN_API)
    public ResponseEntity<?> login(@RequestBody UserDto userDto) {
        try {
            if (!userRepository.findByUserEmail(userDto.getEmail()).isPresent()) {
                logger.error("User not found.");
                throw new UserNotFoundException();
            }
            user = jwtUtils.findByEmail(userDto.getEmail());
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));
            Map<String, String> tokens = jwtUtils.generateToken(user);
            logger.info("Logged in successfully.");
            return ResponseEntity.ok(tokens);
        } catch (BadCredentialsException exception) {
            logger.error("Incorrect credentials");
            throw new PasswordIncorrectException();
        }
    }

    @RequestMapping(
            method = RequestMethod.POST,
            path = Constants.USER_API
    )
    public ResponseEntity<?> signUp(@RequestBody User user) {
        try {
            logger.info("saving user");
            this.user = userServices.saveUser(user);
        } catch (DataIntegrityViolationException exception) {
            logger.error("Data Integrity Error");
            throw new IllegalStateException(exception);
        }
        if (this.user != null) {
            Map<String, String> tokens = this.jwtUtils.generateToken(user);
            logger.info("token generated");
            return ResponseEntity.ok(tokens);
        } else {
            logger.error("User not saved");
            throw new UserNotSavedException();
        }
    }

    @PutMapping(path = Constants.CHANGE_PASSWORD)
    public ResponseEntity<?> changePassword(@RequestBody UserDto userDto) {
        user = userServices.changePassword(userDto);
        return ResponseEntity.ok("Password changed successfully.");
    }

    @PutMapping(path = Constants.USER_API)
    public ResponseEntity<?> updateUserDetails(@RequestBody UserDto userDto) {
        try {
            user = userServices.updareUserDetails(userDto);
            return ResponseEntity.ok(user);
        } catch (NullPointerException ex) {
            throw new UserNotSavedException();
        }
    }

    @DeleteMapping(path = Constants.USER_API)
    public void deleteUser() {
        try {
            userServices.deleteUser();
        } catch (Exception e) {
            throw new UserNotFoundException();
        }
    }
}
