import { Page } from "./page.interface";

export interface CategoriaResponse {
    content: Categoria[];
    page: Page;
}

export interface Categoria {
    id: number;
    nombre: string;
}

export interface CategoriaDetails {
    categoria: Categoria;
    categoriasHijas: Categoria[];
}