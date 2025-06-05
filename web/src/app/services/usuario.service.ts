import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LoginRequest, Usuario, UsuarioResponse } from '../models/usuario';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  constructor(private http: HttpClient) { }

  private baseUrl = "http://localhost:8080/api/usuario";

  //Inicio de sesión para admin y para almaceneros

  iniciarSesion(loginRequest: LoginRequest): Observable<Usuario> {
    return this.http.post<Usuario>(`${this.baseUrl}/auth/login`,
      loginRequest
    );
  }


  refreshToken(): Observable<Usuario> {
    return this.http.post<Usuario>(`${this.baseUrl}/auth/refresh/token`,
      {
        "refreshToken": localStorage.getItem("refreshToken")
      }
    );
  }

  findAll(page: number): Observable<UsuarioResponse> {
    return this.http.get<UsuarioResponse>(`${this.baseUrl}/admin/listado?size=6&page=${page}`,
      {
        headers: {
          "Authorization": `Bearer ${localStorage.getItem("token")}`
        }
      }
    );
  }
  cerrarSesion() {
    return this.http.get(`${this.baseUrl}/auth/logout`,
      {
        headers: {
          "Authorization": `Bearer ${localStorage.getItem("token")}`
        }
      }
    );
  }
}
