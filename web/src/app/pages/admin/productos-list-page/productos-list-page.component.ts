import { Component, inject, OnInit } from '@angular/core';
import { ProductoService } from '../../../services/producto.service';
import { UsuarioService } from '../../../services/usuario.service';
import { Producto, ProductoFilter, ProductoResponse } from '../../../models/producto';
import { ErrorResponse } from '../../../models/error';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ModalDeleteProductoComponent } from '../../../components/admin/modal-delete-producto/modal-delete-producto.component';
import { CategoriaService } from '../../../services/categoria.service';
import { Categoria } from '../../../models/categoria';

@Component({
  selector: 'app-productos-list-page',
  templateUrl: './productos-list-page.component.html',
  styleUrl: './productos-list-page.component.css'
})
export class ProductosListPageComponent implements OnInit{

  constructor(private productoService: ProductoService, private usuarioService: UsuarioService, private router: Router,
    private categoriaService: CategoriaService
  ) { }

  private modalService = inject(NgbModal);

  productos: ProductoResponse | null = null;

  categorias: Categoria[] = [];

  errorMessage: string | null = null;

  nombre = "";
  categoria = "";
  proveedor = "";
  precioMin = 0;
  precioMax = 0;

  page = 0;

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
    })
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
