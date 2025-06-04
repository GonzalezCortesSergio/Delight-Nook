package com.salesianostriana.dam.delight_nook.security.jwt.refresh;

import com.salesianostriana.dam.delight_nook.user.model.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RefreshToken {

    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(nullable = false)
    private Instant expireAt;

    @Builder.Default
    private Instant createdAt = Instant.now();


    public String getToken() {

        return id.toString();
    }
}
