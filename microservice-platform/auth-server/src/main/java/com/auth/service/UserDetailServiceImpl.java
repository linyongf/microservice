package com.auth.service;

import com.auth.service.feign.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class UserDetailServiceImpl implements UserDetailsService {

    @Resource
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userService.getUserByUsername(username).getData();
    }
}
