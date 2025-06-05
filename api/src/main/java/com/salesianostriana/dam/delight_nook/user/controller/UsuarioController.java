package com.salesianostriana.dam.delight_nook.user.controller;

import com.salesianostriana.dam.delight_nook.security.jwt.access.JwtService;
import com.salesianostriana.dam.delight_nook.security.jwt.refresh.RefreshToken;
import com.salesianostriana.dam.delight_nook.security.jwt.refresh.RefreshTokenRequest;
import com.salesianostriana.dam.delight_nook.security.jwt.refresh.RefreshTokenService;
import com.salesianostriana.dam.delight_nook.user.dto.CreateUsuarioDto;
import com.salesianostriana.dam.delight_nook.user.dto.LoginRequest;
import com.salesianostriana.dam.delight_nook.user.dto.UsuarioResponseDto;
import com.salesianostriana.dam.delight_nook.user.dto.ValidateUsuarioDto;
import com.salesianostriana.dam.delight_nook.user.model.Usuario;
import com.salesianostriana.dam.delight_nook.user.service.UsuarioService;
import com.salesianostriana.dam.delight_nook.util.files.service.StorageService;
import com.salesianostriana.dam.delight_nook.util.files.utils.MimeTypeDetector;
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
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuario")
@RequiredArgsConstructor
@Tag(name = "Usuario",
description = "Controlador para gestionar a los usuarios, además de su seguridad")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final StorageService storageService;
    private final MimeTypeDetector mimeTypeDetector;


    @Operation(summary = "Se registra un usuario con rol de admin")
    @Parameter(in = ParameterIn.HEADER, description = "Authorization token",
    name = "JWT-Auth-Token", content = @Content(schema = @Schema(type = "string")),
    example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhYTljMGY2OS00ZTRkLTQ1YjctOWFkMC01ZjU0MmI0YmZiMGUiLCJpYXQiOjE3Mzk5Njk5NTgsImV4cCI6MTczOTk3MDAxOH0.-fIz2zXh-aGZepekV2MZ5mxQMR2pJRrel1-c-XDIdmk")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Se ha creado el usuario correctamente",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = UsuarioResponseDto.class),
                                            examples = {
                                                    @ExampleObject(
                                                            value = """
                                                                        {
                                                                            "id": "08be4ce7-6e1d-4f4a-bb78-a9149949b110",
                                                                            "username": "Usuario_2",
                                                                            "nombreCompleto": "Usuario 1",
                                                                            "avatar": "avatar.png",
                                                                            "roles": [
                                                                                "ALMACENERO"
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
                            description = "El token de validación ha caducado",
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
                                                                            "detail": "JWT expired 23567 milliseconds ago at 2025-02-19T12:38:03.000Z. Current time: 2025-02-19T12:38:26.567Z. Allowed clock skew: 0 milliseconds.",
                                                                            "instance": "/api/usuario/auth/register"
                                                                        }
                                                                    """
                                                    )
                                            }
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Algún dato no es válido (campos vacíos o correo no válido)",
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
                                                                            "instance": "/api/usuario/auth/register",
                                                                            "invalid-params": [
                                                                                {
                                                                                    "object": "createUsuarioDto",
                                                                                    "message": "El nombre de usuario no puede estar vacío",
                                                                                    "field": "username",
                                                                                    "rejectedValue": ""
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
    @PostMapping("/admin/auth/register")
    public ResponseEntity<UsuarioResponseDto> register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del usuario a crear",
                    required = true,
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CreateUsuarioDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                                {
                                                                    "username": "Usuario_1",
                                                                    "nombreCompleto": "Usuario 1",
                                                                    "email": "usuario@gmail.com",
                                                                    "avatar": "avatar.png"
                                                                }
                                                            """
                                            )
                                    }
                            )
                    }
            )
            @RequestBody @Validated CreateUsuarioDto usuarioDto,
            @Parameter(in = ParameterIn.QUERY,
            description = "Distintivo del usuario a crear",
            schema = @Schema(type = "string"),
            example = "almacenero")
            @RequestParam(defaultValue = "user", required = false) String userRole) {

        Usuario usuario = usuarioService.saveUsuario(usuarioDto, userRole);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UsuarioResponseDto.of(usuario, usuarioService.getImageUrl(usuario.getAvatar())));
    }

    @Operation(summary = "El usuario inicia sesión para hacer operaciones que requieran de algún tipo de identificación")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Se ha iniciado sesión correctamente",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UsuarioResponseDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                                {
                                                                    "id": "5796d422-dd48-49e8-bc71-c613caac0052",
                                                                    "username": "admin",
                                                                    "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1Nzk2ZDQyMi1kZDQ4LTQ5ZTgtYmM3MS1jNjEzY2FhYzAwNTIiLCJpYXQiOjE3Mzk5NjkwNTgsImV4cCI6MTczOTk2OTExOH0.wegxMbt6rtBKxslg8e5BL3P669F5BobBESTs8StAHEY",
                                                                    "refreshToken": "3267f607-32d7-402b-b085-e08fe4f7e90f",
                                                                    "nombreCompleto": "admin",
                                                                    "avatar": "admin.png",
                                                                    "roles": [
                                                                        "ADMIN"
                                                                    ]
                                                                }
                                                            """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Usuario o contraseña no correctos",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ProblemDetail.class),
                                            examples = {
                                                    @ExampleObject(
                                                            value = """
                                                                        {
                                                                            "type": "about:blank",
                                                                            "title": "Unauthorized",
                                                                            "status": 401,
                                                                            "detail": "Bad credentials",
                                                                            "instance": "/api/usuario/auth/login"
                                                                        }
                                                                    """
                                                    )
                                            }
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Alguno de los campos está vacío",
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
                                                                            "instance": "/api/usuario/auth/login",
                                                                            "invalid-params": [
                                                                                {
                                                                                    "object": "loginRequest",
                                                                                    "message": "no debe estar vacío",
                                                                                    "field": "password",
                                                                                    "rejectedValue": ""
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
    @PostMapping("/auth/login")
    public ResponseEntity<UsuarioResponseDto> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Usuario y contraseña",
                    required = true,
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = LoginRequest.class),
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                                {
                                                                    "username": "admin",
                                                                    "password": "admin"
                                                                }
                                                            """
                                            )
                                    }
                            )
                    }
            )
            @RequestBody @Validated LoginRequest loginRequest) {

        Authentication auth =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.username(),
                                loginRequest.password()
                        )
                );
        SecurityContextHolder.getContext().setAuthentication(auth);

        Usuario usuario = (Usuario) auth.getPrincipal();

        String accessToken = jwtService.generateAccessToken(usuario);
        RefreshToken refreshToken = refreshTokenService.create(usuario);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UsuarioResponseDto.of(usuario, accessToken, refreshToken.getToken(), usuarioService.getImageUrl(usuario.getAvatar())));
    }

    @Operation(summary = "Validación de un usuario que se acaba de registrar")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Se ha validado el usuario correctamente",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = UsuarioResponseDto.class),
                                            examples = {
                                                    @ExampleObject(
                                                            value = """
                                                                        {
                                                                            "id": "7eedf5b4-f3cb-4759-8be0-7cab736d6ef2",
                                                                            "username": "Usuario_1",
                                                                            "nombreCompleto": "Usuario 1",
                                                                            "avatar": "avatar.png",
                                                                            "roles": [
                                                                                "ALMACENERO"
                                                                            ]
                                                                        }
                                                                    """
                                                    )
                                            }
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "El código de activación no existe o ya ha caducado",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ProblemDetail.class),
                                            examples = {
                                                    @ExampleObject(
                                                            value = """
                                                                        {
                                                                            "type": "about:blank",
                                                                            "title": "No puede activar el usuario",
                                                                            "status": 400,
                                                                            "detail": "El código de activación no existe o ha caducado",
                                                                            "instance": "/api/usuario/auth/validate"
                                                                        }
                                                                    """
                                                    )
                                            }
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Uno de los campos proporcionados no es válido",
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
                                                                            "instance": "/api/usuario/auth/validate",
                                                                            "invalid-params": [
                                                                                {
                                                                                    "object": "validateUsuarioDto",
                                                                                    "message": "no debe estar vacío",
                                                                                    "field": "verifyPassword",
                                                                                    "rejectedValue": ""
                                                                                },
                                                                                {
                                                                                    "object": "validateUsuarioDto",
                                                                                    "message": "Las contraseñas no coinciden"
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
    @PutMapping("/auth/validate")
    public UsuarioResponseDto validateAccount(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Contraseña y token del usuario",
                    required = true,
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ValidateUsuarioDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                                {
                                                                    "password": "1234",
                                                                    "verifyPassword": "1234",
                                                                    "activationToken": "946c9fef-cf9c-4e1c-b0ae-12ce0a7138f4"
                                                                }
                                                            """
                                            )
                                    }
                            )
                    }
            )
            @RequestBody @Validated ValidateUsuarioDto validateUsuarioDto) {

        Usuario usuario = usuarioService.activateAccount(validateUsuarioDto);

        return UsuarioResponseDto.of(usuario, usuarioService.getImageUrl(usuario.getAvatar()));
    }

    @Operation(summary = "Se refrescan los token de acceso y refresco del usuario")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Se han refrescado los token correctamente",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = UsuarioResponseDto.class),
                                            examples = {
                                                    @ExampleObject(
                                                            value = """
                                                                        {
                                                                            "id": "bd84c880-5d08-4a04-8e3b-0dd0ceaeea81",
                                                                            "username": "admin",
                                                                            "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiZDg0Yzg4MC01ZDA4LTRhMDQtOGUzYi0wZGQwY2VhZWVhODEiLCJpYXQiOjE3NDAwNDI0NjksImV4cCI6MTc0MDA0MjUyOX0.V0l2NwylK-SDjDmCi6fUPJCcFmzemsWMcTQPKkvrnoo",
                                                                            "refreshToken": "d770ad63-5486-422c-bcd6-41ea81e89130",
                                                                            "nombreCompleto": "admin",
                                                                            "avatar": "admin.png",
                                                                            "roles": [
                                                                                "ADMIN"
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
                            description = "Token de refresco caducado o no válido",
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
                                                                            "detail": "Token de refresco caducado. Por favor, vuelva a loguearse",
                                                                            "instance": "/api/usuario/auth/refresh/token"
                                                                        }
                                                                    """
                                                    )
                                            }
                                    )
                            }
                    )
            }
    )
    @PostMapping("/auth/refresh/token")
    public ResponseEntity<UsuarioResponseDto> refreshToken(@RequestBody RefreshTokenRequest req) {

        String token = req.refreshToken();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(refreshTokenService.refreshToken(token));
    }

    @Operation(summary = "Se muestran todos los usuarios")
    @Parameter(in = ParameterIn.HEADER, description = "Authorization token",
            name = "JWT-Auth-Token", content = @Content(schema = @Schema(type = "string")),
            example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhYTljMGY2OS00ZTRkLTQ1YjctOWFkMC01ZjU0MmI0YmZiMGUiLCJpYXQiOjE3Mzk5Njk5NTgsImV4cCI6MTczOTk3MDAxOH0.-fIz2zXh-aGZepekV2MZ5mxQMR2pJRrel1-c-XDIdmk")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Se han encontrado usuarios",
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
                                                                                    "id": "a57b573c-c9a2-4970-af0b-d90961c1bef2",
                                                                                    "username": "admin",
                                                                                    "nombreCompleto": "admin",
                                                                                    "avatar": "admin.png",
                                                                                    "roles": [
                                                                                        "ADMIN"
                                                                                    ]
                                                                                },
                                                                                {
                                                                                    "id": "e3c64584-1a13-4012-a8a2-789db46354d2",
                                                                                    "username": "Usuario_11",
                                                                                    "nombreCompleto": "Usuario 1",
                                                                                    "avatar": "avatar.png",
                                                                                    "roles": [
                                                                                        "ALMACENERO"
                                                                                    ]
                                                                                },
                                                                                {
                                                                                    "id": "7a11d9f7-1f9f-4995-8f27-803895317895",
                                                                                    "username": "Usuario_10",
                                                                                    "nombreCompleto": "Usuario 1",
                                                                                    "avatar": "avatar.png",
                                                                                    "roles": [
                                                                                        "ALMACENERO"
                                                                                    ]
                                                                                },
                                                                                {
                                                                                    "id": "19cfd021-d52f-454d-a544-0ed7848c105a",
                                                                                    "username": "Usuario_9",
                                                                                    "nombreCompleto": "Usuario 1",
                                                                                    "avatar": "avatar.png",
                                                                                    "roles": [
                                                                                        "ALMACENERO"
                                                                                    ]
                                                                                },
                                                                                {
                                                                                    "id": "91f60545-757f-41a1-b739-92b9caaa85d2",
                                                                                    "username": "Usuario_8",
                                                                                    "nombreCompleto": "Usuario 1",
                                                                                    "avatar": "avatar.png",
                                                                                    "roles": [
                                                                                        "ALMACENERO"
                                                                                    ]
                                                                                },
                                                                                {
                                                                                    "id": "0c95f8a1-7a6d-4d7e-8af3-9db38c5a9d8e",
                                                                                    "username": "Usuario_7",
                                                                                    "nombreCompleto": "Usuario 1",
                                                                                    "avatar": "avatar.png",
                                                                                    "roles": [
                                                                                        "ALMACENERO"
                                                                                    ]
                                                                                },
                                                                                {
                                                                                    "id": "0efd5305-9da0-4884-b1fb-23395e06c9c9",
                                                                                    "username": "Usuario_6",
                                                                                    "nombreCompleto": "Usuario 1",
                                                                                    "avatar": "avatar.png",
                                                                                    "roles": [
                                                                                        "ALMACENERO"
                                                                                    ]
                                                                                },
                                                                                {
                                                                                    "id": "ef52acb5-25f1-42c2-9514-29a196e1bf79",
                                                                                    "username": "Usuario_5",
                                                                                    "nombreCompleto": "Usuario 1",
                                                                                    "avatar": "avatar.png",
                                                                                    "roles": [
                                                                                        "ALMACENERO"
                                                                                    ]
                                                                                },
                                                                                {
                                                                                    "id": "ac98c778-57db-40e1-9379-4e3ed1982ae9",
                                                                                    "username": "Usuario_4",
                                                                                    "nombreCompleto": "Usuario 1",
                                                                                    "avatar": "avatar.png",
                                                                                    "roles": [
                                                                                        "ALMACENERO"
                                                                                    ]
                                                                                },
                                                                                {
                                                                                    "id": "efc5f4da-640c-4539-bd64-3dfae835f712",
                                                                                    "username": "Usuario_3",
                                                                                    "nombreCompleto": "Usuario 1",
                                                                                    "avatar": "avatar.png",
                                                                                    "roles": [
                                                                                        "ALMACENERO"
                                                                                    ]
                                                                                }
                                                                            ],
                                                                            "pageable": {
                                                                                "pageNumber": 0,
                                                                                "pageSize": 10,
                                                                                "sort": {
                                                                                    "empty": true,
                                                                                    "unsorted": true,
                                                                                    "sorted": false
                                                                                },
                                                                                "offset": 0,
                                                                                "unpaged": false,
                                                                                "paged": true
                                                                            },
                                                                            "totalPages": 2,
                                                                            "totalElements": 13,
                                                                            "last": false,
                                                                            "size": 10,
                                                                            "number": 0,
                                                                            "sort": {
                                                                                "empty": true,
                                                                                "unsorted": true,
                                                                                "sorted": false
                                                                            },
                                                                            "first": true,
                                                                            "numberOfElements": 10,
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
                            description = "No se ha encontrado ningún usuario",
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
                                                                            "detail": "No se ha encontrado ningún usuario",
                                                                            "instance": "/api/usuario/admin/listado"
                                                                        }
                                                                    """
                                                    )
                                            }
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Token de acceso caducado",
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
                                                                            "detail": "JWT expired 27740 milliseconds ago at 2025-02-20T13:09:24.000Z. Current time: 2025-02-20T13:09:51.740Z. Allowed clock skew: 0 milliseconds.",
                                                                            "instance": "/api/usuario/admin/listado"
                                                                        }
                                                                    """
                                                    )
                                            }
                                    )
                            }
                    )
            }
    )
    @GetMapping("/admin/listado")
    public Page<UsuarioResponseDto> findAll(@PageableDefault Pageable pageable, @RequestParam(defaultValue = "no", required = false) String filter) {

        return usuarioService.findAllByUserRole(pageable, filter);
    }

    @Operation(summary = "Se agrega el rol de admin a un usuario")
    @Parameter(in = ParameterIn.HEADER, description = "Authorization token",
            name = "JWT-Auth-Token", content = @Content(schema = @Schema(type = "string")),
            example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhYTljMGY2OS00ZTRkLTQ1YjctOWFkMC01ZjU0MmI0YmZiMGUiLCJpYXQiOjE3Mzk5Njk5NTgsImV4cCI6MTczOTk3MDAxOH0.-fIz2zXh-aGZepekV2MZ5mxQMR2pJRrel1-c-XDIdmk")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Se añade el rol correctamente",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = UsuarioResponseDto.class),
                                            examples = {
                                                    @ExampleObject(
                                                            value = """
                                                                        {
                                                                            "id": "c2f7ec8b-d98a-4f67-8430-f7655a323445",
                                                                            "username": "Usuario_1",
                                                                            "nombreCompleto": "Usuario 1",
                                                                            "avatar": "avatar.png",
                                                                            "roles": [
                                                                                "ADMIN",
                                                                                "ALMACENERO"
                                                                            ]
                                                                        }
                                                                    """
                                                    )
                                            }
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Usuario no encontrado",
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
                                                                            "detail": "No se ha encontrado el usuario: Usuario_1",
                                                                            "instance": "/api/usuario/admin/addAdmin/Usuario_1"
                                                                        }
                                                                    """
                                                    )
                                            }
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Token caducado",
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
                                                                            "detail": "JWT expired 323 milliseconds ago at 2025-02-20T17:02:24.000Z. Current time: 2025-02-20T17:02:24.323Z. Allowed clock skew: 0 milliseconds.",
                                                                            "instance": "/api/usuario/admin/addAdmin/Usuario_1"
                                                                        }
                                                                    """
                                                    )
                                            }
                                    )
                            }
                    )
            }
    )
    @PutMapping("/admin/addAdmin/{username}")
    public UsuarioResponseDto addAdmin(
            @Parameter(in = ParameterIn.PATH,
            description = "Nombre de usuario",
            schema = @Schema(type = "string"),
            example = "Usuario_1")
            @PathVariable String username) {
        Usuario usuario = usuarioService.addRoleAdmin(username);
        return UsuarioResponseDto.of(usuario, usuarioService.getImageUrl(usuario.getAvatar()));
    }

    @Operation(summary = "Se quita el rol de admin a un usuario")
    @Parameter(in = ParameterIn.HEADER, description = "Authorization token",
            name = "JWT-Auth-Token", content = @Content(schema = @Schema(type = "string")),
            example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhYTljMGY2OS00ZTRkLTQ1YjctOWFkMC01ZjU0MmI0YmZiMGUiLCJpYXQiOjE3Mzk5Njk5NTgsImV4cCI6MTczOTk3MDAxOH0.-fIz2zXh-aGZepekV2MZ5mxQMR2pJRrel1-c-XDIdmk")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Se quita el rol correctamente",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = UsuarioResponseDto.class),
                                            examples = {
                                                    @ExampleObject(
                                                            value = """
                                                                        {
                                                                            "id": "c2f7ec8b-d98a-4f67-8430-f7655a323445",
                                                                            "username": "Usuario_1",
                                                                            "nombreCompleto": "Usuario 1",
                                                                            "avatar": "avatar.png",
                                                                            "roles": [
                                                                                "ALMACENERO"
                                                                            ]
                                                                        }
                                                                    """
                                                    )
                                            }
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Usuario no encontrado",
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
                                                                            "detail": "No se ha encontrado el usuario: Usuario_1",
                                                                            "instance": "/api/usuario/admin/removeAdmin/Usuario_1"
                                                                        }
                                                                    """
                                                    )
                                            }
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Token caducado",
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
                                                                            "detail": "JWT expired 323 milliseconds ago at 2025-02-20T17:02:24.000Z. Current time: 2025-02-20T17:02:24.323Z. Allowed clock skew: 0 milliseconds.",
                                                                            "instance": "/api/usuario/admin/removeAdmin/Usuario_1"
                                                                        }
                                                                    """
                                                    )
                                            }
                                    )
                            }
                    )
            }
    )
    @PutMapping("/admin/removeAdmin/{username}")
    public UsuarioResponseDto removeAdmin(
            @Parameter(in = ParameterIn.PATH,
            description = "Nombre de usuario",
            schema = @Schema(type = "string"),
            example = "Usuario_1")
            @PathVariable String username) {
        Usuario usuario = usuarioService.removeRoleAdmin(username);
        return UsuarioResponseDto.of(usuario, usuarioService.getImageUrl(usuario.getAvatar()));
    }

    @Operation(summary = "Se elimina un usuario")
    @Parameter(in = ParameterIn.HEADER, description = "Authorization token",
            name = "JWT-Auth-Token", content = @Content(schema = @Schema(type = "string")),
            example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhYTljMGY2OS00ZTRkLTQ1YjctOWFkMC01ZjU0MmI0YmZiMGUiLCJpYXQiOjE3Mzk5Njk5NTgsImV4cCI6MTczOTk3MDAxOH0.-fIz2zXh-aGZepekV2MZ5mxQMR2pJRrel1-c-XDIdmk")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Se borra el usuario correctamente",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "El token ha caducado",
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
                                                                            "detail": "JWT expired 170529 milliseconds ago at 2025-02-20T17:31:07.000Z. Current time: 2025-02-20T17:33:57.529Z. Allowed clock skew: 0 milliseconds.",
                                                                            "instance": "/api/usuario/admin/delete/Usuario_1"
                                                                        }
                                                                    """
                                                    )
                                            }
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "El usuario no se puede borrar a si mismo",
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
                                                                            "detail": "Un usuario no se puede borrar a si mismo",
                                                                            "instance": "/api/usuario/admin/delete/admin"
                                                                        }
                                                                    """
                                                    )
                                            }
                                    )
                            }
                    )
            }
    )
    @PreAuthorize("#username != authentication.principal.username")
    @DeleteMapping("/admin/delete/{username}")
    public ResponseEntity<Void> deleteByUsername(@PathVariable String username) {

        usuarioService.deleteByUsername(username);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/auth/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal Usuario usuario) {
        refreshTokenService.cerrarSesion(usuario);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/download/{id:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String id) {

        Resource resource = storageService.loadAsResource(id);

        String mimeType = mimeTypeDetector.getMimeType(resource);

        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", mimeType)
                .body(resource);
    }
}
