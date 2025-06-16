import { Component, inject, Input } from '@angular/core';
import { ProductoService } from '../../../services/producto.service';
import { UsuarioService } from '../../../services/usuario.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ErrorResponse } from '../../../models/error';

@Component({
  selector: 'app-modal-change-image-producto',
  templateUrl: './modal-change-image-producto.component.html',
  styleUrl: './modal-change-image-producto.component.css'
})
export class ModalChangeImageProductoComponent {

  constructor(private productoService: ProductoService, private usuarioService: UsuarioService) { }

  @Input()
  id: number | null = null;

  selectedFile: File | null = null;

  errorMessage: string | null = null;

  private activeModal = inject(NgbActiveModal);

  changeFile(event: Event) {
    const input = event.target as HTMLInputElement;

    if(input?.files?.length) {
      this.selectedFile = input.files[0];

      console.log(this.selectedFile);
    }
  }

  submitImage() {
    if(this.fileValid()) {
      this.productoService.changeImage(this.selectedFile!, this.id!)
      .subscribe({
        next: () => {
          this.activeModal.close();
        },
        error: err => {
          const errorResponse: ErrorResponse = err.error;

          if(errorResponse.status == 401) {
            this.usuarioService.refreshToken(() => this.submitImage());
          }
        }
      })
    }

    else {
      this.errorMessage = "El formato de la imagen no es válido"
    }
  }

  private fileValid(): boolean {

    const validTypes = ["image/jpeg", "image/png", "image/webp"];

    return this.selectedFile != null && validTypes.includes(this.selectedFile.type);
    
  }
}
