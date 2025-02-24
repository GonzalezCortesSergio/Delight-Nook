package com.salesianostriana.dam.delight_nook.query;

import com.salesianostriana.dam.delight_nook.model.Categoria;
import com.salesianostriana.dam.delight_nook.model.Producto;
import com.salesianostriana.dam.delight_nook.util.SearchCriteria;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ProductoSpecificationBuilder
extends GenericSpecificationBuilder<Producto>{

    public ProductoSpecificationBuilder (List<SearchCriteria> params) {
        super(params);
    }


    @Override
    public Specification<Producto> build() {

        Specification<Producto> result = super.build();



        return result;


    }

    //No hace falta el SetJoin.

    private Specification<Producto> build(SearchCriteria criteria) {

        return ((root, query, criteriaBuilder) -> {

            if(criteria.key().equalsIgnoreCase("categoria") && criteria.operation().equalsIgnoreCase(":")) {

                CriteriaQuery<Producto> criteriaQuery = criteriaBuilder.createQuery(Producto.class);
                Root<Producto> productoRoot = criteriaQuery.from(Producto.class);

                criteriaQuery.select(productoRoot);
                SetJoin<Producto, Categoria> categoria = productoRoot.joinSet("categoria", JoinType.LEFT);

                Predicate nombrePredicate = criteriaBuilder.equal(categoria.get("nombre"), criteria.value());
                productoRoot.fetch(String.valueOf(categoria));

                criteriaQuery.where(nombrePredicate);
            }

            return null;
        });
    }
}
