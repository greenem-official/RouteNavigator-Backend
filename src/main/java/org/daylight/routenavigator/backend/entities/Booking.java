package org.daylight.routenavigator.backend.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "bookings")
@Accessors(chain = true)
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    @ManyToOne
    @JoinColumn(nullable = false, unique = false)
    private User user;

    @Column(nullable = false, unique = true)
    private long routeId;

    @Column(nullable = true, unique = false)
    private OffsetDateTime bookedAt;

    @Column(nullable = true, unique = false)
    private int ticketAmount = 1;
}
