import { Component, OnInit } from '@angular/core';
import { CajaService } from '../../services/caja.service';
import { Caja } from '../../models/caja';
import { VentaService } from '../../services/venta.service';
import { UsuarioService } from '../../services/usuario.service';
import { Router } from '@angular/router';
import { VentasResponse } from '../../models/venta';
import { ErrorResponse } from '../../models/error';

@Component({
  selector: 'app-caja-details-page',
  templateUrl: './caja-details-page.component.html',
  styleUrl: './caja-details-page.component.css'
})
export class CajaDetailsPageComponent implements OnInit{

  constructor(private cajaService: CajaService, private ventaService: VentaService, private usuarioService: UsuarioService, private router: Router) { }

  caja: Caja | null = null;

  id: number = 0;

  ventas: VentasResponse | null = null;

  recaudado: number | null = null;

  page = 1;

  errorMessage: string | null = null;

  ngOnInit(): void {
    this.id = Number.parseInt(this.router.url.replace(/\D/g, ""));
    this.caja = this.cajaService.getCajaDetails();

    if(this.caja && this.caja.id == this.id) {
      this.cargarVentas();
    }
    else {
      this.errorMessage = "Error al cargar la caja";
    }
  }



  private cargarVentas() {
    this.ventaService.getVentasPorCaja(this.caja!.id, this.page - 1)
    .subscribe({
      next: res => {
        this.ventas = res.result;
        this.recaudado = res.totalRecaudado;
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
        this.cargarVentas();
      },
      error: () => {
        localStorage.clear();
        this.router.navigateByUrl("/login");
      }
    })
  }
}
