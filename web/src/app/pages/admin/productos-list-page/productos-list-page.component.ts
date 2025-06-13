import { Component, OnInit } from '@angular/core';
import { ProductoService } from '../../../services/producto.service';
import { UsuarioService } from '../../../services/usuario.service';
import { Producto, ProductoFilter, ProductoResponse } from '../../../models/producto';
import { ErrorResponse } from '../../../models/error';
import { Router } from '@angular/router';

@Component({
  selector: 'app-productos-list-page',
  templateUrl: './productos-list-page.component.html',
  styleUrl: './productos-list-page.component.css'
})
export class ProductosListPageComponent implements OnInit{

  constructor(private productoService: ProductoService, private usuarioService: UsuarioService, private router: Router) { }

  productos: ProductoResponse | null = null;

  nombre = "";
  categoria = "";
  proveedor = "";
  precioMin = 0;
  precioMax = 0;

  page = 1;

  errorMessage: string | null = null;

  ngOnInit(): void {
    this.cargarProductos();
  }


  private cargarProductos() {
    this.productoService.findAll(this.page - 1, this.toProductoFilter())
    .subscribe({
      next: res => {
        this.productos = res;
      },
      error: err => {
        const errorResponse: ErrorResponse = err.error;

        console.log(errorResponse);

        if(errorResponse.status == 401) {
          this.usuarioService.refreshToken(() => this.cargarProductos());
        }

        else {
          this.errorMessage = errorResponse.detail;
        }
      }
    })
  }

  cambiarProductos() {
    this.cargarProductos();
  }

  getImagen(producto: Producto): string {
    return producto.imagen ? producto.imagen : "product-development_8787075.png";
  } 

  openDetails(id: number) {
    this.router.navigateByUrl(`/admin/productos/detalles/${id}`);
  }

  openModalDelete(producto: Producto) {

  }

  private toProductoFilter() {
    return new ProductoFilter(
      this.nombre,
      this.categoria,
      this.proveedor,
      this.precioMin,
      this.precioMax
    );
  }
}
