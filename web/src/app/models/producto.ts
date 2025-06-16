import { Categoria } from "./categoria";
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

export interface ProductoDetails {
  nombre: string;
  precioUnidad: number;
  descripcion: string;
  categoria: Categoria;
  proveedor: string;
  imagen: string;
}

export class CreateProducto {
  private nombre: string;
  private precioUnidad: number;
  private descripcion: string;
  private categoriaId: number;
  private proveedor: string;

  constructor(nombre: string, precioUnidad: number, descripcion: string, categoriaId: number, proveedor: string) {
    this.nombre = nombre;
    this.precioUnidad = precioUnidad;
    this.descripcion = descripcion;
    this.categoriaId = categoriaId;
    this.proveedor = proveedor;
  }
}

export class EditProducto {
  private precioUnidad: number;
  private descripcion: string;
  private idCategoria: number;
  private proveedor: string;

  constructor(precioUnidad: number, descripcion: string, idCategoria: number, proveedor: string) {
    this.precioUnidad = precioUnidad;
    this.descripcion = descripcion;
    this.idCategoria = idCategoria;
    this.proveedor = proveedor;
  }
}