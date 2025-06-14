import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CreateProducto, ProductoDetails, ProductoFilter, ProductoResponse } from '../models/producto';

@Injectable({
  providedIn: 'root'
})
export class ProductoService {

  constructor(private http: HttpClient) { }

  private baseUrl = "http://localhost:8080/api/producto";

  findAll(page: number, productoFilter: ProductoFilter): Observable<ProductoResponse> {
    return this.http.post<ProductoResponse>(`${this.baseUrl}?page=${page}&size=4`,
      productoFilter,
      {
        headers: this.createHeaders()
      }
    );
  }

  findById(id: number): Observable<ProductoDetails> {
    return this.http.get<ProductoDetails>(`${this.baseUrl}/admin/details/${id}`,
      {
        headers: this.createHeaders()
      }
    );
  }

  deleteById(id: number) {
    return this.http.delete(`${this.baseUrl}/admin/borrar/${id}`,
      {
        headers: this.createHeaders()
      }
    );
  }

  createProducto(createProducto: CreateProducto) {
    return this.http.post(`${this.baseUrl}/admin/create`,
      createProducto,
      {
        headers: this.createHeaders()
      }
    );
  }

  private createHeaders(): HttpHeaders {
    return new HttpHeaders({
      "Authorization": `Bearer ${localStorage.getItem("token")}`
    });
  }
}
