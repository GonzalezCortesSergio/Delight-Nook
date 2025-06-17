import { Component, inject, OnInit } from '@angular/core';
import { VentaService } from '../../../services/venta.service';
import { UsuarioService } from '../../../services/usuario.service';
import { Venta, VentasCajaResponse } from '../../../models/venta';
import { Caja } from '../../../models/caja';
import { ErrorResponse } from '../../../models/error';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ModalDetailsVentaComponent } from '../../../components/admin/modal-details-venta/modal-details-venta.component';

@Component({
  selector: 'app-caja-ventas-page',
  templateUrl: './caja-ventas-page.component.html',
  styleUrl: './caja-ventas-page.component.css'
})
export class CajaVentasPageComponent implements OnInit {

  constructor(private ventaService: VentaService, private usuarioService: UsuarioService) { }

  private modalService = inject(NgbModal);

  ventas: VentasCajaResponse | null = null;

  caja: Caja | null = null;

  page = 1;

  ngOnInit(): void {
    this.cargarVentas();
  }

  getFecha(venta: Venta) {
    return venta.fechaVenta.split(" ")[0];
  }

  getHora(venta: Venta) {
    return venta.fechaVenta.split(" ")[1]
  }

  cambiarVentas() {
    this.cargarVentas();
  }

  private cargarVentas() {
    this.ventaService.getVentasPorCajero(this.page - 1)
      .subscribe({
        next: res => {
          this.ventas = res;

          this.cargarCaja(res.result.content[0]);
        },
        error: err => {
          const errorResponse: ErrorResponse = err.error;

          if (errorResponse.status == 401) {
            this.usuarioService.refreshToken(() => this.cargarVentas());
          }
        }
      });
  }

  private cargarCaja(venta: Venta) {
    this.ventaService.getVentaDetailsCajero(venta.id)
      .subscribe({
        next: res => {
          this.caja = res.caja;
        },
        error: err => {
          const errorResponse: ErrorResponse = err.error;

          if (errorResponse.status == 401) {
            this.usuarioService.refreshToken(() => this.cargarCaja(venta));
          }
        }
      })
  }

  openModalDetailsVenta(id: string) {
    const modalRef = this.modalService.open(ModalDetailsVentaComponent,
      { size: "lg" }
    );

    modalRef.componentInstance.idVenta = id;
  }
}
