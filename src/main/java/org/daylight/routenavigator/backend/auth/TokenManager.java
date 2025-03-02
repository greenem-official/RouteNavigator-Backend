package org.daylight.routenavigator.backend.auth;

import org.daylight.routenavigator.backend.components.SpringContextHolder;
import org.daylight.routenavigator.backend.entities.Token;
import org.daylight.routenavigator.backend.entities.User;
import org.daylight.routenavigator.backend.model.outcoming.ErrorResponse;
import org.daylight.routenavigator.backend.model.outcoming.MessageResponse;
import org.daylight.routenavigator.backend.model.outcoming.TokenResponse;
import org.daylight.routenavigator.backend.services.entitysaervices.TokenService;
import org.daylight.routenavigator.backend.services.entitysaervices.UserService;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TokenManager {
    public static Pair<Token, ResponseEntity<?>> findToken(String token) {
        // Parsing the UUID object
        UUID uuid;
        try {
            uuid = UUID.fromString(token);
        } catch (IllegalArgumentException e) {
            return new Pair<>(null, new ResponseEntity<>(new ErrorResponse("Invalid token"), HttpStatus.BAD_REQUEST));
        }

        return findToken(uuid);
    }

    public static Pair<Token, ResponseEntity<?>> findToken(UUID tokenText) {
        TokenService tokenService = SpringContextHolder.getBean(TokenService.class);

        // Checking if token is real
        Optional<Token> token = tokenService.findByToken(tokenText);
        if (token.isEmpty()) {
            return new Pair<>(null, new ResponseEntity<>(new ErrorResponse("Token not found"), HttpStatus.OK));
        }

        // Checking if token is not expired
        boolean active = tokenService.checkTokenActive(token.get());
        if (!active) {
            return new Pair<>(null, new ResponseEntity<>(new ErrorResponse("Token expired"), HttpStatus.OK));
        }

        // Sending the result
        return new Pair<>(token.get(), null);
    }
}
