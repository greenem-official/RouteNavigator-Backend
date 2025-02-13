package org.daylight.marketplace.backend.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@Entity
@Accessors(chain = true)
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = false)
    private long userId;

    @Column(nullable = false, unique = true)
    private long routeId;

    @Column(nullable = true, unique = false)
    private OffsetDateTime bookedAt;
}
