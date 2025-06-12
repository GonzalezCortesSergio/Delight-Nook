import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Caja, CajaResponse, CreateCaja, EditCaja } from '../models/caja';
import { LoginRequest, Usuario } from '../models/usuario';

@Injectable({
  providedIn: 'root'
})
export class CajaService {

  constructor(private http: HttpClient) { }

  private baseUrl = "http://localhost:8080/api/caja";

  findAll(page: number): Observable<CajaResponse> {
    return this.http.get<CajaResponse>(`${this.baseUrl}/admin/listar?size=6&page=${page}`,
      {
        headers: this.createHeaders()
      }
    );
  }

  findById(id: number): Observable<Caja> {
    return this.http.get<Caja>(`${this.baseUrl}/admin/detalles/${id}`,
      {
        headers: this.createHeaders()
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

  createCaja(createCaja: CreateCaja) {
    return this.http.post(`${this.baseUrl}/admin/crear`,
      createCaja,
      {
        headers: this.createHeaders()
      }
    );
  }

  deleteCaja(id: number) {
    return this.http.delete(`${this.baseUrl}/admin/borrar/${id}`,
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
<<<<<<< HEAD
  }
=======
    }
>>>>>>> develop-web
}
