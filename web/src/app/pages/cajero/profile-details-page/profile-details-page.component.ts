import { Component, OnInit } from '@angular/core';
import { UsuarioService } from '../../../services/usuario.service';
import { Usuario } from '../../../models/usuario';
import { ErrorResponse } from '../../../models/error';
import { VentaService } from '../../../services/venta.service';
import { CajaService } from '../../../services/caja.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-profile-details-page',
  templateUrl: './profile-details-page.component.html',
  styleUrl: './profile-details-page.component.css'
})
export class ProfileDetailsPageComponent implements OnInit {

  constructor(private usuarioService: UsuarioService, private ventaService: VentaService, private cajaService: CajaService,
    private router: Router
  ) { }

  usuario: Usuario | null = null;
  numVentas: number | null = null;

  ngOnInit(): void {
      
    this.cargarUsuario();
    this.cargarNumVentas();
  }

  private cargarUsuario() {
    this.usuarioService.me()
    .subscribe({
      next: res => {
        this.usuario = res;
      },
      error: err => {
        const errorResponse: ErrorResponse = err.error;

        if(errorResponse.status == 401) {
          this.usuarioService.refreshToken(() => this.cargarUsuario());
        }
      }
    })
  }

  private cargarNumVentas() {
    this.ventaService.getNumVentasByCajero()
    .subscribe({
      next: res => {
        this.numVentas = res.numVentas;
      },
      error: err => {
        const errorResponse: ErrorResponse = err.error;

        console.log(errorResponse)
        if(errorResponse.status == 401) {
          this.usuarioService.refreshToken(() => this.cargarNumVentas());
        }
      }
    })
  }

  getImagen() {
    return this.usuario?.avatar ? this.usuario.avatar : "profile-default-icon.png";
  }

  logout() {
    this.cajaService.logoutCaja()
    .subscribe({
      next: () => {
        localStorage.clear();
        this.router.navigateByUrl("/login");
      },
      error: err => {
        const errorResponse: ErrorResponse = err.error;

        if(errorResponse.status == 401) {
          this.usuarioService.refreshToken(() => this.logout());
        }
      }
    });
  }
}
