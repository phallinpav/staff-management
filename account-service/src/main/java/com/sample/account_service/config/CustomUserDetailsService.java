package com.sample.account_service.config;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO: find user from database table accounts by username
        //  and return user accordingly
        if (username.equals("admin")) {
            return new User(username, "password", new ArrayList<>());
        }
        throw new UsernameNotFoundException("User not found");
    }
}
