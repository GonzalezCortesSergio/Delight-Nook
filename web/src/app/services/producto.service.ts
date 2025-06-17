import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CreateProducto, EditProducto, ProductoCantidad, ProductoDetails, ProductoFilter, ProductoResponse } from '../models/producto';

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

  findAllStock(page: number, productoFilter: ProductoFilter): Observable<ProductoResponse> {
    return this.http.post<ProductoResponse>(`${this.baseUrl}/cajero/list?page=${page}&size=4`,
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

  editProducto(editProducto: EditProducto, id: number) {
    return this.http.put(`${this.baseUrl}/admin/edit/${id}`,
      editProducto,
      {
        headers: this.createHeaders()
      }
    );
  }

  changeImage(file: File, id: number) {
    const formData = new FormData();
    formData.append("image", file);

    return this.http.put(`${this.baseUrl}/admin/editImage/${id}`,
      formData,
      {
        headers: this.createHeaders()
      }
    );
  }

  addProductoToStock(productoCantidad: ProductoCantidad) {
    return this.http.post(`${this.baseUrl}/almacenero/addStock`,
      productoCantidad,
      { headers: this.createHeaders() }
    );
  }

  private createHeaders(): HttpHeaders {
    return new HttpHeaders({
      "Authorization": `Bearer ${localStorage.getItem("token")}`
    });
  }
}
