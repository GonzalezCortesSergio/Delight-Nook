package com.salesianostriana.dam.delight_nook.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

public record CreateCajaDto(
        @NotBlank
        String  nombre,
        @DecimalMin("0.01")
        double dineroCaja
) {
}
