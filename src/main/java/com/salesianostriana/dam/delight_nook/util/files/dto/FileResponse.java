package com.salesianostriana.dam.delight_nook.util.files.dto;

import lombok.Builder;

@Builder
public record FileResponse(
        String id,
        String name,
        String uri,
        String type,
        long size
) {
}
