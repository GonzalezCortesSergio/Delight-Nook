import { Page } from "./page.interface";

export interface CajaResponse {
    content: Caja[];
    page: Page;
}

export interface Caja {
    id: number;
    nombre: string;
    dineroCaja: number;
}

export class EditCaja {
    private id: number;
    private dineroNuevo: number;

    constructor(id: number, dineroNuevo: number) {
        this.id = id;
        this.dineroNuevo = dineroNuevo;
    }
}

export class CreateCaja {
    private nombre: string;
    private dineroCaja: number;

    constructor(nombre: string, dineroCaja: number) {
        this.nombre = nombre;
        this.dineroCaja = dineroCaja;
    }
}