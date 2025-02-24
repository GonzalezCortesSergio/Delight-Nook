package com.salesianostriana.dam.delight_nook.query;

import com.salesianostriana.dam.delight_nook.util.SearchCriteria;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

@AllArgsConstructor
public abstract class GenericSpecificationBuilder<T> {

    protected List<SearchCriteria> params;

    public Specification<T> build() {

        if(params.isEmpty()) {
            return null;
        }

        Specification<T> result = build(params.get(0));

        for (int i = 1; i < params.size(); i++) {

            result = result.and(build(params.get(i)));
        }

        return result;
    }


    private Specification<T> build(SearchCriteria criteria) {

        return ((root, query, criteriaBuilder) -> {

            if(criteria.operation().equalsIgnoreCase(">")) {

                return criteriaBuilder.greaterThanOrEqualTo(
                        root.<String> get(criteria.key()), criteria.value().toString()
                );
            }

            else if (criteria.operation().equalsIgnoreCase("<")) {

                return criteriaBuilder.lessThanOrEqualTo(
                        root.<String> get(criteria.key()), criteria.value().toString()
                );
            }

            else if (criteria.operation().equalsIgnoreCase(":")) {

                if(root.get(criteria.key()).getJavaType() == String.class) {

                    return criteriaBuilder.like(
                            root.<String> get(criteria.key()), "%" + criteria.value() + "%"
                    );
                } else {
                    return criteriaBuilder.equal(root.get(criteria.key()), criteria.value());
                }
            }

            return null;

        });
    }
}
