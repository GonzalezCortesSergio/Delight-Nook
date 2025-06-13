import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CategoriaDetails, CategoriaResponse, CreateCategoriaHija } from '../models/categoria';

@Injectable({
  providedIn: 'root'
})
export class CategoriaService {

  constructor(private http: HttpClient) { }

  private baseUrl = "http://localhost:8080/api/categoria";


  findAll(page: number): Observable<CategoriaResponse> {
    return this.http.get<CategoriaResponse>(`${this.baseUrl}?page=${page}&size=6`,
      {
        headers: this.createHeaders()
      }
    );
  }

  findById(id: number): Observable<CategoriaDetails> {
    return this.http.get<CategoriaDetails>(`${this.baseUrl}/${id}`,
      {
        headers: this.createHeaders()
      }
    );
  }

  createCategoria(nombre: string) {
    return this.http.post(`${this.baseUrl}/admin/crear/${nombre}`,
      null,
      {
        headers: this.createHeaders()
      }
    );
  }

  createCategoriaHija(createCategoriaHija: CreateCategoriaHija) {
    return this.http.post(`${this.baseUrl}/admin/crearHija`,
      createCategoriaHija,
      {
        headers: this.createHeaders()
      }
    );
  }
  
  deleteCategoria(id: number) {
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
  }
}
