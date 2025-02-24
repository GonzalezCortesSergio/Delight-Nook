package com.salesianostriana.dam.delight_nook.util.files.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AbstractFileMetadata implements FileMetadata{

    protected String id;
    protected String filename;
    protected String URL;
    protected String deleteId;
    protected String deleteURL;
}
