import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginPageComponent } from './pages/login-page/login-page.component';
import { LoginCajeroPageComponent } from './pages/login-cajero-page/login-cajero-page.component';
import { AdminHomePageComponent } from './pages/admin/admin-home-page/admin-home-page.component';
import { UsuariosListPageComponent } from './pages/admin/usuarios-list-page/usuarios-list-page.component';
import { RegisterUsuarioPageComponent } from './pages/admin/register-usuario-page/register-usuario-page.component';
import { CajasListPageComponent } from './pages/admin/cajas-list-page/cajas-list-page.component';


const routes: Routes = [

  {path: "login", component: LoginPageComponent},
  {path: "cajero/login", component: LoginCajeroPageComponent},
  {path: "admin/home", component: AdminHomePageComponent},
  {path: "admin/usuarios", component: UsuariosListPageComponent},
  {path: "admin/cajas", component: CajasListPageComponent},
  {path: "admin/alta/usuario", component: RegisterUsuarioPageComponent},
  {path: "", pathMatch: "full", redirectTo: "/login"}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
