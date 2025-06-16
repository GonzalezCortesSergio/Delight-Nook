import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { VentaDetails, VentasCajaResponse } from '../models/venta';
import { ProductoCantidad } from '../models/producto';

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
    );
  }

  getVentaDetails(id: string): Observable<VentaDetails> {
    return this.http.get<VentaDetails>(`${this.baseUrl}/admin/detalles/${id}`,
      {
        headers: this.createHeaders()
      }
    );
  }

  addProductoToVenta(productoCantidad: ProductoCantidad): Observable<VentaDetails> {
    return this.http.post<VentaDetails>(`${this.baseUrl}/addProducto`,
      productoCantidad,
      {
        headers: this.createHeaders()
      }
    );
  }

  removeLineaVenta(id: string): Observable<VentaDetails> {
    return this.http.put<VentaDetails>(`${this.baseUrl}/removeProducto/${id}`,
      null,
      {
        headers: this.createHeaders()
      }
    );
  }

  finalizarVenta() {
    return this.http.put(`${this.baseUrl}/finalizar`,
      null,
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
}
