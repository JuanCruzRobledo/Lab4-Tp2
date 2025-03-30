package org.juanrobledo.tp2mongo.repository;

import org.juanrobledo.tp2mongo.entities.Pais;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("paisRepositorySQL")
public interface PaisRepository extends JpaRepository<Pais, Integer> {
}