import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginPageComponent } from './pages/login-page/login-page.component';
import { LoginCajeroPageComponent } from './pages/login-cajero-page/login-cajero-page.component';
import { AdminHomePageComponent } from './pages/admin-home-page/admin-home-page.component';

const routes: Routes = [

  {path: "login", component: LoginPageComponent},
  {path: "cajero/login", component: LoginCajeroPageComponent},
  {path: "admin/home", component: AdminHomePageComponent},
  {path: "", pathMatch: "full", redirectTo: "/login"}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
