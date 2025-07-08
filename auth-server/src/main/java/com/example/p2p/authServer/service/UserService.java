package com.example.p2p.authServer.service;

import com.example.p2p.authServer.dto.UserDTO;
import com.example.p2p.authServer.model.User;
import com.example.p2p.authServer.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RegisteredClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repo, PasswordEncoder encoder, RegisteredClientRepository repository) {
        this.userRepository = repo;
        this.passwordEncoder = encoder;
        this.clientRepository = repository;
    }

    public boolean register(UserDTO dto) {
        if (userRepository.existsByName(dto.username)) {
            return false;
        }
        String encodedPassword = passwordEncoder.encode(dto.password);
        User user = new User(dto.username, encodedPassword, new SecureRandom().nextLong() & Long.MAX_VALUE);
        userRepository.saveUser(user);
        clientRepository.save(user.toRegisteredClient());
        return true;
    }
}
