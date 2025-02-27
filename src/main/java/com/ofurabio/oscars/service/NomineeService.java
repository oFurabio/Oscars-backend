package com.ofurabio.oscars.service;

import com.ofurabio.oscars.model.Nominee;
import com.ofurabio.oscars.repository.NomineeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NomineeService {

    @Autowired
    private NomineeRepository nomineeRepository;

    public ResponseEntity<String> setWinner(Long nomineeId) {
        Optional<Nominee> nominee = nomineeRepository.findById(nomineeId);

        if (nominee.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nominee não encontrado");

        nominee.get().setWinner(true);
        nomineeRepository.save(nominee.get());

        return ResponseEntity.status(HttpStatus.OK).body("Vencedor definido com sucesso");
    }
}
