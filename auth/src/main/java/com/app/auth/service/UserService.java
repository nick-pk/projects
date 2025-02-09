package com.app.auth.service;

import com.app.auth.entity.User;
import com.app.auth.exception.InvalidPasswordException;
import com.app.auth.exception.InvalidTokenException;
import com.app.auth.repository.UserRepository;
import com.app.auth.utill.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.app.auth.utill.Constant.TOKEN_PREFIX;

@Service
public class UserService {
    private UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private JWTService jwtService;
    private PasswordEncoder encoder;
    private PasswordValidationService passwordValidationService;

    @Autowired
    public UserService(UserRepository userRepository, AuthenticationManager authenticationManager, JWTService jwtService, PasswordEncoder encoder, PasswordValidationService passwordValidationService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.encoder = encoder;
        this.passwordValidationService = passwordValidationService;
    }

    public User save(User user) {
        if (!passwordValidationService.isValid(user.getPassword())) {
            throw new InvalidPasswordException();
        }
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public String verify(User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        // Set the authentication in the SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtService.generateToken(authentication.getName());
    }

    public boolean validateToken(String token) {
        token = extractToken(token);
        String username = jwtService.extractUsername(token);  // Extract username from token
        Optional<User> userOptional = userRepository.findByUsername(username);  // Retrieve user from DB
        if (!userOptional.isPresent()) {
            return false;  // User not found
        }
        return jwtService.validateToken(token, username);  // Validate token with extracted username and DB usernam
    }

    private String extractToken(String token) {
        if (token != null && token.startsWith(TOKEN_PREFIX)) {
            return token.substring(Constant.INDEX);  // Remove "Bearer " prefix
        }
        throw new InvalidTokenException();
    }
}
