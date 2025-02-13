package org.daylight.routenavigator.backend.repositories;

import org.daylight.routenavigator.backend.entities.Token;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends CrudRepository<Token, Long> {
    List<Token> findByTokenIn(List<UUID> tokens);
    List<Token> findAllByUserId(long userId);
    Optional<Token> findByToken(UUID token);
}
