package com.example.neo4jbackend.repository;

import com.example.neo4jbackend.model.Grupo;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import java.util.List;

public interface GrupoRepository extends Neo4jRepository<Grupo, Long> {
    List<Grupo> findByNombre(String nombre);
}
