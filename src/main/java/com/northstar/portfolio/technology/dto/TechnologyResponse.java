package com.northstar.portfolio.technology.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class TechnologyResponse {

    private UUID id;

    private String name;

    private String description;

    private String iconUrl;

    private boolean active;

    private Instant createdAt;

    private Instant updatedAt;
}