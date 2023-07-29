package com.example.BlogAPI.services;

import com.example.BlogAPI.exceptions.UserNotFoundException;
import com.example.BlogAPI.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserAuthDetailService implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        com.example.BlogAPI.models.User user = userRepository.findByUserEmail(email).orElseThrow(UserNotFoundException::new);
        return new org.springframework.security.core.userdetails.User(email, user.getPassword(), user.getAuthorities());
    }
}
