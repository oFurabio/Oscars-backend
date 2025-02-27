package com.ofurabio.oscars.service;

import com.ofurabio.oscars.model.User;
import com.ofurabio.oscars.model.UserLogin;
import com.ofurabio.oscars.model.UserRole;
import com.ofurabio.oscars.repository.UserRepository;
import com.ofurabio.oscars.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<User> registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent())
            return Optional.empty();

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(UserRole.CLIENT);
        return Optional.of(userRepository.save(user));
    }

    public Optional<User> updateUser(User user) {
        if (userRepository.findById(user.getId()).isPresent()) {
            Optional<User> searchUser = userRepository.findByEmail(user.getEmail());

            if (searchUser.isPresent() && !Objects.equals(searchUser.get().getId(), user.getId()))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já existe!");

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return Optional.of(userRepository.save(user));
        }

        return Optional.empty();
    }

    public Optional<UserLogin> authenticateUser(UserLogin userLogin) {
        if (userLogin == null || userLogin.getEmail() == null || userLogin.getPassword() == null)
            return Optional.empty();

        var credentials = new UsernamePasswordAuthenticationToken(
                userLogin.getEmail(),
                userLogin.getPassword()
        );

        try {
            Authentication authentication = authenticationManager.authenticate(credentials);

            if (authentication.isAuthenticated()) {
                Optional<User> user = userRepository.findByEmail(userLogin.getEmail());

                if (user.isPresent()) {
                    userLogin.setId(user.get().getId());
                    userLogin.setUsername(user.get().getUsername());
                    userLogin.setToken(generateToken(userLogin.getEmail()));
                    userLogin.setRole(user.get().getRole());
                    userLogin.setPassword("");

                    return Optional.of(userLogin);
                }
            }
        } catch (AuthenticationException e) {
            return Optional.empty();
        }

        return Optional.empty();
    }

    private String generateToken(String email) {
        return "Bearer " + jwtService.generateToken(email);
    }
}
