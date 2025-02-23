package com.salesianostriana.dam.delight_nook.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@SQLDelete(sql = "UPDATE Categoria set deleted=true WHERE id=?")
@SQLRestriction("deleted=false")
public class Categoria {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @ManyToOne
    private Categoria categoriaPadre;

    @OneToMany(
            mappedBy = "categoriaPadre",
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST
    )
    @Builder.Default
    private List<Categoria> categoriasHijas = new ArrayList<>();

    private boolean deleted;

    public void addToCategoriaPadre(Categoria categoria) {

        setCategoriaPadre(categoria);
        categoria.getCategoriasHijas().add(this);
    }

    public void removeFromCategoriaPadre(Categoria categoria) {

        categoria.getCategoriasHijas().remove(this);
        setCategoriaPadre(null);
    }
}
