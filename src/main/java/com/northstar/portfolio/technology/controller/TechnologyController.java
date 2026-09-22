package com.northstar.portfolio.technology.controller;

import com.northstar.portfolio.technology.dto.TechnologyRequest;
import com.northstar.portfolio.technology.dto.TechnologyResponse;
import com.northstar.portfolio.technology.service.TechnologyService;
import jakarta.validation.Valid;
import com.northstar.portfolio.common.response.PageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/technologies")
public class TechnologyController {

    private final TechnologyService technologyService;

    public TechnologyController(TechnologyService technologyService) {
        this.technologyService = technologyService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TechnologyResponse> create(
            @Valid @RequestBody TechnologyRequest request
    ) {
        TechnologyResponse response = technologyService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<PageResponse<TechnologyResponse>> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean active,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection
    ) {
        var technologyPage = technologyService.getAll(
                search,
                active,
                page,
                size,
                sortBy,
                sortDirection
        );

        return ResponseEntity.ok(PageResponse.from(technologyPage));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TechnologyResponse> getById(
            @PathVariable UUID id
    ) {
        TechnologyResponse response = technologyService.getById(id);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TechnologyResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody TechnologyRequest request
    ) {
        TechnologyResponse response = technologyService.update(id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id
    ) {
        technologyService.delete(id);

        return ResponseEntity.noContent().build();
    }
}