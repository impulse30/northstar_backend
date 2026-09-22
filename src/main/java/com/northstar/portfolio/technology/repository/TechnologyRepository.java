package com.northstar.portfolio.technology.repository;

import com.northstar.portfolio.technology.entity.Technology;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TechnologyRepository extends JpaRepository<Technology, UUID> {

    boolean existsByNameIgnoreCase(String name);

    Page<Technology> findByNameContainingIgnoreCase(
            String search,
            Pageable pageable
    );

    Page<Technology> findByActive(
            boolean active,
            Pageable pageable
    );

    Page<Technology> findByNameContainingIgnoreCaseAndActive(
            String search,
            boolean active,
            Pageable pageable
    );
}