import { Component } from '@angular/core';
import { ProductoService } from '../../../services/producto.service';
import { UsuarioService } from '../../../services/usuario.service';
import { Router } from '@angular/router';
import { ProductoDetails } from '../../../models/producto';
import { ErrorResponse } from '../../../models/error';
import { CategoriaService } from '../../../services/categoria.service';
import { Categoria } from '../../../models/categoria';

@Component({
  selector: 'app-edit-producto-page',
  templateUrl: './edit-producto-page.component.html',
  styleUrl: './edit-producto-page.component.css'
})
export class EditProductoPageComponent {
    
  constructor(private productoService: ProductoService, private usuarioService: UsuarioService, private categoriaService: CategoriaService, private router: Router) { }

  id = 0;
  
  producto: ProductoDetails | null = null;
  
  nombreProducto = "";
  
  precioUnitario = 0;
  
  descripcion = "";
  
  proveedor = "";
  
  categoriaId = -1;
  
  categorias: Categoria[] = [];
  
  page = 0;
  
  errorMessage: string | null = null;
  
  ngOnInit(): void {
    this.id = Number.parseInt(this.router.url.replace(/\D/g, ""));
  
    this.cargarProducto();
  }

  private cargarProducto() {
    this.productoService.findById(this.id)
    .subscribe({
      next: res => {
        this.producto = res;
      },
      error: err => {
        const errorResponse: ErrorResponse = err.error;

        if(errorResponse.status == 401) {
          this.usuarioService.refreshToken(() => this.cargarProducto());
        }

        if(errorResponse.status == 404) {
          this.errorMessage = errorResponse.detail;
        }

        if(errorResponse.status == 400) {
          this.errorMessage = "El valor del ID no es válido";
        }
      }
    });
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
  
}
