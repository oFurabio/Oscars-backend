package com.ofurabio.oscars.repository;

import com.ofurabio.oscars.model.Category;
import com.ofurabio.oscars.model.Nominee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NomineeRepository extends JpaRepository<Nominee, Long> {
    public List<Nominee> findAllByNameContainingIgnoreCase(@Param("name") String name);
    public List<Nominee> findByCategories(Category category);
}
