export interface VentasCajaResponse {
    result: VentasResponse;
    totalRecaudado: number;
}

export interface VentasResponse {
    content: Venta[];
    totalElements: number;
    size: number;
}

export interface Venta {
    id: string;
    precioFinal: number;
    fechaVenta: string;
}