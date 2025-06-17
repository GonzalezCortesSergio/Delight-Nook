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
import { RegisterProductoPageComponent } from './pages/admin/register-producto-page/register-producto-page.component';
import { EditProductoPageComponent } from './pages/admin/edit-producto-page/edit-producto-page.component';
import { VentaPageComponent } from './pages/cajero/venta-page/venta-page.component';
import { roleGuard } from './role.guard';
import { AccessDeniedPageComponent } from './pages/access-denied-page/access-denied-page.component';
import { ProfileDetailsPageComponent } from './pages/cajero/profile-details-page/profile-details-page.component';
import { CajaVentasPageComponent } from './pages/cajero/caja-ventas-page/caja-ventas-page.component';
import { GestionAlmacenPageComponent } from './pages/almacenero/gestion-almacen-page/gestion-almacen-page.component';
import { ProfileDetailsAlmaceneroPageComponent } from './pages/almacenero/profile-details-almacenero-page/profile-details-almacenero-page.component';


const routes: Routes = [

  { path: "login", component: LoginPageComponent },
  { path: "login/cajero", component: LoginCajeroPageComponent },
  { path: "usuario/validar", component: VerifyUserPageComponent },
  { path: "access-denied", component: AccessDeniedPageComponent },
  {
    path: "admin",
    canActivate: [roleGuard],
    data: { roles: ["ADMIN"] },
    children: [
      { path: "home", component: AdminHomePageComponent },
      { path: "usuarios", component: UsuariosListPageComponent },
      { path: "cajas", component: CajasListPageComponent },
      { path: "categorias", component: CategoriasListPageComponent },
      { path: "productos", component: ProductosListPageComponent },
      { path: "categorias/detalles/:id", component: CategoriaDetailsPageComponent },
      { path: "cajas/detalles/:id", component: CajaDetailsPageComponent },
      { path: "productos/detalles/:id", component: ProductoDetailsPageComponent },
      { path: "alta/usuario", component: RegisterUsuarioPageComponent },
      { path: "alta/producto", component: RegisterProductoPageComponent },
      { path: "editar/producto/:id", component: EditProductoPageComponent }
    ]
  }
  ,
  {
    path: "cajero",
    canActivate: [roleGuard],
    data: {roles: ["CAJERO"]},
    children: [
      { path: "venta", component: VentaPageComponent },
      { path: "caja", component: CajaVentasPageComponent },
      { path: "perfil", component: ProfileDetailsPageComponent }
    ]
  },
  {
    path: "almacenero",
    canActivate: [roleGuard],
    data: {roles: ["ALMACENERO"]},
    children: [
      { path: "almacen", component: GestionAlmacenPageComponent },
      { path: "perfil", component: ProfileDetailsAlmaceneroPageComponent }
    ]
  },
  { path: "", pathMatch: "full", redirectTo: "/login" }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
