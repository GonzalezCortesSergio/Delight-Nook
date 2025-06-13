import { Page } from "./page.interface";

export interface Producto {
  id: number
  nombre: string
  categoria: string
  precioUnidad: number
}

export interface ProductoResponse {
  content: Producto[];
  page: Page;
}