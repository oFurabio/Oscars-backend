package com.ofurabio.oscars.repository;

import com.ofurabio.oscars.model.User;
import com.ofurabio.oscars.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findByEmail(String email);
    public List<User> findByRole(UserRole role);
}
