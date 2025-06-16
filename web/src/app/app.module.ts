import { NgModule } from '@angular/core';
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
import { CajaDetailsPageComponent } from './pages/admin/caja-details-page/caja-details-page.component';
import { ModalCreateCajaComponent } from './components/admin/modal-create-caja/modal-create-caja.component';
import { ModalDetailsVentaComponent } from './components/admin/modal-details-venta/modal-details-venta.component';
import { CategoriasListPageComponent } from './pages/admin/categorias-list-page/categorias-list-page.component';
import { CategoriaDetailsPageComponent } from './pages/admin/categoria-details-page/categoria-details-page.component';
import { ModalDeleteCategoriaComponent } from './components/admin/modal-delete-categoria/modal-delete-categoria.component';
import { ModalCreateCategoriaComponent } from './components/admin/modal-create-categoria/modal-create-categoria.component';
import { ProductosListPageComponent } from './pages/admin/productos-list-page/productos-list-page.component';
import { ProductoDetailsPageComponent } from './pages/admin/producto-details-page/producto-details-page.component';
import { ModalDeleteProductoComponent } from './components/admin/modal-delete-producto/modal-delete-producto.component';
import { RegisterProductoPageComponent } from './pages/admin/register-producto-page/register-producto-page.component';
import { EditProductoPageComponent } from './pages/admin/edit-producto-page/edit-producto-page.component';
import { ModalChangeImageProductoComponent } from './components/admin/modal-change-image-producto/modal-change-image-producto.component';
import { NavbarCajeroComponent } from './components/cajero/navbar-cajero/navbar-cajero.component';
import { VentaPageComponent } from './pages/cajero/venta-page/venta-page.component';
import { AccessDeniedPageComponent } from './pages/access-denied-page/access-denied-page.component';
import { ModalDetailsVentaFinalizadaComponent } from './components/cajero/modal-details-venta-finalizada/modal-details-venta-finalizada.component';


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
    ModalDetailsVentaComponent,
    CategoriasListPageComponent,
    CategoriaDetailsPageComponent,
    ModalDeleteCategoriaComponent,
    ModalCreateCategoriaComponent,
    ProductosListPageComponent,
    ProductoDetailsPageComponent,
    ModalDeleteProductoComponent,
    RegisterProductoPageComponent,
    EditProductoPageComponent,
    ModalChangeImageProductoComponent,
    NavbarCajeroComponent,
    VentaPageComponent,
    AccessDeniedPageComponent,
    ModalDetailsVentaFinalizadaComponent
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
