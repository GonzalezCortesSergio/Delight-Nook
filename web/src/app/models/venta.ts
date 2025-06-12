import { Page } from "./page.interface";

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