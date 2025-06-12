import { Component, inject } from '@angular/core';
import { CajaService } from '../../../services/caja.service';
import { UsuarioService } from '../../../services/usuario.service';
import { Router } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { CreateCaja } from '../../../models/caja';
import { ErrorResponse } from '../../../models/error';

@Component({
  selector: 'app-modal-create-caja',
  templateUrl: './modal-create-caja.component.html',
  styleUrl: './modal-create-caja.component.css'
})
export class ModalCreateCajaComponent {

  constructor(private cajaService: CajaService, private usuarioService: UsuarioService, private router: Router) { }

  private activeModal = inject(NgbActiveModal);

  nombreCaja = "";

  dineroCaja = 0;

  errorMessage: string | null = null;


  crearCaja() {
    this.cajaService.createCaja(this.toCreateCaja())
    .subscribe({
      next: () => {
        this.activeModal.close();
      },
      error: err => {
        const errorResponse: ErrorResponse = err.error;

        if(errorResponse.status == 401) {
          this.refrescarToken();
        }

        if(errorResponse.status == 400) {
          this.errorMessage = errorResponse.detail;

          for(const param of errorResponse["invalid-params"]) {
            this.errorMessage += `, ${param.field}: ${param.message}`;
          }
        }
      }
    })
  }

  private toCreateCaja(): CreateCaja {

    if(this.dineroCaja < 0) {
      this.dineroCaja*= -1;
    }
    return new CreateCaja(
      this.nombreCaja,
      this.dineroCaja
    );
  }

  private refrescarToken() {
    this.usuarioService.refreshToken()
    .subscribe({
      next: res => {
        localStorage.setItem("token", res.token);
        localStorage.setItem("refreshToken", res.refreshToken);
        this.crearCaja();
      }
    })
  }
}
