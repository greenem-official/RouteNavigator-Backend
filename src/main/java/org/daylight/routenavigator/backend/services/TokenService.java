package org.daylight.routenavigator.backend.services;

import org.daylight.routenavigator.backend.entities.Token;
import org.daylight.routenavigator.backend.repositories.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class TokenService {
    @Autowired
    private TokenRepository tokenRepository;

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

    public Token generateToken(long userId) {
        OffsetDateTime expires = OffsetDateTime.now().plusDays(7);
        return generateToken(userId, expires);
    }

    public Token generateToken(long userId, OffsetDateTime expires) {
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
                .setUserId(userId)
                .setToken(uniqueToken)
                .setExpires(expires);
        tokenRepository.save(token);
        return token;
    }

    public boolean checkTokenActive(Token token) {
        return token.getExpires().isAfter(OffsetDateTime.now());
    }
}
