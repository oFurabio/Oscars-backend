package com.ofurabio.oscars.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_votes")
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties("votes")
    private User user;

    @ManyToOne
    @JoinColumn(name = "nominee_id", nullable = false)
    @JsonIgnoreProperties({"categories", "votes"})
    private Nominee nominee;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnoreProperties({"nominees", "votes"})
    private Category category;

    /*Getters & Setters*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Nominee getNominee() {
        return nominee;
    }

    public void setNominee(Nominee nominee) {
        this.nominee = nominee;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
