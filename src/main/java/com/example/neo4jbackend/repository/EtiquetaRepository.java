package com.example.neo4jbackend.repository;

import com.example.neo4jbackend.model.Etiqueta;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import java.util.Optional;

public interface EtiquetaRepository extends Neo4jRepository<Etiqueta, String> {
    Optional<Etiqueta> findByNombre(String nombre);
}
