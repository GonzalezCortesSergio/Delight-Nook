import { Component, inject, Input } from '@angular/core';
import { CajaService } from '../../../services/caja.service';
import { UsuarioService } from '../../../services/usuario.service';
import { Router } from '@angular/router';
import { Caja } from '../../../models/caja';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ErrorResponse } from '../../../models/error';

@Component({
  selector: 'app-modal-delete-caja',
  templateUrl: './modal-delete-caja.component.html',
  styleUrl: './modal-delete-caja.component.css'
})
export class ModalDeleteCajaComponent {

  constructor(private cajaService: CajaService, private usuarioService: UsuarioService, private router: Router) { }

  private activeModal = inject(NgbActiveModal);

  @Input()
  caja: Caja | null = null;

  borrarCaja() {
    this.cajaService.deleteCaja(this.caja!.id)
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
  closeModal() {
    this.activeModal.close();
  }

  private refrescarToken() {
    this.usuarioService.refreshToken()
    .subscribe({
      next: res => {
        localStorage.setItem("token", res.token);
        localStorage.setItem("refreshToken", res.refreshToken);
        this.borrarCaja();
      }
    })
  }
}
