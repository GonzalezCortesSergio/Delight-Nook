import { Component, OnInit } from '@angular/core';
import { UsuarioService } from '../../../services/usuario.service';
import { Router } from '@angular/router';
import { Usuario } from '../../../models/usuario';
import { ErrorResponse } from '../../../models/error';

@Component({
  selector: 'app-profile-details-almacenero-page',
  templateUrl: './profile-details-almacenero-page.component.html',
  styleUrl: './profile-details-almacenero-page.component.css'
})
export class ProfileDetailsAlmaceneroPageComponent implements OnInit {

  constructor(private usuarioService: UsuarioService, private router: Router) { }
  
    usuario: Usuario | null = null;
  
    ngOnInit(): void {
        
      this.cargarUsuario();
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
  
    getImagen() {
      return this.usuario?.avatar ? this.usuario.avatar : "profile-default-icon.png";
    }
  
    logout() {
      this.usuarioService.cerrarSesion()
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
