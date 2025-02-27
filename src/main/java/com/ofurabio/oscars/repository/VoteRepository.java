package com.ofurabio.oscars.repository;

import com.ofurabio.oscars.model.Category;
import com.ofurabio.oscars.model.User;
import com.ofurabio.oscars.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    public Optional<Vote> findByUserAndCategory(User user, Category category);
    public List<Vote> findByCategory(Category category);
}
