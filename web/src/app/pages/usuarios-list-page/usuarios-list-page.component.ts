import { Component, inject, OnInit } from '@angular/core';
import { UsuarioService } from '../../services/usuario.service';
import { Router } from '@angular/router';
import { Usuario, UsuarioResponse } from '../../models/usuario';
import { ErrorResponse } from '../../models/error';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ModalDeleteUserComponent } from '../../components/modal-delete-user/modal-delete-user.component';

@Component({
  selector: 'app-usuarios-list-page',
  templateUrl: './usuarios-list-page.component.html',
  styleUrl: './usuarios-list-page.component.css'
})
export class UsuariosListPageComponent implements OnInit{

  constructor(private usuarioService: UsuarioService, private router: Router) { }

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
        this.cargarUsuarios();
      },
      error: () => {
        localStorage.clear();
        this.router.navigateByUrl("/login");
      }
    })
  }

  getImagen(usuario: Usuario) {
    return usuario.avatar ? usuario.avatar : "profile-default-icon.png";
  }

  cambiarUsuarios() {
    this.cargarUsuarios();
  }

  openModalDelete(username: string) {

    const modalRef = this.modalService.open(ModalDeleteUserComponent);

    modalRef.componentInstance.username = username;

    modalRef.closed.subscribe(() => {
      this.cargarUsuarios();
    })
  }
}