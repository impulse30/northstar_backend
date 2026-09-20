package com.northstar.portfolio.user.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Setter
    @Column(nullable = false, unique = true, length = 50)
    private String name;

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }

}
