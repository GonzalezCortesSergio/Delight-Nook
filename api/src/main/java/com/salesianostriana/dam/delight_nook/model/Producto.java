package com.salesianostriana.dam.delight_nook.model;

import com.salesianostriana.dam.delight_nook.util.SearchCriteria;
import jakarta.persistence.*;
import jakarta.persistence.criteria.Fetch;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@SQLDelete(sql = "UPDATE Producto set deleted=true WHERE id=?")
@SQLRestriction("deleted=false")
public class Producto {

    @Id
    @GeneratedValue
    private Long id;

    private String nombre;

    private double precioUnidad;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(columnDefinition = "TEXT")
    private String imagen;

    private boolean deleted;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    private Categoria categoria;

    private String proveedor;

    public static Specification<Producto> filterByCategoria(SearchCriteria criteria) {

        return ((root, query, criteriaBuilder) -> {

            if(criteria.key().equalsIgnoreCase("categoria") && criteria.operation().equalsIgnoreCase(":")) {
                /*Join<Producto, Categoria> joinCategoria = root.join("categoria", JoinType.INNER);
                return criteriaBuilder.like(
                        joinCategoria.get("nombre"), "%" +criteria.value() + "%");
                        */
                root.fetch("categoria");
                return criteriaBuilder.like(
                    root.get("categoria").get("nombre"), "%" + criteria.value().toString() + "%"
                );
            }

            return null;
        });
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Producto producto = (Producto) o;
        return getId() != null && Objects.equals(getId(), producto.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
