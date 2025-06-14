package com.salesianostriana.dam.delight_nook.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Venta {

    @Id
    @GeneratedValue
    private UUID id;

    private LocalDateTime fechaVenta;

    private String nombreCajero;

    @OneToMany(fetch = FetchType.LAZY,
    mappedBy = "venta",
    cascade = CascadeType.ALL,
    orphanRemoval = true)
    @Builder.Default
    private List<LineaVenta> lineasVenta = new ArrayList<>();

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    private Caja caja;

    private boolean finalizada;

    public double getPrecioFinal() {
        return lineasVenta.stream()
                .mapToDouble(LineaVenta::getSubTotal)
                .sum();
    }

    public void addLineaVenta(LineaVenta lv) {
        lv.setVenta(this);
        lineasVenta.add(lv);
    }

    @Override
    public final boolean equals(Object object) {
        if (this == object) return true;
        if (object == null) return false;
        Class<?> oEffectiveClass = object instanceof HibernateProxy ? ((HibernateProxy) object).getHibernateLazyInitializer().getPersistentClass() : object.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Venta venta = (Venta) object;
        return getId() != null && Objects.equals(getId(), venta.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
