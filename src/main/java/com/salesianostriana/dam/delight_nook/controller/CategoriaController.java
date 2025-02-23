package com.salesianostriana.dam.delight_nook.controller;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
