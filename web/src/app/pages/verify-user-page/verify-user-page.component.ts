import { Component } from '@angular/core';
import { UsuarioService } from '../../services/usuario.service';
import { ValidateUsuario } from '../../models/usuario';
import { ErrorResponse } from '../../models/error';

@Component({
  selector: 'app-verify-user-page',
  templateUrl: './verify-user-page.component.html',
  styleUrl: './verify-user-page.component.css'
})
export class VerifyUserPageComponent {

  constructor(private usuarioService: UsuarioService) { }

  password = "";
  verifyPassword = "";
  activationToken = "";

  showPassword = false;

  errorMessage: string | null = null;

  validarUsuario() {
    this.usuarioService.validarUsuario(this.toValidateUsuario())
    .subscribe({
      next: () => {
        alert("Se ha verificado el usuario correctamente, puede cerrar esta pestaña");
      },
      error: err => {
        const errorResponse: ErrorResponse = err.error;

        this.errorMessage = errorResponse.detail;

        for(const param of errorResponse["invalid-params"]) {
          this.errorMessage += `, ${param.field}: ${param.message}`;
        }
      }
    })
  }

  typePassword() {
    return this.showPassword ? "text" : "password";
  }

  private toValidateUsuario(): ValidateUsuario {

    return new ValidateUsuario(
      this.password,
      this.verifyPassword,
      this.activationToken
    );
  }
}
