import { Component } from '@angular/core';
import { UsuarioService } from '../../../services/usuario.service';
import { CreateUsuario } from '../../../models/usuario';
import { ErrorResponse } from '../../../models/error';

@Component({
  selector: 'app-register-usuario-page',
  templateUrl: './register-usuario-page.component.html',
  styleUrl: './register-usuario-page.component.css'
})
export class RegisterUsuarioPageComponent {
  constructor(private usuarioService: UsuarioService) { }

  username = "";
  email = "";
  nombreCompleto = "";

  role = "Rol del usuario";

  created = false;

  errorMessage: string | null = null;


  register() {
    if(this.role !== "Rol del usuario") {
      this.usuarioService.registrarUsuario(this.toCreateUsuario(), this.role)
      .subscribe({
        next: () => {
          this.created = true;
          this.username = "";
          this.email = "";
          this.nombreCompleto = "";
          this.role = "Rol del usuario";
          this.errorMessage = null;
        },
        error: err => {
          const errorResponse: ErrorResponse = err.error;

          if(errorResponse.status == 401) {
            this.usuarioService.refreshToken(() => this.register());
          }

          if(errorResponse.status == 500) {
            this.errorMessage = "El nombre de usuario o email no está disponible";
          }

          else {
            this.errorMessage = errorResponse.detail;

            for (const param of errorResponse["invalid-params"]) {
              this.errorMessage += `, ${param.field}: ${param.message}`;
            }
          }
        }
      });
    }

    else {
      this.errorMessage = "Es necesario indicar el rol del usuario a registrar";
    }
  }

  private toCreateUsuario(): CreateUsuario {
    return new CreateUsuario(
      this.username,
      this.nombreCompleto,
      this.email
    );
  }

  closeValorationAlert() {
    this.created = false;
  }


}
