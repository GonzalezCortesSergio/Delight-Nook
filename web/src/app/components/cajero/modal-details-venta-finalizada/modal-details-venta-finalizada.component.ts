import { Component, Input } from '@angular/core';
import { VentaService } from '../../../services/venta.service';
import { UsuarioService } from '../../../services/usuario.service';
import { VentaDetails } from '../../../models/venta';
import { ErrorResponse } from '../../../models/error';

@Component({
  selector: 'app-modal-details-venta-finalizada',
  templateUrl: './modal-details-venta-finalizada.component.html',
  styleUrl: './modal-details-venta-finalizada.component.css'
})
export class ModalDetailsVentaFinalizadaComponent {

  @Input()
  venta: VentaDetails | null = null;
}
