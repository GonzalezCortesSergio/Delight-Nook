import { Component, OnInit } from '@angular/core';
import { ProductoService } from '../../../services/producto.service';
import { UsuarioService } from '../../../services/usuario.service';
import { Producto, ProductoCantidad, ProductoCantidadInterface, ProductoFilter, ProductoResponse } from '../../../models/producto';
import { CategoriaService } from '../../../services/categoria.service';
import { Categoria } from '../../../models/categoria';
import { ErrorResponse } from '../../../models/error';

@Component({
  selector: 'app-gestion-almacen-page',
  templateUrl: './gestion-almacen-page.component.html',
  styleUrl: './gestion-almacen-page.component.css'
})
export class GestionAlmacenPageComponent implements OnInit {

  constructor(private productoService: ProductoService, private categoriaService: CategoriaService, private usuarioService: UsuarioService) { }

  productosElegidos: ProductoCantidadInterface[] = [];

  productos: ProductoResponse | null = null;

  nombreProducto = "";

  categoria = "";

  errorMessage: string | null = null;

  page = 0;

  categorias: Categoria[] = [];


  ngOnInit(): void {
    this.cargarCategorias();
    this.cargarProductos();
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
          this.usuarioService.refreshToken(() => this.cargarCategorias());
        }

        else {
          this.page = 1;
        }
      }
    });
  }

  private cargarProductos() {

    this.errorMessage = null;
    this.productoService.findAll(this.page - 1, this.toProductoFilter())
    .subscribe({
      next: res => {
        this.productos = res;
      },
      error: err => {
        const errorResponse: ErrorResponse = err.error;

        if(errorResponse.status == 401) {
          this.usuarioService.refreshToken(() => this.cargarProductos());
        }

        else {
          this.errorMessage = errorResponse.detail;
        }
      }
    })
  }

  cambiarProductosConFiltro() {
    this.page = 1;
    this.cargarProductos();
  }

  cambiarProductos() {
    this.cargarProductos();
  }

  getImagen(producto: Producto): string {
    return producto.imagen ? producto.imagen : "product-development_8787075.png";
  }

  addProductoToAlmacen(producto: Producto) {
    let productoExistente = this.productosElegidos.filter(productoCantidad => productoCantidad.producto.id == producto.id)
    if(productoExistente.length == 0) {
      this.productosElegidos.push({producto: producto, cantidad: 1});
    }
  }

  finalizarGestionAlmacen() {
    this.productosElegidos.forEach(productoCantidad => {
      this.productoService.addProductoToStock(this.toProductoCantidad(productoCantidad.producto.id, productoCantidad.cantidad))
      .subscribe({
        next: () =>{
          this.productosElegidos = [];
        },
        error: err => {
          const errorResponse: ErrorResponse = err.error;

          if(errorResponse.status == 401) {
            this.usuarioService.refreshToken(() => this.finalizarGestionAlmacen());
          }
        }
      });
    })
  }

  private toProductoFilter() {
    return new ProductoFilter(this.nombreProducto, this.categoria, "", 0, 0);
  }

  private toProductoCantidad(idProducto: number, cantidad: number) {
    if(cantidad < 0) {
      cantidad*= -1;
    }

    return new ProductoCantidad(idProducto, cantidad);
  }
}
