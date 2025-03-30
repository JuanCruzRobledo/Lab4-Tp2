package org.juanrobledo.tp2mongo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "paises")
public class Pais {

    @Id
    private String id;

    @Field("codigoPais")
    private String codigoPais;

    @Field("nombrePais")
    private String nombrePais;

    @Field("capitalPais")
    private String capitalPais;

    @Field("region")
    private String region;

    @Field("poblacion")
    private Long poblacion;

    @Field("coordenadas")
    private Coordenadas coordenadas;

    @Field("superficie")
    private Double superficie;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Coordenadas {
        private Double latitud;
        private Double longitud;

    }
}
