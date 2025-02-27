package com.ofurabio.oscars.service;

import com.ofurabio.oscars.model.Category;
import com.ofurabio.oscars.model.Nominee;
import com.ofurabio.oscars.model.User;
import com.ofurabio.oscars.model.Vote;
import com.ofurabio.oscars.repository.CategoryRepository;
import com.ofurabio.oscars.repository.NomineeRepository;
import com.ofurabio.oscars.repository.UserRepository;
import com.ofurabio.oscars.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class VoteService {
    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NomineeRepository nomineeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public ResponseEntity<String> vote(String userEmail, Long categoryId, Long nomineeId) {
        Optional<User> user = userRepository.findByEmail(userEmail);
        Optional<Category> category = categoryRepository.findById(categoryId);
        Optional<Nominee> nominee = nomineeRepository.findById(nomineeId);

        if (user.isEmpty() || category.isEmpty() || nominee.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário, nominee ou categoria não encontrados");

        Optional<Vote> existingVote = voteRepository.findByUserAndCategory(user.get(), category.get());

        if (existingVote.isPresent())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já votou nesta categoria");

        if (!nominee.get().getCategories().contains(category.get()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nominee não pertence à categoria especificada");

        Vote vote = new Vote();
        vote.setUser(user.get());
        vote.setCategory(category.get());
        vote.setNominee(nominee.get());

        voteRepository.save(vote);
        return ResponseEntity.status(HttpStatus.OK).body("Voto registrado com sucesso.");
    }

    public List<Vote> getVotesByCategory(Long categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        return category.map(voteRepository::findByCategory).orElse(Collections.emptyList());
    }
}
