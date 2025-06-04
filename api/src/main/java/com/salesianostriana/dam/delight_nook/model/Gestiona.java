package com.salesianostriana.dam.delight_nook.model;

import com.salesianostriana.dam.delight_nook.user.model.Cajero;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Gestiona {

    @EmbeddedId
    private GestionaPK gestionaPK = new GestionaPK();

    @ManyToOne
    @MapsId("cajero_id")
    @JoinColumn(name = "cajero_id")
    private Cajero cajero;

    @ManyToOne
    @MapsId("caja_id")
    @JoinColumn(name = "caja_id")
    private Caja caja;

    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime fechaGestiona;

    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime fechaDejaGestionar;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Gestiona gestiona = (Gestiona) o;
        return getGestionaPK() != null && Objects.equals(getGestionaPK(), gestiona.getGestionaPK());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(gestionaPK);
    }
}
