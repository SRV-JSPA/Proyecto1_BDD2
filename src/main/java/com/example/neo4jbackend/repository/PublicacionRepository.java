package com.example.neo4jbackend.repository;

import com.example.neo4jbackend.model.Publicacion;
import com.example.neo4jbackend.model.Persona;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import java.util.List;
import java.util.Optional;

public interface PublicacionRepository extends Neo4jRepository<Publicacion, Long> {
    List<Publicacion> findByAutorUsername(String username);
    Optional<Publicacion> findById(Long id);

    @Query("MATCH (p:Publicacion {id: $id}) DETACH DELETE p")
    void deleteById(Long id);

    @Query("MATCH (p:Publicacion)<-[:DA_LIKE]-(u:Persona) WHERE p.id = $id RETURN u")
    List<Persona> findLikesDePublicacion(Long id);

    @Query("MATCH (u:Persona)-[:DA_LIKE]->(p:Publicacion) WHERE u.username = $username RETURN p")
    List<Publicacion> findPublicacionesLikeadasPorUsuario(String username);
}
