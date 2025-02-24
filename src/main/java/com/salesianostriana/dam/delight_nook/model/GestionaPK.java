package com.salesianostriana.dam.delight_nook.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class GestionaPK implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long caja_id;
    private UUID cajero_id;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        GestionaPK that = (GestionaPK) o;
        return getCaja_id() != null && Objects.equals(getCaja_id(), that.getCaja_id())
                && getCajero_id() != null && Objects.equals(getCajero_id(), that.getCajero_id());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(caja_id, cajero_id);
    }
}
