package org.daylight.marketplace.backend.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Accessors(chain = true)
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = false)
    private long userId;

    @Column(nullable = false, unique = true)
    private UUID token;

    @Column(nullable = false, unique = false)
    private OffsetDateTime expires;
}
