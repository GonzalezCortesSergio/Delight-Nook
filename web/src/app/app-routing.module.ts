import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginPageComponent } from './pages/login-page/login-page.component';
import { LoginCajeroPageComponent } from './pages/login-cajero-page/login-cajero-page.component';

const routes: Routes = [

  {path: "login", component: LoginPageComponent},
  {path: "cajero/login", component: LoginCajeroPageComponent},
  {path: "", pathMatch: "full", redirectTo: "/login"}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
