import { Component, OnInit } from '@angular/core';
import { UsuarioService } from '../../services/usuario.service';
import { CajaService } from '../../services/caja.service';
import { Router } from '@angular/router';
import { Caja } from '../../models/caja';
import { ErrorResponse } from '../../models/error';
import { LoginRequest } from '../../models/usuario';

@Component({
  selector: 'app-login-cajero-page',
  templateUrl: './login-cajero-page.component.html',
  styleUrl: './login-cajero-page.component.css'
})
export class LoginCajeroPageComponent implements OnInit{

  constructor(private usuarioService: UsuarioService, private cajaService: CajaService, private router: Router) { }

  page = 1;

  cajas: Caja[] = [];

  username = "";

  password = "";

  errorMessage: string | null = null;

  showPassword = false;

  idCaja = -1;

  ngOnInit(): void {
    this.iniciarSesionAnonimo();
  }

  iniciarSesion() {
    
    this.errorMessage = null;

    if(this.idCaja != -1) {
      this.cajaService.iniciarSesionCaja(this.toLoginRequest(this.username, this.password), this.idCaja)
      .subscribe({
        next: res => {
          if(res.roles.includes("CAJERO")) {
            localStorage.setItem("token", res.token);
            localStorage.setItem("refreshToken", res.refreshToken);

            this.router.navigateByUrl("/cajero/home");
          }

          else {
            this.errorMessage = "El usuario no es un cajero";
          }
        },
        error: err => {
          
        }
      })
    }
  }

  private iniciarSesionAnonimo() {
    this.usuarioService.iniciarSesion(this.toLoginRequest("admin", "admin"))
    .subscribe({
      next: res => {
        localStorage.setItem("token", res.token);
        this.cargarCajas();
      }
    })
  }

  private cargarCajas() {
    this.cajaService.findAll(this.page - 1)
    .subscribe({
      next: res => {
        this.cajas.concat(res.content);
        this.page++;
        this.cargarCajas();
      },
      error: () => {
        localStorage.clear();
      }
    })
  }

  private toLoginRequest(username: string, password: string) {
    return new LoginRequest(username, password);
  }
}
