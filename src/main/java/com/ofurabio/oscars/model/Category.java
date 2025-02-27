package com.ofurabio.oscars.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name = "tb_categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 5, max = 100)
    private String title;

    @ManyToMany(mappedBy = "categories")
    @JsonIgnoreProperties("categories")
    private List<Nominee> nominees;

    /*Getters & Setters*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Nominee> getNominees() {
        return nominees;
    }

    public void setNominees(List<Nominee> nominees) {
        this.nominees = nominees;
    }
}
