import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CajaResponse } from '../models/caja';
import { LoginRequest, Usuario } from '../models/usuario';

@Injectable({
  providedIn: 'root'
})
export class CajaService {

  constructor(private http: HttpClient) { }

  private baseUrl = "http://localhost:8080/api/caja";


  findAll(page: number): Observable<CajaResponse> {
    return this.http.get<CajaResponse>(`${this.baseUrl}/admin/listar?page=${page}`,
      {
        headers: {
          "Authorization": `Bearer ${localStorage.getItem("token")}`
        }
      }
    );
  }

  iniciarSesionCaja(loginRequest: LoginRequest, id: number): Observable<Usuario> {
    return this.http.post<Usuario>(`${this.baseUrl}/${id}/login`,
      loginRequest
    );
  }
}
