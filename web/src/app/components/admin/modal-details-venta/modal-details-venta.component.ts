import { Component, inject, Input, OnInit } from '@angular/core';
import { VentaService } from '../../../services/venta.service';
import { UsuarioService } from '../../../services/usuario.service';
import { Router } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { VentaDetails } from '../../../models/venta';
import { ErrorResponse } from '../../../models/error';

@Component({
  selector: 'app-modal-details-venta',
  templateUrl: './modal-details-venta.component.html',
  styleUrl: './modal-details-venta.component.css'
})
export class ModalDetailsVentaComponent implements OnInit{

  constructor(private ventaService: VentaService, private usuarioService: UsuarioService, private router: Router) { }

  @Input()
  idVenta: string | null = null;

  venta: VentaDetails | null = null;

  ngOnInit(): void {
    this.cargarVenta();
  }


  private cargarVenta() {
    this.ventaService.getVentaDetails(this.idVenta!)
    .subscribe({
      next: res => {
        this.venta = res;
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
        this.cargarVenta();
      },
      error: () => {
        localStorage.clear();
        this.router.navigateByUrl("/login");
      }
    })
  }

}
