package com.salesianostriana.dam.delight_nook.controller;

import com.salesianostriana.dam.delight_nook.dto.producto.CreateProductoDto;
import com.salesianostriana.dam.delight_nook.dto.producto.GetProductoDetailsDto;
import com.salesianostriana.dam.delight_nook.service.ProductoService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequestMapping("/api/producto")
@RequiredArgsConstructor
@Tag(name = "Producto",
description = "Controlador para gestionar productos")
public class ProductoController {

    private final ProductoService productoService;


    @Operation(summary = "Se crea un producto nuevo")
    @Parameter(in = ParameterIn.HEADER, description = "Authorization token",
            name = "JWT-Auth-Token", content = @Content(schema = @Schema(type = "string")),
            example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhYTljMGY2OS00ZTRkLTQ1YjctOWFkMC01ZjU0MmI0YmZiMGUiLCJpYXQiOjE3Mzk5Njk5NTgsImV4cCI6MTczOTk3MDAxOH0.-fIz2zXh-aGZepekV2MZ5mxQMR2pJRrel1-c-XDIdmk")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Se ha creado el producto correctamente",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = GetProductoDetailsDto.class),
                                            examples = {
                                                    @ExampleObject(
                                                            value = """
                                                                        {
                                                                            "nombre": "Pantalonichi waperrimo",
                                                                            "precioUnidad": 12.24,
                                                                            "descripcion": "Un pantalón to wapo pa ti pa tos",
                                                                            "imagen": "pantalon.png",
                                                                            "categoria": "Pantalones",
                                                                            "proveedor": "Niños de Bangliadesh"
                                                                        }
                                                                    """
                                                    )
                                            }
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Alguno de los datos introducidos no es válido",
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
                                                                            "instance": "/api/producto/admin/create",
                                                                            "invalid-params": [
                                                                                {
                                                                                    "object": "createProductoDto",
                                                                                    "message": "debe ser mayor que o igual a 0.01",
                                                                                    "field": "precioUnidad",
                                                                                    "rejectedValue": -1.0
                                                                                },
                                                                                {
                                                                                    "object": "createProductoDto",
                                                                                    "message": "no debe estar vacío",
                                                                                    "field": "proveedor",
                                                                                    "rejectedValue": ""
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
                                                                            "detail": "Malformed protected header JSON: Unable to deserialize: Illegal unquoted character ((CTRL-CHAR, code 8)): has to be escaped using backslash to be included in name\\n at [Source: REDACTED (`StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION` disabled); line: 1, column: 11]",
                                                                            "instance": "/api/producto/admin/create"
                                                                        }
                                                                    """
                                                    )
                                            }
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No se ha encontrado la categoría",
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
                                                                            "detail": "No se ha encontrado la categoría con ID: 2",
                                                                            "instance": "/api/producto/admin/create"
                                                                        }
                                                                    """
                                                    )
                                            }
                                    )
                            }
                    )
            }
    )
    @PostMapping("/admin/create")
    public ResponseEntity<GetProductoDetailsDto> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del producto a crear",
                    required = true,
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CreateProductoDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                                {
                                                                    "nombre": "Pantalonichi waperrimo",
                                                                    "precioUnidad": 1,
                                                                    "descripcion": "Un pantalón to wapo pa ti pa tos",
                                                                    "imagen": "pantalon.png",
                                                                    "categoriaId": 1,
                                                                    "proveedor": "Niños de Bangliadesh"
                                                                }
                                                            """
                                            )
                                    }
                            )
                    }
            )
            @RequestBody @Validated CreateProductoDto productoDto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        GetProductoDetailsDto.of(productoService.create(productoDto))
                );
    }
}
