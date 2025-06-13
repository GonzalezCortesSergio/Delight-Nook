import { Component, inject, OnInit } from '@angular/core';
import { CategoriaService } from '../../../services/categoria.service';
import { UsuarioService } from '../../../services/usuario.service';
import { Router } from '@angular/router';
import { Categoria, CreateCategoriaHija } from '../../../models/categoria';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ErrorResponse } from '../../../models/error';

@Component({
  selector: 'app-modal-create-categoria',
  templateUrl: './modal-create-categoria.component.html',
  styleUrl: './modal-create-categoria.component.css'
})
export class ModalCreateCategoriaComponent implements OnInit{

  constructor(private categoriaService: CategoriaService, private usuarioService: UsuarioService, private router: Router) { }

  private activeModal = inject(NgbActiveModal);

  categoriaMadreId = -1;

  categoriaNombre = "";

  errorMessage: string | null = null;

  categorias: Categoria[] = [];

  page = 0;

  ngOnInit(): void {
    this.cargarCategorias();
  }

  private cargarCategorias() {
    this.categoriaService.findAll(this.page)
    .subscribe({
      next: res => {
        this.categorias = this.categorias.concat(res.content);
        this.page++;
        this.cargarCategorias();
      },
      error: err => {
        const errorResponse: ErrorResponse = err.error;

        if(errorResponse.status == 401) {
          this.refrescarToken(() => this.cargarCategorias());
        }
      }
    })
  }

  createCategoria() {
    if(this.categoriaMadreId == -1) {
      this.createCategoriaBase();
    }
    else {
      this.createCategoriaHija();
    }

  }

  private createCategoriaBase() {
    this.categoriaService.createCategoria(this.categoriaNombre)
    .subscribe({
      next: () => {
        this.activeModal.close();
      },
      error: err => {
        const errorResponse: ErrorResponse = err.error;

        if(errorResponse.status == 401) {
          this.refrescarToken(() => this.createCategoriaBase());
        }

        if(errorResponse.status == 400) {
          this.errorMessage = "Debe proporcionar el nombre de la categoría";
        }
      }
    });
  }

  private createCategoriaHija() {
    this.categoriaService.createCategoriaHija(this.toCreateCategoriaHija())
    .subscribe({
      next: () => {
        this.activeModal.close();
      },
      error: err => {
        const errorResponse: ErrorResponse = err.error;
        console.log(errorResponse);

        if(errorResponse.status == 401) {
          this.refrescarToken(() => this.createCategoriaHija());
        }

        if(errorResponse.status == 400) {
          this.errorMessage = errorResponse.detail;

          for (const param of errorResponse["invalid-params"]) {
            this.errorMessage += `, ${param.field}: ${param.message}`;
          }
        }
      }
    })
  }

  private refrescarToken(method: Function) {
    this.usuarioService.refreshToken()
    .subscribe({
      next: res => {
        localStorage.setItem("token", res.token);
        localStorage.setItem("refreshToken", res.refreshToken);
        method();
      },
      error: () => {
        localStorage.clear();
        this.router.navigateByUrl("/login");
      }
    });  
  }


  private toCreateCategoriaHija(): CreateCategoriaHija {
    return new CreateCategoriaHija(
      this.categoriaMadreId!,
      this.categoriaNombre
    );
  }
} 
