package org.daylight.routenavigator.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * A token database entity, including the assigned user and an expiration date
 */
@Data
@Entity
@Table(name = "tokens")
@Accessors(chain = true)
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    @Setter(AccessLevel.NONE)
    private long id;

    @ManyToOne
    @JoinColumn(nullable = false, unique = false)
    private User user;

    @Column(nullable = false, unique = true)
    private UUID token;

    @Column(nullable = false, unique = false)
    private OffsetDateTime expires;
}
