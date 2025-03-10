package org.daylight.routenavigator.backend.services.entityservices;

import org.daylight.routenavigator.backend.components.SpringContextHolder;
import org.daylight.routenavigator.backend.entities.Token;
import org.daylight.routenavigator.backend.entities.User;
import org.daylight.routenavigator.backend.repositories.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * A service for the Token repository
 */
@Service
public class TokenService {
    @Autowired
    private TokenRepository tokenRepository;

    public void delete(Token token) {
        tokenRepository.delete(token);
    }

    public void save(Token token) {
        tokenRepository.save(token);
    }

    public List<Token> findAllByUserId(long userId) {
        return tokenRepository.findAllByUserId(userId);
    }

    public Optional<Token> findByToken(UUID token) {
        return tokenRepository.findByToken(token);
    }

    private List<UUID> generateUUIDBatch(int size) {
        return Stream.generate(UUID::randomUUID)
                .limit(size)
                .toList();
    }

    private Optional<User> findUserById(long userId) {
        UserService userService = SpringContextHolder.getBean(UserService.class);
        return userService.findById(userId);
    }

    public Optional<Token> generateToken(long userId) {
        Optional<User> user = findUserById(userId);
        if (user.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(generateToken(user.get()));
    }

    public Token generateToken(User user) {
        OffsetDateTime expires = OffsetDateTime.now().plusDays(7);
        return generateToken(user, expires);
    }

    /**
     * A function that generates a new token that is guaranteed to be not used yet
     * @param user The user
     * @param expires An expiration date
     * @return A token object
     */
    public Token generateToken(User user, OffsetDateTime expires) {
        int batchSize = 100;
        List<UUID> uniqueTokens = new ArrayList<>();

        while (uniqueTokens.isEmpty()) {
            List<UUID> generatedTokens = generateUUIDBatch(batchSize);
            List<Token> existingGeneratedTokens = tokenRepository
                    .findByTokenIn(generatedTokens);
            uniqueTokens = generatedTokens.stream()
                    .filter(token -> existingGeneratedTokens.stream().noneMatch(token1 -> token1.getToken().equals(token)))
                    .toList();
        }

        UUID uniqueToken = uniqueTokens.getFirst();
        Token token = new Token()
                .setUser(user)
                .setToken(uniqueToken)
                .setExpires(expires);
        tokenRepository.save(token);
        return token;
    }

    public boolean checkTokenActive(Token token) {
        return token.getExpires().isAfter(OffsetDateTime.now());
    }
}
