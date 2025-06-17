import { Caja } from "./caja";
import { Page } from "./page.interface";
import { Producto } from "./producto";

export interface VentasCajaResponse {
    result: VentasResponse;
    totalRecaudado: number;
}

export interface VentasResponse {
    content: Venta[];
    page: Page;
}

export interface Venta {
    id: string;
    precioFinal: number;
    fechaVenta: string;
}

export interface VentaDetails {
    id: string;
    nombreCajero: string;
    caja: Caja;
    lineasVenta: LineaVenta[];
    precioFinal: number;
    fechaVenta: string;
}

export interface LineaVenta {
    id: string;
    producto: Producto;    
    cantidad: number;
    subTotal: number;
}

export interface NumVentas {
    numVentas: number;
}