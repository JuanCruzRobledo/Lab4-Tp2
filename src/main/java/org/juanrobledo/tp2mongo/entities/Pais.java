package org.juanrobledo.tp2mongo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pais {

    @Id
    private Integer codigoPais;

    @Column(name = "nombre_pais", nullable = false)
    private String nombrePais;

    @Column(name = "capital_pais", nullable = false)
    private String capitalPais;

    @Column(name = "region", nullable = false)
    private String region;

    @Column(name = "subregion", nullable = false)
    private String subregion;

    @Column(name = "poblacion", nullable = false)
    private Long poblacion;

    @Column(name = "latitud", nullable = false)
    private Double latitud;

    @Column(name = "longitud", nullable = false)
    private Double longitud;
}
