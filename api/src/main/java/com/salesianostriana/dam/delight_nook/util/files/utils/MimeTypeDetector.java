package com.salesianostriana.dam.delight_nook.util.files.utils;


import org.springframework.core.io.Resource;

public interface MimeTypeDetector {

    String getMimeType(Resource resource);
}
