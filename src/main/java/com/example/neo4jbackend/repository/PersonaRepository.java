package com.example.neo4jbackend.repository;

import com.example.neo4jbackend.model.Persona;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import java.util.List;
import java.util.Optional;

public interface PersonaRepository extends Neo4jRepository<Persona, Long> {
    List<Persona> findByUsername(String username);
    Optional<Persona> findById(Long id);

    @Query("MATCH (p:Persona)-[:SIGUE_A]->(seguido) WHERE p.username = $username RETURN seguido")
    List<Persona> findSeguidos(String username);

    @Query("MATCH (p:Persona)<-[:SIGUE_A]-(seguidor) WHERE p.username = $username RETURN seguidor")
    List<Persona> findSeguidores(String username);
}
