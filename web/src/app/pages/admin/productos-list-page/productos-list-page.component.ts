import { Component, inject, OnInit } from '@angular/core';
import { ProductoService } from '../../../services/producto.service';
import { UsuarioService } from '../../../services/usuario.service';
import { Producto, ProductoFilter, ProductoResponse } from '../../../models/producto';
import { ErrorResponse } from '../../../models/error';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ModalDeleteProductoComponent } from '../../../components/admin/modal-delete-producto/modal-delete-producto.component';

@Component({
  selector: 'app-productos-list-page',
  templateUrl: './productos-list-page.component.html',
  styleUrl: './productos-list-page.component.css'
})
export class ProductosListPageComponent implements OnInit{

  constructor(private productoService: ProductoService, private usuarioService: UsuarioService, private router: Router) { }

  private modalService = inject(NgbModal);

  productos: ProductoResponse | null = null;

  nombre = "";
  categoria = "";
  proveedor = "";
  precioMin = 0;
  precioMax = 0;

  page = 1;

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

      }
    })
  }

  cambiarProductos() {
    this.cargarProductos();
  }

  cambiarProductosConFiltro() {
    this.page = 1;
    this.cargarProductos();
  }

  getImagen(producto: Producto): string {
    return producto.imagen ? producto.imagen : "product-development_8787075.png";
  } 

  openDetails(id: number) {
    this.router.navigateByUrl(`/admin/productos/detalles/${id}`);
  }

  openModalDelete(producto: Producto) {
    const modalRef = this.modalService.open(ModalDeleteProductoComponent);

    modalRef.componentInstance.producto = producto;

    modalRef.closed.subscribe(() => {
      this.cargarProductos();
    })
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
