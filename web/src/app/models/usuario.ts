import { Page } from "./page.interface";

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
    page: Page;
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

export class ValidateUsuario {
    private password: string;
    private verifyPassword: string;
    private activationToken: string;

    constructor(password: string, verifyPassword: string, activationToken: string) {
        this.password = password;
        this.verifyPassword = verifyPassword;
        this.activationToken = activationToken;
    }
}