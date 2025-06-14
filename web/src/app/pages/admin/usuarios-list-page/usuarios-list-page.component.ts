import { Component, inject, OnInit } from '@angular/core';
import { UsuarioService } from '../../../services/usuario.service';
import { Usuario, UsuarioResponse } from '../../../models/usuario';
import { ErrorResponse } from '../../../models/error';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ModalDeleteUserComponent } from '../../../components/admin/modal-delete-user/modal-delete-user.component';
import { ModalDetailsUserComponent } from '../../../components/admin/modal-details-user/modal-details-user.component';

@Component({
  selector: 'app-usuarios-list-page',
  templateUrl: './usuarios-list-page.component.html',
  styleUrl: './usuarios-list-page.component.css'
})
export class UsuariosListPageComponent implements OnInit{

  constructor(private usuarioService: UsuarioService) { }

  private modalService = inject(NgbModal);

  usuarios: UsuarioResponse | null = null;

  pagina = 1;

  ngOnInit(): void {
    this.cargarUsuarios();
  }

  private cargarUsuarios() {
    this.usuarioService.findAll(this.pagina - 1)
    .subscribe({
      next: res => {
        this.usuarios = res;
      },
      error: err => {
        const errorResponse: ErrorResponse = err.error;

        if(errorResponse.status == 401) {
          this.usuarioService.refreshToken(() => this.cargarUsuarios());
        }
      }
    })
  }

  getImagen(usuario: Usuario) {
    return usuario.avatar ? usuario.avatar : "profile-default-icon.png";
  }

  cambiarUsuarios() {
    this.cargarUsuarios();
  }

  openModalDetails(usuario: Usuario) {
    const modalRef = this.modalService.open(ModalDetailsUserComponent);

    modalRef.componentInstance.usuario = usuario;

    modalRef.closed.subscribe(() => {
      this.cargarUsuarios();
    })
  }
  openModalDelete(username: string) {

    const modalRef = this.modalService.open(ModalDeleteUserComponent);

    modalRef.componentInstance.username = username;

    modalRef.closed.subscribe(() => {
      this.cargarUsuarios();
    })
  }

  colorCard(usuario: Usuario): string {
    return usuario.enabled ? "background-color: white;" : "background-color: rgb(200, 200, 200);"
  }
}