import { Component, inject, Input } from '@angular/core';
import { CajaService } from '../../../services/caja.service';
import { UsuarioService } from '../../../services/usuario.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { EditCaja } from '../../../models/caja';
import { ErrorResponse } from '../../../models/error';

@Component({
  selector: 'app-modal-edit-caja',
  templateUrl: './modal-edit-caja.component.html',
  styleUrl: './modal-edit-caja.component.css'
})
export class ModalEditCajaComponent {

  constructor(private cajaService: CajaService, private usuarioService: UsuarioService) { }

  @Input()
  id: number | null = null;

  dinero = 0;

  errorMessage: string | null = null;

  multiplicador = 0;

  private modalActive = inject(NgbActiveModal);


  editarDinero() {
    this.errorMessage = null;

    if(this.dinero < 0) {
      this.dinero*= -1;
    }
    if (this.multiplicador != 0) {

      this.dinero *= this.multiplicador;

      this.cajaService.editDineroCaja(this.toEditCaja())
        .subscribe({
          next: () => {
            this.modalActive.close();
          },
          error: err => {
            const errorResponse: ErrorResponse = err.error;

            if (errorResponse.status == 401)
              this.usuarioService.refreshToken(() => this.editarDinero());

            else {
              this.errorMessage = errorResponse["invalid-params"][0].message;
              this.dinero = 0;
            }
          }
        });
    }

    else {
      this.errorMessage = "Debe indicar qué operación quiere realizar"
    }
  }

  private toEditCaja() {
    return new EditCaja(this.id!, this.dinero);
  }
}
