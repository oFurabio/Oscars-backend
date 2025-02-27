package com.ofurabio.oscars.controller;

import com.ofurabio.oscars.model.User;
import com.ofurabio.oscars.model.UserLogin;
import com.ofurabio.oscars.model.UserRole;
import com.ofurabio.oscars.repository.UserRepository;
import com.ofurabio.oscars.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/admins")
    public ResponseEntity<List<User>> getAdmins() {
        return ResponseEntity.ok(userRepository.findByRole(UserRole.ADMIN));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id, Authentication authentication) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty())
            return ResponseEntity.notFound().build();

        String authenticatedUserEmail = authentication.getName();

        if (user.get().getEmail().equals(authenticatedUserEmail))
            return ResponseEntity.ok(user.get());

        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin)
            return ResponseEntity.ok(user.get());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PostMapping("/login")
    public ResponseEntity<UserLogin> authenticateUser(@RequestBody UserLogin userLogin) {
        if (userLogin == null || userLogin.getEmail() == null || userLogin.getPassword() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        return userService.authenticateUser(userLogin)
                .map(response -> ResponseEntity.status(HttpStatus.OK).body(response))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PostMapping("/register")
    public ResponseEntity<User> postUser(@RequestBody @Valid User user) {
        return userService.registerUser(user)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response))
                .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody @Valid User user, Principal principal) {
        Optional<User> authenticatedUser = userRepository.findByEmail(principal.getName());

        if (authenticatedUser.isEmpty())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        User userToUpdate = authenticatedUser.get();

        userToUpdate.setUsername(user.getUsername());
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setPassword(user.getPassword());

        Optional<User> updatedUser = userService.updateUser(userToUpdate);

        return updatedUser.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
