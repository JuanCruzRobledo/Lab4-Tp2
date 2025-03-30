package org.juanrobledo.tp2mongo.repository;

import org.juanrobledo.tp2mongo.entities.Pais;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface PaisRepository extends MongoRepository<Pais, String> {
    // 5.1 - Países de Americas
    List<Pais> findByRegion(String region);

    // 5.2 - Países de Americas con población > 100M
    List<Pais> findByRegionAndPoblacionGreaterThan(String region, long poblacion);

    // 5.3 - Países cuya región no es Africa ($ne)
    List<Pais> findByRegionNot(String region);

    // 5.4 - Actualizar Egipto se hace desde el servicio con save()

    // 5.5 - Eliminar país por código
    void deleteByCodigoPais(String codigoPais);

    // 5.7 - Países con población entre 50M y 150M
    List<Pais> findByPoblacionBetween(long min, long max);

    // 5.8 - Ordenar países por nombre ascendente
    List<Pais> findAllByOrderByNombrePaisAsc();

    // 5.9 - Skip no es directo en MongoRepository, se gestiona desde el servicio

    // 5.10 - Usando expresiones regulares (LIKE de SQL)
    @Query("{ 'nombrePais': { $regex: ?0, $options: 'i' } }")
    List<Pais> findByNombrePaisRegex(String regex);
}