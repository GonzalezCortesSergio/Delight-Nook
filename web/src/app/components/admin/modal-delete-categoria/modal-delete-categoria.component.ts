import { Component, inject, Input } from '@angular/core';
import { CategoriaService } from '../../../services/categoria.service';
import { UsuarioService } from '../../../services/usuario.service';
import { Router } from '@angular/router';
import { Categoria } from '../../../models/categoria';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ErrorResponse } from '../../../models/error';

@Component({
  selector: 'app-modal-delete-categoria',
  templateUrl: './modal-delete-categoria.component.html',
  styleUrl: './modal-delete-categoria.component.css'
})
export class ModalDeleteCategoriaComponent {

  constructor(private categoriaService: CategoriaService, private usuarioService: UsuarioService, private router: Router) { }

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
          this.refrescarToken();
        }
      } 
    });
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
        this.borrarCategoria();
      },
      error: () => {
        localStorage.clear();
        this.router.navigateByUrl("/login");
      }
    });
  }
}
