package com.xime.averapizza.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Data
@Entity
@Table(name = "rol")
@NoArgsConstructor
@AllArgsConstructor
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private RolNombre nombre;

    public enum RolNombre {
        ADMIN,
        CAJERO,
        CLIENTE,
        COCINA
    }

//    private String nombre;
//
//    @ManyToMany(mappedBy = "roles")
//    private Set<Usuario> usuarios;

}
