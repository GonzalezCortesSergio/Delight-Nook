import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-access-denied-page',
  templateUrl: './access-denied-page.component.html',
  styleUrl: './access-denied-page.component.css'
})
export class AccessDeniedPageComponent {

  constructor(private router: Router) { }

  role = localStorage.getItem("role");

  redirectTo() {

    switch(this.role) {

      case "ADMIN":
        this.router.navigateByUrl("/admin/home");
        break;

      case "CAJERO":
        this.router.navigateByUrl("/cajero/venta");
        break;

      case "ALMACENERO":
        this.router.navigateByUrl("/almacenero/stock");
        break;

      default:
        this.router.navigateByUrl("/login");
        break;
    }
  }

}
