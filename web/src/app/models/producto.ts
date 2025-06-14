import { Page } from "./page.interface";

export interface Producto {
  id: number;
  nombre: string;
  categoria: string;
  precioUnidad: number;
  imagen: string;
}

export interface ProductoResponse {
  content: Producto[];
  page: Page;
}

export class ProductoFilter {
  private nombre: string;
  private categoria: string;
  private proveedor: string;
  private precioMin: number;
  private precioMax: number;


  constructor(nombre: string, categoria: string, proveedor: string, precioMin: number, precioMax: number) {
    this.nombre = nombre;
    this.categoria = categoria;
    this.proveedor = proveedor;
    this.precioMin = precioMin;
    this.precioMax = precioMax;
  }
}