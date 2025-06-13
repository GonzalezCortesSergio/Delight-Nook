import { Component, inject, Input } from '@angular/core';
import { UsuarioService } from '../../../services/usuario.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Usuario } from '../../../models/usuario';
import { ErrorResponse } from '../../../models/error';

@Component({
  selector: 'app-modal-details-user',
  templateUrl: './modal-details-user.component.html',
  styleUrl: './modal-details-user.component.css'
})
export class ModalDetailsUserComponent {

  constructor(private usuarioService: UsuarioService) { }

  activeModal = inject(NgbActiveModal);

  @Input()
  usuario: Usuario | null = null;

  errorMessage: string | null = null;

  getImagen(): string {
    return this.usuario?.avatar ? this.usuario.avatar : "profile-default-icon.png";
  }

  notAdmin(): boolean {
    return this.usuario?.roles.length == 1
    && !this.usuario.roles.includes("ADMIN");
  }

  isAdmin(): boolean {
    return this.usuario!.roles.length > 1
    && this.usuario!.roles.includes("ADMIN");
  }

  addRoleAdmin() {
    this.usuarioService.addRoleAdmin(this.usuario!.username)
    .subscribe({
      next: () => {
        this.activeModal.close();
      },
      error: err => {
        const errorResponse: ErrorResponse = err.error;

        if(errorResponse.status == 401) {
          this.usuarioService.refreshToken(() => this.addRoleAdmin());
        }
      }
    })
  }

  removeRoleAdmin() {
    this.usuarioService.removeRoleAdmin(this.usuario!.username)
    .subscribe({
      next: () => {
        this.activeModal.close();
      },
      error: err => {

        const errorResponse: ErrorResponse = err.error;

        if(errorResponse.status == 401) {
          this.usuarioService.refreshToken(() => this.removeRoleAdmin());
        }
      }
    })
  }

  disableUser() {
    this.usuarioService.disableUser(this.usuario!.username)
    .subscribe({
      next: () => {
        this.activeModal.close();
      },
      error: err => {

        const errorResponse: ErrorResponse = err.error;

        if(errorResponse.status == 401) {
          this.usuarioService.refreshToken(() => this.disableUser());
        }
        
        if(errorResponse.status == 403) {
          this.errorMessage = "No puede deshabilitar su propio perfil";
        }
      }
    })
  }

  enableUser() {
    this.usuarioService.enableUser(this.usuario!.username)
    .subscribe({
      next: () => {
        this.activeModal.close();
      },
      error: err => {
        const errorResponse: ErrorResponse = err.error;

        if(errorResponse.status == 401) {
          this.usuarioService.refreshToken(() => this.enableUser());
        }

        if(errorResponse.status == 400) {
          this.errorMessage = errorResponse.detail;
        }
      }
    })
  }

}
