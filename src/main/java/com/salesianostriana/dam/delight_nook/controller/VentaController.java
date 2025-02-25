package com.salesianostriana.dam.delight_nook.controller;

import com.salesianostriana.dam.delight_nook.dto.producto.ProductoCantidadDto;
import com.salesianostriana.dam.delight_nook.dto.venta.GetVentaDetailsDto;
import com.salesianostriana.dam.delight_nook.model.Venta;
import com.salesianostriana.dam.delight_nook.service.VentaService;
import com.salesianostriana.dam.delight_nook.user.model.Cajero;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/venta")
@RequiredArgsConstructor
public class VentaController {

    private final VentaService ventaService;

    @Operation(summary = "Se agrega un producto a una linea de venta y la linea de venta se agrega a una venta")
    @Parameter(in = ParameterIn.HEADER, description = "Authorization token",
            name = "JWT-Auth-Token", content = @Content(schema = @Schema(type = "string")),
            example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhYTljMGY2OS00ZTRkLTQ1YjctOWFkMC01ZjU0MmI0YmZiMGUiLCJpYXQiOjE3Mzk5Njk5NTgsImV4cCI6MTczOTk3MDAxOH0.-fIz2zXh-aGZepekV2MZ5mxQMR2pJRrel1-c-XDIdmk")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Se agrega la linea de venta correctamente",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = GetVentaDetailsDto.class),
                                            examples = {
                                                    @ExampleObject(
                                                            value = """
                                                                        {
                                                                            "id": "e9d1827c-92f9-4e0f-95a7-e6241c2000ed",
                                                                            "nombreCajero": "Manolo López Guzmán",
                                                                            "caja": {
                                                                                "id": 1,
                                                                                "nombre": "Caja 1",
                                                                                "dineroCaja": 158.75
                                                                            },
                                                                            "lineasVenta": [
                                                                                {
                                                                                    "id": "a7b5164f-6c9d-4799-81dc-e850e49cfac9",
                                                                                    "producto": {
                                                                                        "id": 1,
                                                                                        "nombre": "Pantalonichi waperrimo",
                                                                                        "categoria": "Pantalones",
                                                                                        "precioUnidad": 12.23
                                                                                    },
                                                                                    "cantidad": 3,
                                                                                    "subTotal": 36.69
                                                                                },
                                                                                {
                                                                                    "id": "6e84c3f6-9405-471c-837f-c78632a482ba",
                                                                                    "producto": {
                                                                                        "id": 2,
                                                                                        "nombre": "Pantalonichi waperrimo",
                                                                                        "categoria": "Pantalones",
                                                                                        "precioUnidad": 12.23
                                                                                    },
                                                                                    "cantidad": 3,
                                                                                    "subTotal": 36.69
                                                                                }
                                                                            ],
                                                                            "precioFinal": 73.38
                                                                        }
                                                                    """
                                                    )
                                            }
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No se ha iniciado sesión en una caja",
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
                                                                            "detail": "No has iniciado sesión en una caja para realizar esta operación",
                                                                            "instance": "/api/venta/addProducto"
                                                                        }
                                                                    """
                                                    )
                                            }
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Producto no encontrado",
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
                                                                           "detail": "El producto no se encuentra en stock",
                                                                           "instance": "/api/venta/addProducto"
                                                                       }
                                                                    """
                                                    )
                                            }
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "La cantidad es mayor a la del stock",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ProblemDetail.class),
                                            examples = {
                                                    @ExampleObject(
                                                            value = """
                                                                        {
                                                                            "type": "about:blank",
                                                                            "title": "Solicitud incorrecta",
                                                                            "status": 400,
                                                                            "detail": "No puedes vender una cantidad mayor a la que se encuentra en stock",
                                                                            "instance": "/api/venta/addProducto"
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
                                                                            "instance": "/api/venta/addProducto"
                                                                        }
                                                                    """
                                                    )
                                            }
                                    )
                            }
                    )
            }
    )
    @PostMapping("/addProducto")
    public ResponseEntity<GetVentaDetailsDto> createVenta(@AuthenticationPrincipal Cajero cajero,
                                                          @RequestBody @Validated ProductoCantidadDto productoCantidadDto) {

        Venta venta = ventaService.create(cajero, productoCantidadDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        GetVentaDetailsDto.of(venta)
                );
    }
}
