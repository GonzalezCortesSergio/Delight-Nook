export interface CajaResponse {
    content: Caja[];
    size: number;
    totalElements: number;
}

export interface Caja {
    id: number;
    nombre: string;
    dineroCaja: number;
}