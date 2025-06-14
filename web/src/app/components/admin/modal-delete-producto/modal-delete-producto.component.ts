import { Component, inject, Input } from '@angular/core';
import { ProductoService } from '../../../services/producto.service';
import { UsuarioService } from '../../../services/usuario.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Producto } from '../../../models/producto';
import { ErrorResponse } from '../../../models/error';

@Component({
  selector: 'app-modal-delete-producto',
  templateUrl: './modal-delete-producto.component.html',
  styleUrl: './modal-delete-producto.component.css'
})
export class ModalDeleteProductoComponent {

  constructor(private productoService: ProductoService, private usuarioService: UsuarioService) { }

  private activeModal = inject(NgbActiveModal);

  @Input()
  producto: Producto | null = null;

  borrarProducto() {
    this.productoService.deleteById(this.producto!.id)
    .subscribe({
      next: () => {
        this.activeModal.close();
      },
      error: err => {
        const errorResponse: ErrorResponse = err.error;

        if(errorResponse.status == 401) {
          this.usuarioService.refreshToken(() => this.borrarProducto());
        }
      }
    });
  }

  closeModal() {
    this.activeModal.close();
  }
}
