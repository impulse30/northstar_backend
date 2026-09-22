package com.northstar.portfolio.technology.service;

import com.northstar.portfolio.technology.dto.TechnologyRequest;
import com.northstar.portfolio.technology.dto.TechnologyResponse;
import com.northstar.portfolio.technology.entity.Technology;
import com.northstar.portfolio.technology.repository.TechnologyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;
import java.util.UUID;

@Service
public class TechnologyService {

    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int MAX_PAGE_SIZE = 100;

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "name",
            "active",
            "createdAt",
            "updatedAt"
    );

    private final TechnologyRepository technologyRepository;

    public TechnologyService(TechnologyRepository technologyRepository) {
        this.technologyRepository = technologyRepository;
    }

    public TechnologyResponse create(TechnologyRequest request) {
        String normalizedName = request.getName().trim();

        if (technologyRepository.existsByNameIgnoreCase(normalizedName)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Une technologie avec ce nom existe déjà"
            );
        }

        Technology technology = new Technology();
        applyRequestToEntity(request, technology);

        Technology savedTechnology = technologyRepository.save(technology);

        return toResponse(savedTechnology);
    }

    public TechnologyResponse getById(UUID id) {
        Technology technology = findEntityById(id);
        return toResponse(technology);
    }

    public Page<TechnologyResponse> getAll(
            String search,
            Boolean active,
            Integer page,
            Integer size,
            String sortBy,
            String sortDirection
    ) {
        Pageable pageable = createPageable(page, size, sortBy, sortDirection);

        String normalizedSearch = normalizeSearch(search);

        Page<Technology> technologies;

        if (normalizedSearch != null && active != null) {
            technologies = technologyRepository
                    .findByNameContainingIgnoreCaseAndActive(
                            normalizedSearch,
                            active,
                            pageable
                    );
        } else if (normalizedSearch != null) {
            technologies = technologyRepository
                    .findByNameContainingIgnoreCase(normalizedSearch, pageable);
        } else if (active != null) {
            technologies = technologyRepository.findByActive(active, pageable);
        } else {
            technologies = technologyRepository.findAll(pageable);
        }

        return technologies.map(this::toResponse);
    }

    public TechnologyResponse update(UUID id, TechnologyRequest request) {
        Technology technology = findEntityById(id);

        String normalizedName = request.getName().trim();

        boolean changedName = !technology.getName().equalsIgnoreCase(normalizedName);

        if (changedName && technologyRepository.existsByNameIgnoreCase(normalizedName)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Une technologie avec ce nom existe déjà"
            );
        }

        applyRequestToEntity(request, technology);

        Technology updatedTechnology = technologyRepository.save(technology);

        return toResponse(updatedTechnology);
    }

    public void delete(UUID id) {
        Technology technology = findEntityById(id);
        technologyRepository.delete(technology);
    }

    private Technology findEntityById(UUID id) {
        return technologyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Technologie introuvable"
                ));
    }

    private void applyRequestToEntity(
            TechnologyRequest request,
            Technology technology
    ) {
        technology.setName(request.getName().trim());
        technology.setDescription(normalizeNullableText(request.getDescription()));
        technology.setIconUrl(normalizeNullableText(request.getIconUrl()));
        technology.setActive(Boolean.TRUE.equals(request.getActive()));
    }

    private Pageable createPageable(
            Integer page,
            Integer size,
            String sortBy,
            String sortDirection
    ) {
        int safePage = page == null ? 0 : Math.max(page, 0);

        int requestedSize = size == null ? DEFAULT_PAGE_SIZE : size;
        int safeSize = Math.min(Math.max(requestedSize, 1), MAX_PAGE_SIZE);

        String safeSortBy = ALLOWED_SORT_FIELDS.contains(sortBy)
                ? sortBy
                : "name";

        Sort.Direction direction = "desc".equalsIgnoreCase(sortDirection)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Sort sort = Sort.by(direction, safeSortBy)
                .and(Sort.by(Sort.Direction.ASC, "id"));

        return PageRequest.of(safePage, safeSize, sort);
    }

    private String normalizeSearch(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }

    private String normalizeNullableText(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }

    private TechnologyResponse toResponse(Technology technology) {
        return new TechnologyResponse(
                technology.getId(),
                technology.getName(),
                technology.getDescription(),
                technology.getIconUrl(),
                technology.isActive(),
                technology.getCreatedAt(),
                technology.getUpdatedAt()
        );
    }
}