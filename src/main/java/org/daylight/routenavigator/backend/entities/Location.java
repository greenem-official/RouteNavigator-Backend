package org.daylight.routenavigator.backend.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

@Data
@Entity
@Accessors(chain = true)
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false, unique = false)
    private String displayName;

    public Location setCode(String code) {
        this.code = code.toLowerCase();
        return this;
    }

    @Override
    public String toString() {
        return "Location [id=" + id + ", code=" + code + "]";
    }
}
