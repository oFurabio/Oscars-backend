package com.ofurabio.oscars.controller;

import com.ofurabio.oscars.model.Category;
import com.ofurabio.oscars.model.Nominee;
import com.ofurabio.oscars.repository.CategoryRepository;
import com.ofurabio.oscars.repository.NomineeRepository;
import com.ofurabio.oscars.service.NomineeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/nominees")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class NomineeController {

    @Autowired
    private NomineeRepository nomineeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private NomineeService nomineeService;

    @GetMapping
    public ResponseEntity<List<Nominee>> getAll() {
        return ResponseEntity.ok(nomineeRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Nominee> getById(@PathVariable Long id) {
        return nomineeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<Nominee>> getByName (@PathVariable String name){
        return ResponseEntity.ok(nomineeRepository.findAllByNameContainingIgnoreCase(name));
    }

    @GetMapping("/category/{id}")
    public ResponseEntity <List<Nominee>> getByCategory (@PathVariable Long id) {
        return ResponseEntity.ok(nomineeRepository.findByCategories(categoryRepository.findById(id)));
    }

    @PostMapping
    public ResponseEntity<Nominee> post(@Valid @RequestBody Nominee nominee) {
        List<Long> categoryIds = nominee.getCategories().stream()
                .map(Category::getId)
                .toList();

        List<Category> categories = categoryRepository.findAllById(categoryIds);

        if (categories.size() != categoryIds.size())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "One or more categories not found", null);

        nominee.setCategories(categories);

        return ResponseEntity.status(HttpStatus.CREATED).body(nomineeRepository.save(nominee));
    }

    @PostMapping("/set-winner/{nomineeId}")
    public ResponseEntity<String> setWinner(@PathVariable Long nomineeId) {
        return nomineeService.setWinner(nomineeId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Nominee> put(@PathVariable Long id, @Valid @RequestBody Nominee nominee) {
        return nomineeRepository.findById(id)
                .map(existingNominee -> {
                    List<Long> categoryIds = nominee.getCategories().stream()
                            .map(Category::getId)
                            .toList();

                    List<Category> categories = categoryRepository.findAllById(categoryIds);

                    if (categories.size() != categoryIds.size())
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "One or more categories not found");

                    existingNominee.setName(nominee.getName());
                    existingNominee.setInfo(nominee.getInfo());
                    existingNominee.setPhotoUrl(nominee.getPhotoUrl());
                    existingNominee.setCategories(categories);

                    return ResponseEntity.ok(nomineeRepository.save(existingNominee));
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nominee not found"));
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        Optional<Nominee> nominee = nomineeRepository.findById(id);

        if(nominee.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        nomineeRepository.deleteById(id);
    }
}
