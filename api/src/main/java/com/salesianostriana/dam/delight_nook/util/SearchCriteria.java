package com.salesianostriana.dam.delight_nook.util;

public record SearchCriteria(
        String key,
        String operation,
        Object value
) {
}
