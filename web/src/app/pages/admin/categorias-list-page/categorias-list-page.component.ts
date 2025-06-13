import { Component, inject, OnInit } from '@angular/core';
import { CategoriaService } from '../../../services/categoria.service';
import { Categoria, CategoriaResponse } from '../../../models/categoria';
import { ErrorResponse } from '../../../models/error';
import { UsuarioService } from '../../../services/usuario.service';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ModalCreateCategoriaComponent } from '../../../components/admin/modal-create-categoria/modal-create-categoria.component';
import { ModalDeleteCategoriaComponent } from '../../../components/admin/modal-delete-categoria/modal-delete-categoria.component';

@Component({
  selector: 'app-categorias-list-page',
  templateUrl: './categorias-list-page.component.html',
  styleUrl: './categorias-list-page.component.css'
})
export class CategoriasListPageComponent implements OnInit{

  constructor(private categoriaService: CategoriaService, private usuarioService: UsuarioService, private router: Router) { }

  private modalService = inject(NgbModal);

  page = 1;

  categorias: CategoriaResponse | null = null;

  ngOnInit(): void {
    this.cargarCategorias();
  }

  private cargarCategorias() {
    this.categoriaService.findAll(this.page - 1)
    .subscribe({
      next: res => {
        this.categorias = res;
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
        this.cargarCategorias();  
      }
    })
  }

  cambiarCategorias() {
    this.cargarCategorias();
  }

  openModalCreate() {
    const modalRef = this.modalService.open(ModalCreateCategoriaComponent);

    modalRef.closed.subscribe(() => {
      this.cargarCategorias();
    })
  }

  openDetails(categoria: Categoria) {
    this.router.navigateByUrl(`/admin/categorias/detalles/${categoria.id}`)
  }
  openModalDelete(categoria: Categoria) {
    const modalRef = this.modalService.open(ModalDeleteCategoriaComponent);

    modalRef.componentInstance.categoria = categoria;

    modalRef.closed.subscribe(() => {
      this.cargarCategorias();
    })
  }
}
