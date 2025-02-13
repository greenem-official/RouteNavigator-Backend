package org.daylight.marketplace.backend.services;

import org.daylight.marketplace.backend.entities.Token;
import org.daylight.marketplace.backend.entities.User;
import org.daylight.marketplace.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    public void save(User user) {
        userRepository.save(user);
    }

    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findUserFromToken(UUID tokenUuid) {
        Optional<Token> token = tokenService.findByToken(tokenUuid);
        if (token.isPresent() && tokenService.checkTokenActive(token.get())) {
            return this.findById(token.get().getUserId());
        }
        return Optional.empty();
    }
}
