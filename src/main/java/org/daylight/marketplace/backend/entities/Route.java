package org.daylight.marketplace.backend.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

@Data
@Entity
@Accessors(chain = true)
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(nullable = false, unique = false)
    private TransportType transportType;

    @ManyToOne
    @JoinColumn(nullable = false, unique = false)
    private Location departureLocation;

    @ManyToOne
    @JoinColumn(nullable = false, unique = false)
    private Location arrivalLocation;

    @Column(nullable = false, unique = false)
    private OffsetDateTime departureTime;

    @Column(nullable = false, unique = false)
    private OffsetDateTime arrivalTime;
}
