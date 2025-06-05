import { Component, inject, Input } from '@angular/core';
import { UsuarioService } from '../../services/usuario.service';
import { Router } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ErrorResponse } from '../../models/error';

@Component({
  selector: 'app-modal-delete-user',
  templateUrl: './modal-delete-user.component.html',
  styleUrl: './modal-delete-user.component.css'
})
export class ModalDeleteUserComponent {

  constructor(private usuarioService: UsuarioService, private router: Router) { }

  private activeModal = inject(NgbActiveModal);

  @Input()
  username: string | null = null;

  borrarUsuario() {
    this.usuarioService.deleteByUsername(this.username!)
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
        this.borrarUsuario();
      },
      error: () => {
        localStorage.clear();
        this.router.navigateByUrl("/login");
      }
    })
  }

  closeModal() {
    this.activeModal.close();
  }
}
