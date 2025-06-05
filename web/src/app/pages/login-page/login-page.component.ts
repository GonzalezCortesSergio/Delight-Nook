import { Component } from '@angular/core';
import { UsuarioService } from '../../services/usuario.service';
import { Router } from '@angular/router';
import { LoginRequest } from '../../models/usuario';
import { ErrorResponse } from '../../models/error';

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.css'
})
export class LoginPageComponent {

  constructor(private usuarioService: UsuarioService, private router: Router) { }

  role = "Tipo de usuario";

  username = "";

  password = "";

  errorMessage: string | null = null;

  showPassword = false;


  iniciarSesion() {

    this.errorMessage = null;


    if(this.role == "ADMIN") {
      this.iniciarSesionAdmin();
    }

    if(this.role == "ALMACENERO") {
      this.iniciarSesionAlmacenero();
    }

    else {
      this.errorMessage = "Es necesario especificar bajo qué tipo de usuario quiere iniciar sesión";
    }
  }

  private iniciarSesionAdmin() {
    this.usuarioService.iniciarSesion(this.toLoginRequest())
    .subscribe({
      next: res => {
        if(res.roles.includes("ADMIN")) {

          localStorage.setItem("token", res.token);
          localStorage.setItem("refreshToken", res.refreshToken);
          localStorage.setItem("role", "ADMIN");

          this.router.navigateByUrl("/admin/home");
        }

        else {
          this.errorMessage = "El usuario no es de ese tipo";
        }
      },
      error: err => {
        const errorResponse: ErrorResponse = err.error;

        this.errorMessage = errorResponse.detail;

        for (const invalidParam of errorResponse["invalid-params"]) {
          this.errorMessage+=`, ${invalidParam.field} ${invalidParam.message}`;
        }
      }
    })
  }

  private iniciarSesionAlmacenero() {
    this.usuarioService.iniciarSesion(this.toLoginRequest())
    .subscribe({
      next: res => {
        if(res.roles.includes("ALMACENERO")) {

          localStorage.setItem("token", res.token);
          localStorage.setItem("refreshToken", res.refreshToken);
          localStorage.setItem("role", "ALMACENERO");

          this.router.navigateByUrl("/almacenero/home");
        }
      }
    })
  }

  typePassword(): string {
    return this.showPassword ? "text" : "password";
  }

  private toLoginRequest(): LoginRequest {
    return new LoginRequest(this.username, this.password);
  }
}
