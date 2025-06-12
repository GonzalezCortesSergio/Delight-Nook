import { Component, inject, OnInit } from '@angular/core';
import { CajaService } from '../../../services/caja.service';
import { UsuarioService } from '../../../services/usuario.service';
import { Router } from '@angular/router';
import { Caja, CajaResponse } from '../../../models/caja';
import { ErrorResponse } from '../../../models/error';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ModalDeleteCajaComponent } from '../../../components/admin/modal-delete-caja/modal-delete-caja.component';
import { ModalCreateCajaComponent } from '../../../components/admin/modal-create-caja/modal-create-caja.component';

@Component({
  selector: 'app-cajas-list-page',
  templateUrl: './cajas-list-page.component.html',
  styleUrl: './cajas-list-page.component.css'
})
export class CajasListPageComponent implements OnInit {

  constructor(private cajaService: CajaService, private usuarioService: UsuarioService, private router: Router) { }

  private modalService = inject(NgbModal);

  cajas : CajaResponse | null = null;

  pagina = 1;

  ngOnInit(): void {
    this.cargarCajas();
  }

  cambiarCajas() {
    this.cargarCajas();
  }

  private cargarCajas() {
    this.cajaService.findAll(this.pagina - 1)
    .subscribe({
      next: res => {
        this.cajas = res;
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
        this.cargarCajas();
      },
      error: () => {
        localStorage.clear();
        this.router.navigateByUrl("/login");
      }
    })
  }

  openDetails(caja: Caja) {
    this.router.navigateByUrl(`/admin/cajas/detalles/${caja.id}`);
  }

  openModalDelete(caja: Caja) {

    const modalRef = this.modalService.open(ModalDeleteCajaComponent);

    modalRef.componentInstance.caja = caja;
    
    modalRef.closed.subscribe(() => {
      this.cargarCajas();
    })
  }

  openModalCreate() {
    const modalRef = this.modalService.open(ModalCreateCajaComponent);

    modalRef.closed.subscribe(() => {
      this.cargarCajas();
    })
  }
}
