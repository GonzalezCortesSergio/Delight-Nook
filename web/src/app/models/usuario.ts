export interface Usuario {
    username: string;
    token: string;
    refreshToken: string;
    nombreCompleto: string;
    avatar: string;
    roles: string[];
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