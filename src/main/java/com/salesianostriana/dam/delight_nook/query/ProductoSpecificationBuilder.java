package com.salesianostriana.dam.delight_nook.query;

import com.salesianostriana.dam.delight_nook.model.Producto;
import com.salesianostriana.dam.delight_nook.util.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ProductoSpecificationBuilder
extends GenericSpecificationBuilder<Producto>{

    public ProductoSpecificationBuilder (List<SearchCriteria> params) {
        super(params);
    }


    @Override
    public Specification<Producto> build() {

        Specification<Producto> result;

        if (params.isEmpty())
            return null;

        SearchCriteria first = params.get(0);

        if (first.key().equalsIgnoreCase("categoria"))
            result = Producto.filterByCategoria(first);
        else
            result = super.build(first);

        for (int i = 1; i < params.size(); i++) {
            if (params.get(i).key().equalsIgnoreCase("categoria"))
                result = result.and(Producto.filterByCategoria(params.get(i)));
            else
                result = super.build(params.get(i));

            //result = result.and(Producto.filterByCategoria(params.get(i)));
        }

        return result;
    }

}
