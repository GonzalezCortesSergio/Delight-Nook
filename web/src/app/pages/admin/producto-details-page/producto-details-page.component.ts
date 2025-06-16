import { Component, OnInit } from '@angular/core';
import { ProductoService } from '../../../services/producto.service';
import { UsuarioService } from '../../../services/usuario.service';
import { Router } from '@angular/router';
import { ProductoDetails } from '../../../models/producto';
import { ErrorResponse } from '../../../models/error';

@Component({
  selector: 'app-producto-details-page',
  templateUrl: './producto-details-page.component.html',
  styleUrl: './producto-details-page.component.css'
})
export class ProductoDetailsPageComponent implements OnInit {

  constructor(private productoService: ProductoService, private usuarioService: UsuarioService, private router: Router) { }

  id = 0;

  producto: ProductoDetails | null = null;

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

  getImagen() {
    return this.producto?.imagen ? this.producto.imagen : "product-development_8787075.png";
  }

  editProducto() {
    this.router.navigateByUrl(`/admin/editar/producto/${this.id}`);
  }
}
