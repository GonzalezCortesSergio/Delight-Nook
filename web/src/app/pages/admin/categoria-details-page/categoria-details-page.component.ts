import { Component, inject, OnInit } from '@angular/core';
import { CategoriaService } from '../../../services/categoria.service';
import { UsuarioService } from '../../../services/usuario.service';
import { Router } from '@angular/router';
import { Categoria, CategoriaDetails } from '../../../models/categoria';
import { ErrorResponse } from '../../../models/error';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ModalDeleteCategoriaComponent } from '../../../components/admin/modal-delete-categoria/modal-delete-categoria.component';

@Component({
  selector: 'app-categoria-details-page',
  templateUrl: './categoria-details-page.component.html',
  styleUrl: './categoria-details-page.component.css'
})
export class CategoriaDetailsPageComponent implements OnInit{

  constructor(private categoriaService: CategoriaService, private usuarioService: UsuarioService, private router: Router) { }

  private modalService = inject(NgbModal);

  categoria: CategoriaDetails | null = null;

  id: number | null = null;

  errorMessage: string | null = null;

  ngOnInit(): void {
    this.id = Number.parseInt(this.router.url.replace(/\D/g, ""));
    
    this.cargarCategoria();
  }

  private cargarCategoria() {
    this.categoriaService.findById(this.id!)
    .subscribe({
      next: res => {
        console.log(res);
        this.categoria = res;
      },
      error: err => {
        const errorResponse: ErrorResponse = err.error;

        console.log(errorResponse);

        if(errorResponse.status == 401) {
          this.refrescarToken();
        }

        else if(errorResponse.status == 400) {
          this.errorMessage = "El valor del ID no es válido";
        }

        else {
          this.errorMessage = errorResponse.detail;
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
        this.cargarCategoria();
      }
    })
  }

  mostrarDetalles(categoria: Categoria) {
    window.location.href = `/admin/categorias/detalles/${categoria.id}`;
  }

  openModalDelete(categoria: Categoria) {
    const modalRef = this.modalService.open(ModalDeleteCategoriaComponent);
  
      modalRef.componentInstance.categoria = categoria;
  
      modalRef.closed.subscribe(() => {
        this.cargarCategoria();
      })
  }
}
