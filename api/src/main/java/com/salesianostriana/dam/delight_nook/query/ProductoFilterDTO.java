package com.salesianostriana.dam.delight_nook.query;

import com.salesianostriana.dam.delight_nook.model.Categoria;
import com.salesianostriana.dam.delight_nook.model.Producto;
import com.salesianostriana.dam.delight_nook.security.validation.annotation.MinNotHigherThanMax;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

@Getter
@Setter
@MinNotHigherThanMax
public class ProductoFilterDTO {

    private String nombre;

    private String categoria;

    private String proveedor;

    private Double precioMin;
  
    private Double precioMax;

    public Specification<Producto> obtainFilterSpecification() {
        return ((root, query, cb) -> {

            Predicate predicate = cb.and();

            if(StringUtils.hasText(this.nombre)) {
                Expression<String> nameToLowerCase = cb.lower(root.get("nombre"));
                predicate = cb.and(predicate,
                        cb.like(nameToLowerCase, "%".concat(this.nombre.toLowerCase()).concat("%")));
            }

            if(StringUtils.hasText(this.categoria)) {
                Join<Producto, Categoria> joinCategoria = root.join("categoria");
                Expression<String> categoriaNameToLowerCase = cb.lower(joinCategoria.get("nombre"));
                predicate = cb.and(predicate,
                        cb.like(categoriaNameToLowerCase, "%".concat(this.categoria.toLowerCase()).concat("%")));
            }

            if(StringUtils.hasText(this.proveedor)) {
                Expression<String> proveedorToLowerCase = cb.lower(root.get("proveedor"));
                predicate = cb.and(predicate,
                        cb.like(proveedorToLowerCase, "%".concat(this.proveedor.toLowerCase()).concat("%")));
            }

            if(precioMin != null && precioMin > 0) {
                predicate = cb.and(predicate,
                        cb.greaterThanOrEqualTo(root.get("precioUnidad"), this.precioMin));
            }

            if(precioMax != null && precioMax > 0) {
                predicate = cb.and(predicate,
                        cb.lessThanOrEqualTo(root.get("precioUnidad"), this.precioMax));
            }

            return predicate;
        });
    }
}
