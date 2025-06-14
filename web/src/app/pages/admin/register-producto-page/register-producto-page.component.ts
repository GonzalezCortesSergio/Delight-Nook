import { Component, OnInit } from '@angular/core';
import { ProductoService } from '../../../services/producto.service';
import { UsuarioService } from '../../../services/usuario.service';
import { CreateProducto } from '../../../models/producto';
import { CategoriaService } from '../../../services/categoria.service';
import { Categoria } from '../../../models/categoria';
import { ErrorResponse } from '../../../models/error';

@Component({
  selector: 'app-register-producto-page',
  templateUrl: './register-producto-page.component.html',
  styleUrl: './register-producto-page.component.css'
})
export class RegisterProductoPageComponent implements OnInit {

  constructor(private productoService: ProductoService, private usuarioService: UsuarioService, private categoriaService: CategoriaService) { }

  nombreProducto = "";

  precioUnitario = 0;

  descripcion = "";

  proveedor = "";

  categoriaId = -1;

  categorias: Categoria[] = [];

  page = 0;

  created = false;

  errorMessage: string | null = null;

  ngOnInit(): void {
    this.cargarCategorias();
  }

  registrarProducto() {

    this.created = false;
    this.errorMessage = null;
    this.productoService.createProducto(this.toCreateProducto())
    .subscribe({
      next: () => {
        this.created = true;
      },
      error: err => {
        const errorResponse: ErrorResponse = err.error;

        if(errorResponse.status == 401) {
          this.usuarioService.refreshToken(() => this.registrarProducto());
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

  private cargarCategorias() {
    this.categoriaService.findAll(this.page)
    .subscribe({
      next: res => {
        console.log(res);
        this.categorias = this.categorias.concat(res.content);
        this.page++;
        this.cargarCategorias();
      },
      error: err => {
        const errorResponse: ErrorResponse = err.error;

        if(errorResponse.status == 401) {
          this.usuarioService.refreshToken(() => this.cargarCategorias());
        }
      }
    })
  }

  closeValorationAlert() {
    this.created = false;
  }

  private toCreateProducto(): CreateProducto {
    return new CreateProducto(
      this.nombreProducto,
      this.precioUnitario,
      this.descripcion,
      this.categoriaId,
      this.proveedor
    );
  }
}
