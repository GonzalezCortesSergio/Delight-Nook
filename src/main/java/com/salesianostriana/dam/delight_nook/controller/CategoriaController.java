package com.salesianostriana.dam.delight_nook.controller;

import com.salesianostriana.dam.delight_nook.dto.categoria.CreateCategoriaHijaDto;
import com.salesianostriana.dam.delight_nook.dto.categoria.GetCategoriaDto;
import com.salesianostriana.dam.delight_nook.model.Categoria;
import com.salesianostriana.dam.delight_nook.security.validation.annotation.UniqueCategoryName;
import com.salesianostriana.dam.delight_nook.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categoria")
@RequiredArgsConstructor
@Tag(name = "Categoría",
description = "Controlador para gestionar categorías")
@Validated
public class CategoriaController {

    private final CategoriaService categoriaService;


    @Operation(summary = "Se crea una categoría nueva")
    @Parameter(in = ParameterIn.HEADER, description = "Authorization token",
            name = "JWT-Auth-Token", content = @Content(schema = @Schema(type = "string")),
            example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhYTljMGY2OS00ZTRkLTQ1YjctOWFkMC01ZjU0MmI0YmZiMGUiLCJpYXQiOjE3Mzk5Njk5NTgsImV4cCI6MTczOTk3MDAxOH0.-fIz2zXh-aGZepekV2MZ5mxQMR2pJRrel1-c-XDIdmk")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Se ha creado la categoría correctamente",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = GetCategoriaDto.class),
                                            examples = {
                                                    @ExampleObject(
                                                            value = """
                                                                        {
                                                                            "id": 1,
                                                                            "nombre": "Peluches"
                                                                        }
                                                                    """
                                                    )
                                            }
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Token inválido",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ProblemDetail.class),
                                            examples = {
                                                    @ExampleObject(
                                                            value = """
                                                                        {
                                                                            "type": "about:blank",
                                                                            "title": "Invalid token",
                                                                            "status": 401,
                                                                            "detail": "JWT expired 523259 milliseconds ago at 2025-02-22T14:35:14.000Z. Current time: 2025-02-22T14:43:57.259Z. Allowed clock skew: 0 milliseconds.",
                                                                            "instance": "/api/categoria/admin/crear/Peluches"
                                                                        }
                                                                    """
                                                    )
                                            }
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "La categoría ya existe",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ProblemDetail.class),
                                            examples = {
                                                    @ExampleObject(
                                                            value = """
                                                                        {
                                                                            "type": "about:blank",
                                                                            "title": "Bad Request",
                                                                            "status": 400,
                                                                            "detail": "crear.nombre: La categoría con ese nombre ya existe",
                                                                            "instance": "/api/categoria/admin/crear/Peluches",
                                                                            "invalid-params": [
                                                                                {
                                                                                    "object": "CategoriaController",
                                                                                    "message": "La categoría con ese nombre ya existe",
                                                                                    "field": "nombre",
                                                                                    "rejectedValue": "Peluches"
                                                                                }
                                                                            ]
                                                                        }
                                                                    """
                                                    )
                                            }
                                    )
                            }
                    )
            }
    )
    @PostMapping("/admin/crear/{nombre}")
    public ResponseEntity<GetCategoriaDto> crear(
            @Parameter(in = ParameterIn.PATH,
            description = "Nombre de la nueva categoría",
            schema = @Schema(type = "string"),
            example= "Peluches")
            @PathVariable @UniqueCategoryName String nombre) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        GetCategoriaDto.of(categoriaService.create(nombre))
                );
    }

    @Operation(summary = "Se crea una categoría hija")
    @Parameter(in = ParameterIn.HEADER, description = "Authorization token",
            name = "JWT-Auth-Token", content = @Content(schema = @Schema(type = "string")),
            example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhYTljMGY2OS00ZTRkLTQ1YjctOWFkMC01ZjU0MmI0YmZiMGUiLCJpYXQiOjE3Mzk5Njk5NTgsImV4cCI6MTczOTk3MDAxOH0.-fIz2zXh-aGZepekV2MZ5mxQMR2pJRrel1-c-XDIdmk")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Se crea la categoría correctamente",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = GetCategoriaDto.class),
                                            examples = {
                                                    @ExampleObject(
                                                            value = """
                                                                        {
                                                                            "id": 2,
                                                                            "nombre": "Vaqueros",
                                                                            "categoriaPadre": "Pantalones"
                                                                        }
                                                                    """
                                                    )
                                            }
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No se ha encontrado la posible categoría padre",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ProblemDetail.class),
                                            examples = {
                                                    @ExampleObject(
                                                            value = """
                                                                        {
                                                                            "type": "about:blank",
                                                                            "title": "Entidad no encontrada",
                                                                            "status": 404,
                                                                            "detail": "No se ha encontrado la categoría con ID: 0",
                                                                            "instance": "/api/categoria/admin/crearHija"
                                                                        }
                                                                    """
                                                    )
                                            }
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "La categoría ya existe",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ProblemDetail.class),
                                            examples = {
                                                    @ExampleObject(
                                                            value = """
                                                                        {
                                                                            "type": "about:blank",
                                                                            "title": "Bad Request",
                                                                            "status": 400,
                                                                            "detail": "Error de validación",
                                                                            "instance": "/api/categoria/admin/crearHija",
                                                                            "invalid-params": [
                                                                                {
                                                                                    "object": "createCategoriaHijaDto",
                                                                                    "message": "La categoría con ese nombre ya existe",
                                                                                    "field": "categoriaNombre",
                                                                                    "rejectedValue": "Vaqueros"
                                                                                }
                                                                            ]
                                                                        }
                                                                    """
                                                    )
                                            }
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Token no válido",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ProblemDetail.class),
                                            examples = {
                                                    @ExampleObject(
                                                            value = """
                                                                        {
                                                                            "type": "about:blank",
                                                                            "title": "Invalid token",
                                                                            "status": 401,
                                                                            "detail": "JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.",
                                                                            "instance": "/api/categoria/admin/crearHija"
                                                                        }
                                                                    """
                                                    )
                                            }
                                    )
                            }
                    )
            }
    )
    @PostMapping("/admin/crearHija")
    public ResponseEntity<GetCategoriaDto> crearCategoriaHija(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "ID de la categoría padre y nombre de la nueva categoría",
                    required = true,
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CreateCategoriaHijaDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                                {
                                                                    "categoriaMadreId": 1,
                                                                    "categoriaNombre": "Vaqueros"
                                                                }
                                                            """
                                            )
                                    }
                            )
                    }
            )
            @RequestBody @Validated CreateCategoriaHijaDto categoriaHijaDto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        GetCategoriaDto.of(categoriaService.createCategoriaHija(categoriaHijaDto))
                );
    }

    @Operation(summary = "Se borra una categoría")
    @Parameter(in = ParameterIn.HEADER, description = "Authorization token",
            name = "JWT-Auth-Token", content = @Content(schema = @Schema(type = "string")),
            example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhYTljMGY2OS00ZTRkLTQ1YjctOWFkMC01ZjU0MmI0YmZiMGUiLCJpYXQiOjE3Mzk5Njk5NTgsImV4cCI6MTczOTk3MDAxOH0.-fIz2zXh-aGZepekV2MZ5mxQMR2pJRrel1-c-XDIdmk")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Se ha borrado la categoría correctamente",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Token no válido",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ProblemDetail.class),
                                            examples = {
                                                    @ExampleObject(
                                                            value = """
                                                                        {
                                                                            "type": "about:blank",
                                                                            "title": "Invalid token",
                                                                            "status": 401,
                                                                            "detail": "JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.",
                                                                            "instance": "/api/categoria/admin/borrar/1"
                                                                        }
                                                                    """
                                                    )
                                            }
                                    )
                            }
                    )
            }
    )
    @DeleteMapping("/admin/borrar/{id}")
    public ResponseEntity<?> deleteById(
            @Parameter(in = ParameterIn.PATH,
            description = "ID de la categoría a borrar",
            schema = @Schema(type = "long"),
            example = "1")
            @PathVariable Long id) {

        categoriaService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Se muestran todas las categorías base")
    @Parameter(in = ParameterIn.HEADER, description = "Authorization token",
            name = "JWT-Auth-Token", content = @Content(schema = @Schema(type = "string")),
            example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhYTljMGY2OS00ZTRkLTQ1YjctOWFkMC01ZjU0MmI0YmZiMGUiLCJpYXQiOjE3Mzk5Njk5NTgsImV4cCI6MTczOTk3MDAxOH0.-fIz2zXh-aGZepekV2MZ5mxQMR2pJRrel1-c-XDIdmk")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Se han encontrado categorías",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = Page.class),
                                            examples = {
                                                    @ExampleObject(
                                                            value = """
                                                                        {
                                                                            "content": [
                                                                                {
                                                                                    "id": 1,
                                                                                    "nombre": "Pantalones"
                                                                                },
                                                                                {
                                                                                    "id": 3,
                                                                                    "nombre": "Camisetas"
                                                                                }
                                                                            ],
                                                                            "pageable": {
                                                                                "pageNumber": 0,
                                                                                "pageSize": 10,
                                                                                "sort": {
                                                                                    "empty": true,
                                                                                    "sorted": false,
                                                                                    "unsorted": true
                                                                                },
                                                                                "offset": 0,
                                                                                "paged": true,
                                                                                "unpaged": false
                                                                            },
                                                                            "last": true,
                                                                            "totalElements": 2,
                                                                            "totalPages": 1,
                                                                            "size": 10,
                                                                            "number": 0,
                                                                            "sort": {
                                                                                "empty": true,
                                                                                "sorted": false,
                                                                                "unsorted": true
                                                                            },
                                                                            "first": true,
                                                                            "numberOfElements": 2,
                                                                            "empty": false
                                                                        }
                                                                    """
                                                    )
                                            }
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No se han encontrado categorías",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ProblemDetail.class),
                                            examples = {
                                                    @ExampleObject(
                                                            value = """
                                                                        {
                                                                            "type": "about:blank",
                                                                            "title": "Entidad no encontrada",
                                                                            "status": 404,
                                                                            "detail": "No se han encontrado categorías",
                                                                            "instance": "/api/categoria"
                                                                        }
                                                                    """
                                                    )
                                            }
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Token no válido",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ProblemDetail.class),
                                            examples = {
                                                    @ExampleObject(
                                                            value = """
                                                                        {
                                                                            "type": "about:blank",
                                                                            "title": "Invalid token",
                                                                            "status": 401,
                                                                            "detail": "JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.",
                                                                            "instance": "/api/categoria"
                                                                        }
                                                                    """
                                                    )
                                            }
                                    )
                            }
                    )
            }
    )
    @GetMapping
    public Page<GetCategoriaDto> findAllBase(@PageableDefault Pageable pageable) {

        return categoriaService.findAllBase(pageable);
    }
}
