import { Component, OnInit } from '@angular/core';
import { CategoriaService } from '../../../services/categoria.service';
import { Categoria, CategoriaResponse } from '../../../models/categoria';
import { ErrorResponse } from '../../../models/error';
import { UsuarioService } from '../../../services/usuario.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-categorias-list-page',
  templateUrl: './categorias-list-page.component.html',
  styleUrl: './categorias-list-page.component.css'
})
export class CategoriasListPageComponent implements OnInit{

  constructor(private categoriaService: CategoriaService, private usuarioService: UsuarioService, private router: Router) { }

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

  }

  openDetails(categoria: Categoria) {

  }
  openModalDelete(categoria: Categoria) {

  }
}
