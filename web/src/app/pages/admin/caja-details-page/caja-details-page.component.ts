import { Component, inject, OnInit } from '@angular/core';
import { CajaService } from '../../../services/caja.service';
import { Caja } from '../../../models/caja';
import { VentaService } from '../../../services/venta.service';
import { UsuarioService } from '../../../services/usuario.service';
import { Router } from '@angular/router';
import { Venta, VentasResponse } from '../../../models/venta';
import { ErrorResponse } from '../../../models/error';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ModalEditCajaComponent } from '../../../components/admin/modal-edit-caja/modal-edit-caja.component';
import { ModalDetailsVentaComponent } from '../../../components/admin/modal-details-venta/modal-details-venta.component';

@Component({
  selector: 'app-caja-details-page',
  templateUrl: './caja-details-page.component.html',
  styleUrl: './caja-details-page.component.css'
})
export class CajaDetailsPageComponent implements OnInit{

  constructor(private cajaService: CajaService, private ventaService: VentaService, private usuarioService: UsuarioService, private router: Router) { }

  private modalService = inject(NgbModal);

  caja: Caja | null = null;

  id: number = 0;

  ventas: VentasResponse | null = null;

  recaudado: number | null = null;

  page = 1;

  errorMessage: string | null = null;

  notFound = false;

  ngOnInit(): void {
    this.id = Number.parseInt(this.router.url.replace(/\D/g, "")); 

    this.cargarCaja();
  }

  private cargarCaja() {
    this.cajaService.findById(this.id)
    .subscribe({
      next: res => {
        this.caja = res;
        this.cargarVentas();
      },
      error: err => {
        const errorResponse: ErrorResponse = err.error;

        if(errorResponse.status == 401) {
          this.refrescarToken(() => this.cargarCaja());
        }

        else if(errorResponse.status == 400) {
          this.errorMessage = "El valor del ID no es válido";
        }

        else {
          this.errorMessage = errorResponse.detail;
        }
      }
    })
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
          this.refrescarToken(() => this.cargarVentas());
        }

        else if(errorResponse.status == 404) {
          this.notFound = true;
        }
      }
    })
  }

  cambiarVentas() {
    this.cargarVentas();
  }

  private refrescarToken(method: Function) {
    this.usuarioService.refreshToken()
    .subscribe({
      next: res => {
        localStorage.setItem("token", res.token);
        localStorage.setItem("refreshToken", res.refreshToken);
        method();
      },
      error: () => {
        localStorage.clear();
        this.router.navigateByUrl("/login");
      }
    })
  }

  openModalEditCaja(id: number) {
    const modalRef = this.modalService.open(ModalEditCajaComponent);

    modalRef.componentInstance.id = id;

    modalRef.closed.subscribe(() => {
      this.cargarCaja();
    })
  }

  getFecha(venta: Venta) {
    return venta.fechaVenta.split(" ")[0];
  }

  getHora(venta: Venta) {
    return venta.fechaVenta.split(" ")[1];
  }

  marginInfoCaja(): string {
    return this.notFound ? "margin-top: 12%;" : "";
  }

  openModalDetailsVenta(id: string) {
    const modalRef = this.modalService.open(ModalDetailsVentaComponent,
      {size: "lg"}
    );

    modalRef.componentInstance.idVenta = id;
  }
}
