package com.northstar.portfolio.technology.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TechnologyRequest {

    @NotBlank(message = "Le nom de la technologie est obligatoire")
    @Size(max = 100, message = "Le nom ne doit pas dépasser 100 caractères")
    private String name;

    @Size(max = 500, message = "La description ne doit pas dépasser 500 caractères")
    private String description;

    @Size(max = 500, message = "L'URL de l'icône ne doit pas dépasser 500 caractères")
    private String iconUrl;

    private Boolean active = true;
}