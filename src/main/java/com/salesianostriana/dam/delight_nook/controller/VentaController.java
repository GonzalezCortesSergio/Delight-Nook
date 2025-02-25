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
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    @Operation(summary = "Se borra una linea de venta")
    @Parameter(in = ParameterIn.HEADER, description = "Authorization token",
            name = "JWT-Auth-Token", content = @Content(schema = @Schema(type = "string")),
            example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhYTljMGY2OS00ZTRkLTQ1YjctOWFkMC01ZjU0MmI0YmZiMGUiLCJpYXQiOjE3Mzk5Njk5NTgsImV4cCI6MTczOTk3MDAxOH0.-fIz2zXh-aGZepekV2MZ5mxQMR2pJRrel1-c-XDIdmk")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Se ha borrado la línea de venta y hay más líneas de venta",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = GetVentaDetailsDto.class),
                                            examples = {
                                                    @ExampleObject(
                                                            value = """
                                                                        {
                                                                            "id": "b5c639b0-5f4b-41c2-a2a1-f985e23c917c",
                                                                            "nombreCajero": "Manolo López Guzmán",
                                                                            "caja": {
                                                                                "id": 1,
                                                                                "nombre": "Caja 1",
                                                                                "dineroCaja": 158.75
                                                                            },
                                                                            "lineasVenta": [
                                                                                {
                                                                                    "id": "90769703-1652-4aef-999c-04d437bb8610",
                                                                                    "producto": {
                                                                                        "id": 1,
                                                                                        "nombre": "Pantalonichi waperrimo",
                                                                                        "categoria": "Pantalones",
                                                                                        "precioUnidad": 12.23
                                                                                    },
                                                                                    "cantidad": 3,
                                                                                    "subTotal": 36.69
                                                                                }
                                                                            ],
                                                                            "precioFinal": 36.69
                                                                        }
                                                                    """
                                                    )
                                            }
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "204",
                            description = "Se ha borrado la línea de venta y se ha quedado vacía. Borrándose",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No se ha encontrado la venta",
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
                                                                            "detail": "No se ha encontrado la venta",
                                                                            "instance": "/api/venta/removeProducto/90769703-1652-4aef-999c-04d437bb8610"
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
                                                                            "instance": "/api/venta/removeProducto/90769703-1652-4aef-999c-04d437bb8610"
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
                                                                            "detail": "Malformed protected header JSON: Unable to deserialize: Unexpected character ('-' (code 45)): was expecting double-quote to start field name\\n at [Source: REDACTED (`StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION` disabled); line: 1, column: 2]",
                                                                            "instance": "/api/venta/removeProducto/90769703-1652-4aef-999c-04d437bb8610"
                                                                        }
                                                                    """
                                                    )
                                            }
                                    )
                            }
                    )
            }
    )
    @PutMapping("/removeProducto/{idLineaVenta}")
    public ResponseEntity<?> removeLineaVenta(@AuthenticationPrincipal Cajero cajero,
                                              @Parameter(in = ParameterIn.PATH,
                                              description = "ID de la línea de venta",
                                              schema = @Schema(type = "uuid"),
                                              example = "90769703-1652-4aef-999c-04d437bb8610")
                                              @PathVariable UUID idLineaVenta) {

        Venta venta = ventaService.removeLineaVenta(cajero, idLineaVenta);

        if(venta == null)
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(GetVentaDetailsDto.of(venta));
    }

    @Operation(summary = "Se finaliza la venta")
    @Parameter(in = ParameterIn.HEADER, description = "Authorization token",
            name = "JWT-Auth-Token", content = @Content(schema = @Schema(type = "string")),
            example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhYTljMGY2OS00ZTRkLTQ1YjctOWFkMC01ZjU0MmI0YmZiMGUiLCJpYXQiOjE3Mzk5Njk5NTgsImV4cCI6MTczOTk3MDAxOH0.-fIz2zXh-aGZepekV2MZ5mxQMR2pJRrel1-c-XDIdmk")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Se ha finalizado la venta correctamente",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = GetVentaDetailsDto.class),
                                            examples = {
                                                    @ExampleObject(
                                                            value = """
                                                                        {
                                                                            "id": "0e96b4fb-c223-49b6-9a42-6fba19ed3e72",
                                                                            "nombreCajero": "Manolo López Guzmán",
                                                                            "caja": {
                                                                                "id": 1,
                                                                                "nombre": "Caja 1",
                                                                                "dineroCaja": 195.44
                                                                            },
                                                                            "lineasVenta": [
                                                                                {
                                                                                    "id": "2d753d2d-4853-496d-80d8-af28c55c0607",
                                                                                    "producto": {
                                                                                        "id": 1,
                                                                                        "nombre": "Pantalonichi waperrimo",
                                                                                        "categoria": "Pantalones",
                                                                                        "precioUnidad": 12.23
                                                                                    },
                                                                                    "cantidad": 3,
                                                                                    "subTotal": 36.69
                                                                                }
                                                                            ],
                                                                            "precioFinal": 36.69,
                                                                            "fechaVenta": "25-02-2025 14:20:21"
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
                                                                            "instance": "/api/venta/finalizar"
                                                                        }
                                                                    """
                                                    )
                                            }
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No se ha encontrado la venta",
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
                                                                            "detail": "No se ha encontrado la venta",
                                                                            "instance": "/api/venta/finalizar"
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
                                                                            "detail": "Malformed protected header JSON: Unable to deserialize: Unexpected character ('-' (code 45)): was expecting double-quote to start field name\\n at [Source: REDACTED (`StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION` disabled); line: 1, column: 2]",
                                                                            "instance": "/api/venta/finalizar"
                                                                        }
                                                                    """
                                                    )
                                            }
                                    )
                            }
                    )
            }
    )
    @PutMapping("/finalizar")
    public GetVentaDetailsDto finalizarVenta(@AuthenticationPrincipal Cajero cajero) {

        return GetVentaDetailsDto.of(ventaService.finalizarVenta(cajero));
    }
}
