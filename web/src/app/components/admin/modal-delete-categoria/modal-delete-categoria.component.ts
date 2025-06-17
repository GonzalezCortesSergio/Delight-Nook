import { Component, inject, Input } from '@angular/core';
import { CategoriaService } from '../../../services/categoria.service';
import { UsuarioService } from '../../../services/usuario.service';
import { Categoria } from '../../../models/categoria';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ErrorResponse } from '../../../models/error';

@Component({
  selector: 'app-modal-delete-categoria',
  templateUrl: './modal-delete-categoria.component.html',
  styleUrl: './modal-delete-categoria.component.css'
})
export class ModalDeleteCategoriaComponent {

  constructor(private categoriaService: CategoriaService, private usuarioService: UsuarioService) { }

  private activeModal = inject(NgbActiveModal);

  @Input()
  categoria: Categoria | null = null;

  borrarCategoria() {
    this.categoriaService.deleteCategoria(this.categoria!.id)
    .subscribe({
      next: () => {
        this.activeModal.close();
      },
      error: err => {
        const errorResponse: ErrorResponse = err.error;

        if(errorResponse.status == 401) {
          this.usuarioService.refreshToken(() => this.borrarCategoria());
        }
      } 
    });
  }

  closeModal() {
    this.activeModal.close();
  }

}
