package com.ofurabio.oscars.security;

import com.ofurabio.oscars.model.User;
import com.ofurabio.oscars.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty())
            throw new UsernameNotFoundException("Usuário não encontrado com o email: " + email);

        return new org.springframework.security.core.userdetails.User(
                user.get().getEmail(),
                user.get().getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.get().getRole().name()))
        );
    }
}
