package com.example.neo4jbackend.repository;

import com.example.neo4jbackend.model.Comentario;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import java.util.List;

public interface ComentarioRepository extends Neo4jRepository<Comentario, Long> {
    List<Comentario> findByPublicacionId(Long idPublicacion);
}
