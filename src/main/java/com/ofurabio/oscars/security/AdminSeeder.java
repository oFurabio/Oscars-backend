package com.ofurabio.oscars.security;

import com.ofurabio.oscars.model.User;
import com.ofurabio.oscars.model.UserRole;
import com.ofurabio.oscars.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;

@Component
public class AdminSeeder implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        String email = "admin@freevote.com";
        String username = "ADMINISTRATOR";

        if (userRepository.findByEmail(email).isEmpty()) {
            String randomPassword = generateRandomPassword();
            User admin = new User();
            admin.setUsername(username);
            admin.setEmail(email);
            admin.setPassword(passwordEncoder.encode(randomPassword));
            admin.setRole(UserRole.ADMIN);

            userRepository.save(admin);
            System.out.println("Usuário ADMINISTRATOR criado com sucesso!");
            System.out.println("Senha temporária: " + randomPassword);
        } else
            System.out.println("Usuário ADMINISTRATOR já existe!");
    }

    private String generateRandomPassword() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[10];
        random.nextBytes(bytes);
        return Base64.getEncoder().withoutPadding().encodeToString(bytes);
    }
}
