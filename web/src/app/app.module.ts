import { LOCALE_ID, NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { LoginPageComponent } from './pages/login-page/login-page.component';
import { provideHttpClient, withFetch } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { LoginCajeroPageComponent } from './pages/login-cajero-page/login-cajero-page.component';
import { AdminHomePageComponent } from './pages/admin/admin-home-page/admin-home-page.component';
import { UsuariosListPageComponent } from './pages/admin/usuarios-list-page/usuarios-list-page.component';
import { ModalDeleteUserComponent } from './components/admin/modal-delete-user/modal-delete-user.component';
import { ModalDetailsUserComponent } from './components/admin/modal-details-user/modal-details-user.component';
import { RegisterUsuarioPageComponent } from './pages/admin/register-usuario-page/register-usuario-page.component';
import { CajasListPageComponent } from './pages/admin/cajas-list-page/cajas-list-page.component';
import { ModalDeleteCajaComponent } from './components/admin/modal-delete-caja/modal-delete-caja.component';
import { ModalEditCajaComponent } from './components/admin/modal-edit-caja/modal-edit-caja.component';
import { VerifyUserPageComponent } from './pages/verify-user-page/verify-user-page.component';
import { CajaDetailsPageComponent } from './pages/caja-details-page/caja-details-page.component';
import { ModalCreateCajaComponent } from './components/admin/modal-create-caja/modal-create-caja.component';
import { ModalDetailsVentaComponent } from './components/admin/modal-details-venta/modal-details-venta.component';


@NgModule({
  declarations: [
    AppComponent,
    LoginPageComponent,
    LoginCajeroPageComponent,
    AdminHomePageComponent,
    UsuariosListPageComponent,
    ModalDeleteUserComponent,
    ModalDetailsUserComponent,
    RegisterUsuarioPageComponent,
    CajasListPageComponent,
    ModalDeleteCajaComponent,
    ModalEditCajaComponent,
    VerifyUserPageComponent,
    CajaDetailsPageComponent,
    ModalCreateCajaComponent,
    ModalDetailsVentaComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    FormsModule
  ],
  providers: [
    provideClientHydration(),
    provideHttpClient(withFetch())
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
