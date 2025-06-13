import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UsuarioService } from '../../../services/usuario.service';

@Component({
  selector: 'app-admin-home-page',
  templateUrl: './admin-home-page.component.html',
  styleUrl: './admin-home-page.component.css'
})
export class AdminHomePageComponent implements OnInit{

  constructor(private router: Router, private usuarioService: UsuarioService) { }

  role = localStorage.getItem("role");
    
  ngOnInit(): void {
    if(!this.role || this.role !== "ADMIN")
      this.router.navigateByUrl("/login");
  }

  cerrarSesion() {
    this.usuarioService.cerrarSesion()
    .subscribe({
      next: () => {
        localStorage.clear();
        this.router.navigateByUrl("/login");
      },
      error: () => {
        this.usuarioService.refreshToken(() => this.cerrarSesion());
      }
    })
  }

}
