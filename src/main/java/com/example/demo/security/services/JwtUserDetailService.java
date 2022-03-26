package com.example.demo.security.services;

import java.util.Arrays;

import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailService implements UserDetailsService{
	
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
			throw new UsernameNotFoundException("Username not found with username: " + username);
		}
		return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
				Arrays.asList(new SimpleGrantedAuthority(user.getRole()))
                );
    }
    
}
