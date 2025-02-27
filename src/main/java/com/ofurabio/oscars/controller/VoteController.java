package com.ofurabio.oscars.controller;

import com.ofurabio.oscars.dto.VoteRequest;
import com.ofurabio.oscars.model.Vote;
import com.ofurabio.oscars.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/votes")
public class VoteController {

    @Autowired
    private VoteService voteService;

    @PostMapping("/vote")
    public ResponseEntity<String> vote(@RequestBody VoteRequest voteRequest, Authentication authentication) {
        String userEmail = authentication.getName();
        return voteService.vote(userEmail, voteRequest.getCategoryId(), voteRequest.getNomineeId());
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Vote>> getVotesByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(voteService.getVotesByCategory(categoryId));
    }
}