package com.app.auth.service;

import com.app.auth.entity.User;
import com.app.auth.entity.UserPrincipal;
import com.app.auth.repository.UserRepository;
import com.app.auth.utill.Constant;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    private UserRepository userRepository;

    public UserDetailServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException(String.format(Constant.USER_NOT_FOUND, username));
        }
        return new UserPrincipal(userOptional.get());
    }
}
