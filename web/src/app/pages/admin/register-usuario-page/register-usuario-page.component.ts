import { Component } from '@angular/core';
import { UsuarioService } from '../../../services/usuario.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register-usuario-page',
  templateUrl: './register-usuario-page.component.html',
  styleUrl: './register-usuario-page.component.css'
})
export class RegisterUsuarioPageComponent {
  constructor(private usuarioService: UsuarioService, private router: Router) { }
}
