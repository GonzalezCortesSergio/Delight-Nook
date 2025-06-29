import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CreateUsuario, LoginRequest, Usuario, UsuarioResponse, ValidateUsuario } from '../models/usuario';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  constructor(private http: HttpClient, private router: Router) { }

  private baseUrl = "http://localhost:8080/api/usuario";

  //Inicio de sesión para admin y para almaceneros

  iniciarSesion(loginRequest: LoginRequest): Observable<Usuario> {
    return this.http.post<Usuario>(`${this.baseUrl}/auth/login`,
      loginRequest
    );
  }


  refreshToken(method: Function) {
    this.http.post<Usuario>(`${this.baseUrl}/auth/refresh/token`,
      {
        "refreshToken": localStorage.getItem("refreshToken")
      }
    )
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
    });
  }

  findAll(page: number): Observable<UsuarioResponse> {
    return this.http.get<UsuarioResponse>(`${this.baseUrl}/admin/listado?size=6&page=${page}`,
      {
        headers: this.createHeaders()
      }
    );
  }
  cerrarSesion() {
    return this.http.get(`${this.baseUrl}/auth/logout`,
      {
        headers: this.createHeaders()
      }
    );
  }

  deleteByUsername(username: string) {
    return this.http.delete(`${this.baseUrl}/admin/delete/${username}`,
      {
        headers: this.createHeaders()
      }
    );
  }

  addRoleAdmin(username: string): Observable<Usuario> {
    return this.http.put<Usuario>(`${this.baseUrl}/admin/addAdmin/${username}`,
      null,
      {
        headers: this.createHeaders()
      }
    );
  }

  removeRoleAdmin(username: string): Observable<Usuario> {
    return this.http.put<Usuario>(`${this.baseUrl}/admin/removeAdmin/${username}`,
      null,
      {
        headers: this.createHeaders()
      }
    );
  }

  disableUser(username: string): Observable<Usuario> {
    return this.http.patch<Usuario>(`${this.baseUrl}/admin/disable/${username}`,
      null,
      {
        headers: this.createHeaders()
      }
    )
  }

  enableUser(username: string): Observable<Usuario> {
    return this.http.patch<Usuario>(`${this.baseUrl}/admin/enable/${username}`,
      null,
      {
        headers: this.createHeaders()
      }
    )
  }

  registrarUsuario(createUsuario: CreateUsuario, role: string) {
    return this.http.post(`${this.baseUrl}/admin/auth/register?userRole=${role}`,
      createUsuario,
      {
        headers: this.createHeaders()
      }
    );
  }

  validarUsuario(validateUsuario: ValidateUsuario) {
    return this.http.put(`${this.baseUrl}/auth/validate`,
      validateUsuario
    );
  }

  me(): Observable<Usuario> {
    return this.http.get<Usuario>(`${this.baseUrl}/me`,
      { headers: this.createHeaders() }
    );
  }
  
  private createHeaders(): HttpHeaders {
    return new HttpHeaders(
      {
        "Authorization": `Bearer ${localStorage.getItem("token")}`
      }
    );
  }
}
