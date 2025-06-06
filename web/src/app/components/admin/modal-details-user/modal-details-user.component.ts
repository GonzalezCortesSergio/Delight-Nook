import { Component, inject, Input } from '@angular/core';
import { UsuarioService } from '../../../services/usuario.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Usuario } from '../../../models/usuario';
import { ErrorResponse } from '../../../models/error';
import { Router } from '@angular/router';

@Component({
  selector: 'app-modal-details-user',
  templateUrl: './modal-details-user.component.html',
  styleUrl: './modal-details-user.component.css'
})
export class ModalDetailsUserComponent {

  constructor(private usuarioService: UsuarioService, private router: Router) { }

  activeModal = inject(NgbActiveModal);

  @Input()
  usuario: Usuario | null = null;

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
          this.refrescarToken();
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
          this.refrescarToken();
        }
      }
    })
  }

  private refrescarToken() {
    this.usuarioService.refreshToken()
    .subscribe({
      next: res => {
        localStorage.setItem("token", res.token);
        localStorage.setItem("refreshToken", res.refreshToken);

        if(this.notAdmin())
          this.addRoleAdmin();

        if(this.isAdmin())
          this.removeRoleAdmin();
      },
      error: () => {
        localStorage.clear();
        this.router.navigateByUrl("/login");
      }
    })
  }
}
