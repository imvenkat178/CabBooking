package com.app.CabBooking.service;

import com.app.CabBooking.model.CabDriver;
import com.app.CabBooking.model.User;
import com.app.CabBooking.repository.CabDriverRepository;
import com.app.CabBooking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired private UserRepository userRepository;
    @Autowired private CabDriverRepository driverRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
            .<UserDetails>map(user -> new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
            ))
            .orElseGet(() -> driverRepository.findByName(username)
                .<UserDetails>map(driver -> new org.springframework.security.core.userdetails.User(
                    driver.getName(), driver.getPassword(),
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_DRIVER"))
                ))
                .orElseThrow(() -> new UsernameNotFoundException("User not found")));
    }
}