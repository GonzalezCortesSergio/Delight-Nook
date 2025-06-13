package com.salesianostriana.dam.delight_nook.query;

import com.salesianostriana.dam.delight_nook.model.Producto;
import com.salesianostriana.dam.delight_nook.security.validation.annotation.MinNotHigherThanMax;
import jakarta.persistence.criteria.Predicate;
import jakarta.validation.constraints.DecimalMin;
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

    @DecimalMin("0.01")
    private Double precioMin;

    @DecimalMin("0.01")
    private Double precioMax;

    public Specification<Producto> obtainFilterSpecification() {
        return ((root, query, cb) -> {

            Predicate predicate = cb.and();

            if(StringUtils.hasText(this.nombre)) {
                predicate = cb.and(predicate,
                        cb.like(root.get("nombre"), this.nombre + "%"));
            }

            if(StringUtils.hasText(this.categoria)) {
                root.fetch("categoria");
                predicate = cb.and(predicate,
                        cb.like(root.get("categoria").get("nombre"), this.categoria + "%"));
            }

            if(StringUtils.hasText(this.proveedor)) {
                predicate = cb.and(predicate,
                        cb.like(root.get("proveedor"), this.proveedor + "%"));
            }

            if(precioMin != null && precioMin != 0) {
                predicate = cb.and(predicate,
                        cb.greaterThanOrEqualTo(root.get("precioUnidad"), this.precioMin));
            }

            if(precioMax != null && precioMax != 0) {
                predicate = cb.and(predicate,
                        cb.lessThanOrEqualTo(root.get("precioUnidad"), this.precioMax));
            }

            return predicate;
        });
    }
}
