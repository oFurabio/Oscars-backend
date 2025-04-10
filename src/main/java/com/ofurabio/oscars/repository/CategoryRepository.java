package com.ofurabio.oscars.repository;

import com.ofurabio.oscars.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    public List<Category> findAllByTitleContainingIgnoreCase(@Param("title") String title);
}
