export interface Usuario {
    username: string;
    token: string;
    refreshToken: string;
    nombreCompleto: string;
    avatar: string;
    roles: string[];
    enabled: boolean;
}

export interface UsuarioResponse {
    content: Usuario[];
    totalElements: number;
    size: number;
}

export class LoginRequest {
    private username: string;
    private password: string;

    constructor(username: string, password: string) {
        this.username = username;
        this.password = password;
    }
}

export class CreateUsuario {
    private username: string;
    private nombreCompleto: string;
    private email: string;


    constructor(username: string, nombreCompleto: string, email: string) {
        this.username = username;
        this.nombreCompleto = nombreCompleto;
        this.email = email;
    }
}