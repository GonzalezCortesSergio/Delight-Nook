import { Component, inject, Input } from '@angular/core';
import { UsuarioService } from '../../../services/usuario.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ErrorResponse } from '../../../models/error';

@Component({
  selector: 'app-modal-delete-user',
  templateUrl: './modal-delete-user.component.html',
  styleUrl: './modal-delete-user.component.css'
})
export class ModalDeleteUserComponent {

  constructor(private usuarioService: UsuarioService) { }

  private activeModal = inject(NgbActiveModal);

  @Input()
  username: string | null = null;

  error = false;

  borrarUsuario() {
    this.usuarioService.deleteByUsername(this.username!)
    .subscribe({
      next: () => {
        this.activeModal.close();
      },
      error: err => {
        const errorResponse: ErrorResponse = err.error;

        if(errorResponse.status == 401) {
          this.usuarioService.refreshToken(() => this.borrarUsuario());
        }

        if(errorResponse.status == 403) {
          this.error = true;
        }
      }
    })
  }

  closeModal() {
    this.activeModal.close();
  }
}
