import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Caja, CajaResponse, EditCaja } from '../models/caja';
import { LoginRequest, Usuario } from '../models/usuario';

@Injectable({
  providedIn: 'root'
})
export class CajaService {

  constructor(private http: HttpClient) { }

  private baseUrl = "http://localhost:8080/api/caja";

  private cajaDetails: Caja | null = null;


  findAll(page: number): Observable<CajaResponse> {
    return this.http.get<CajaResponse>(`${this.baseUrl}/admin/listar?size=6&page=${page}`,
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

  editDineroCaja(editCaja: EditCaja) {
    return this.http.put(`${this.baseUrl}/admin/editar`,
      editCaja,
      {
        headers: this.createHeaders()
      }
    );
  }

  private createHeaders(): HttpHeaders {
      return new HttpHeaders(
        {
          "Authorization": `Bearer ${localStorage.getItem("token")}`
        }
      );
  }

  setCajaDetails(caja: Caja) {
    this.cajaDetails = caja;
  }

  getCajaDetails() {
    return this.cajaDetails;
  }
}
