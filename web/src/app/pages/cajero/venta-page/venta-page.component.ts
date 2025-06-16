import { Component, OnInit } from '@angular/core';
import { VentaService } from '../../../services/venta.service';
import { ProductoService } from '../../../services/producto.service';
import { UsuarioService } from '../../../services/usuario.service';
import { CategoriaService } from '../../../services/categoria.service';
import { Categoria } from '../../../models/categoria';
import { Producto, ProductoCantidad, ProductoFilter, ProductoResponse } from '../../../models/producto';
import { LineaVenta, VentaDetails } from '../../../models/venta';
import { ErrorResponse } from '../../../models/error';

@Component({
  selector: 'app-venta-page',
  templateUrl: './venta-page.component.html',
  styleUrl: './venta-page.component.css'
})
export class VentaPageComponent implements OnInit {

  constructor(private ventaService: VentaService, private productoService: ProductoService, 
  private usuarioService: UsuarioService, private categoriaService: CategoriaService) { }

  page = 0;

  categorias: Categoria[] = [];

  categoria = "";

  nombreProducto = "";

  errorMessage: string | null = null;

  productos: ProductoResponse | null = null;

  venta: VentaDetails | null = null;

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

  cambiarProductos() {
    this.cargarProductos();
  }

  cambiarProductosConFiltro() {
    this.page = 1;
    this.cargarProductos();
  }

  private cargarProductos() {
    this.errorMessage = null;
    this.productoService.findAll(this.page - 1, new ProductoFilter(this.nombreProducto, this.categoria, "", 0, 0))
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
    });

  }

  getImagen(producto: Producto): string {
    return producto.imagen ? producto.imagen : "product-development_8787075.png";
  }

  addProductoToVenta(idProducto: number) {
    this.ventaService.addProductoToVenta(this.toProductoCantidad(idProducto))
    .subscribe({
      next: res => {
        this.venta = res;
      },
      error: err => {
        const errorResponse: ErrorResponse = err.error;

        if(errorResponse.status == 401) {
          this.usuarioService.refreshToken(() => this.addProductoToVenta(idProducto));
        }
      }
    });
  }

  cambiarCantidad(lineaVenta: LineaVenta, idProducto: number, cantidad: number) {
    if(cantidad > 0) {
      this.changeProductoInVenta(idProducto, cantidad);
    }

    else {
      this.deleteProductoInVenta(lineaVenta);
    }
  }

  finalizarVenta() {
    this.ventaService.finalizarVenta()
    .subscribe({
      next: () => {
        this.venta = null;
      },
      error: err => {
        const errorResponse: ErrorResponse = err.error;

        if(errorResponse.status == 401) {
          this.usuarioService.refreshToken(() => this.finalizarVenta());
        }
      }
    })
  }

  private changeProductoInVenta(idProducto: number, cantidad: number) {
    this.ventaService.addProductoToVenta(this.toProductoCantidad(idProducto, cantidad))
    .subscribe({
      next: res => {
        this.venta = res;
      },
      error: err => {
        const errorResponse: ErrorResponse = err.error;

        if(errorResponse.status == 401) {
          this.usuarioService.refreshToken(() => this.changeProductoInVenta(idProducto, cantidad));
        }
      }
    });
  }

  private deleteProductoInVenta(lineaVenta: LineaVenta) {
    this.ventaService.removeLineaVenta(lineaVenta.id)
    .subscribe({
      next: res => {
        this.venta = res;
      },
      error: err => {
        const errorResponse: ErrorResponse = err.error;
        console.log(errorResponse);

        if(errorResponse.status == 401) {
          this.usuarioService.refreshToken(() => this.deleteProductoInVenta(lineaVenta));
        }
      }
    })
  }

  private toProductoCantidad(idProducto: number, cantidad: number = 1) {
    return new ProductoCantidad(idProducto, cantidad);
  }

}
