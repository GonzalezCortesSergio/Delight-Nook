import { NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { LoginPageComponent } from './pages/login-page/login-page.component';
import { provideHttpClient, withFetch } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { LoginCajeroPageComponent } from './pages/login-cajero-page/login-cajero-page.component';
import { AdminHomePageComponent } from './pages/admin-home-page/admin-home-page.component';
import { UsuariosListPageComponent } from './pages/usuarios-list-page/usuarios-list-page.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginPageComponent,
    LoginCajeroPageComponent,
    AdminHomePageComponent,
    UsuariosListPageComponent
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
