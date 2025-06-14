import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginPageComponent } from './pages/login-page/login-page.component';
import { LoginCajeroPageComponent } from './pages/login-cajero-page/login-cajero-page.component';
import { AdminHomePageComponent } from './pages/admin/admin-home-page/admin-home-page.component';
import { UsuariosListPageComponent } from './pages/admin/usuarios-list-page/usuarios-list-page.component';
import { RegisterUsuarioPageComponent } from './pages/admin/register-usuario-page/register-usuario-page.component';
import { CajasListPageComponent } from './pages/admin/cajas-list-page/cajas-list-page.component';
import { VerifyUserPageComponent } from './pages/verify-user-page/verify-user-page.component';
import { CajaDetailsPageComponent } from './pages/admin/caja-details-page/caja-details-page.component';
import { CategoriasListPageComponent } from './pages/admin/categorias-list-page/categorias-list-page.component';
import { CategoriaDetailsPageComponent } from './pages/admin/categoria-details-page/categoria-details-page.component';
import { ProductosListPageComponent } from './pages/admin/productos-list-page/productos-list-page.component';
import { ProductoDetailsPageComponent } from './pages/admin/producto-details-page/producto-details-page.component';


const routes: Routes = [

  {path: "login", component: LoginPageComponent},
  {path: "cajero/login", component: LoginCajeroPageComponent},
  {path: "usuario/validar", component: VerifyUserPageComponent},
  {path: "admin/home", component: AdminHomePageComponent},
  {path: "admin/usuarios", component: UsuariosListPageComponent},
  {path: "admin/cajas", component: CajasListPageComponent},
  {path: "admin/categorias", component: CategoriasListPageComponent},
  {path: "admin/productos", component: ProductosListPageComponent},
  {path: "admin/categorias/detalles/:id", component: CategoriaDetailsPageComponent},
  {path: "admin/cajas/detalles/:id", component: CajaDetailsPageComponent},
  {path: "admin/productos/detalles/:id", component: ProductoDetailsPageComponent},
  {path: "admin/alta/usuario", component: RegisterUsuarioPageComponent},
  {path: "", pathMatch: "full", redirectTo: "/login"}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
