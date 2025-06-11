import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { VentasCajaResponse } from '../models/venta';

@Injectable({
  providedIn: 'root'
})
export class VentaService {

  constructor(private http: HttpClient) { }

  private baseUrl = "http://localhost:8080/api/venta";


  getVentasPorCaja(idCaja: number, page: number): Observable<VentasCajaResponse> {
    return this.http.get<VentasCajaResponse>(`${this.baseUrl}/admin/listar/${idCaja}?page=${page}&size=4`,
      {
        headers: this.createHeaders()
      }
    )
  }


  private createHeaders(): HttpHeaders {
    return new HttpHeaders(
      {
        "Authorization": `Bearer ${localStorage.getItem("token")}`
      }
    );
  }
}
