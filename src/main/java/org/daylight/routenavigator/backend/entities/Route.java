package org.daylight.routenavigator.backend.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Duration;
import java.time.OffsetDateTime;

/**
 * A route database entity
 */
@Data
@Entity
@Table(indexes = {
        @Index(name = "idx_route_departure_location", columnList = "departure_location_id"),
        @Index(name = "idx_route_arrival_location", columnList = "arrival_location_id"),
        @Index(name = "idx_route_transport_type", columnList = "transport_type_id"),
})
@Accessors(chain = true)
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
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

    @Column(nullable = false, unique = false)
    private int price;

    public Duration getDuration() {
        return Duration.between(departureTime, arrivalTime);
    }

    @Override
    public String toString() {
        return "Route [id=" + id + ", departureLocation=" + departureLocation + ", arrivalLocation=" + arrivalLocation + "]";
    }
}
