package com.example.neo4jbackend.repository;

import com.example.neo4jbackend.model.Publicacion;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import java.util.List;

public interface PublicacionRepository extends Neo4jRepository<Publicacion, Long> {
    List<Publicacion> findByAutorUsername(String username);
}