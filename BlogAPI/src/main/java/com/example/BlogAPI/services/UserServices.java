package com.example.BlogAPI.services;

import com.example.BlogAPI.dto.UserDto;
import com.example.BlogAPI.models.User;
import org.springframework.stereotype.Service;

@Service
public interface UserServices {

    public User saveUser(User user);

    public User changePassword(UserDto userDto);

    public User updareUserDetails(UserDto userDto);

    public User deleteUser();


}
