package com.example.neo4jbackend.service;

import com.example.neo4jbackend.dto.ComentarioDTO;
import com.example.neo4jbackend.model.Comentario;
import com.example.neo4jbackend.model.Persona;
import com.example.neo4jbackend.model.Publicacion;
import com.example.neo4jbackend.repository.ComentarioRepository;
import com.example.neo4jbackend.repository.PersonaRepository;
import com.example.neo4jbackend.repository.PublicacionRepository;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Service;
import org.springframework.data.neo4j.core.Neo4jClient;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ComentarioService {
    private final ComentarioRepository comentarioRepository;
    private final PersonaRepository personaRepository;
    private final PublicacionRepository publicacionRepository;
    private final Neo4jClient neo4jClient;

    public ComentarioService(Neo4jClient neo4jClient, ComentarioRepository comentarioRepository, PersonaRepository personaRepository, PublicacionRepository publicacionRepository) {
        this.comentarioRepository = comentarioRepository;
        this.personaRepository = personaRepository;
        this.publicacionRepository = publicacionRepository;
        this.neo4jClient = neo4jClient;
    }

    public void crearComentario(ComentarioDTO comentarioDTO) {
        if (comentarioDTO.getContenido() == null || comentarioDTO.getContenido().trim().isEmpty()) {
            throw new IllegalArgumentException("El comentario no puede estar vacío.");
        }
        if (comentarioDTO.getContenido().length() > 1000) {
            throw new IllegalArgumentException("El comentario no puede superar los 1000 caracteres.");
        }
        
        Optional<Persona> autorOpt = personaRepository.findByUsername(comentarioDTO.getUsernameAutor()).stream().findFirst();
        Optional<Publicacion> publicacionOpt = publicacionRepository.findById(comentarioDTO.getId());

        if (autorOpt.isEmpty()) {
            throw new IllegalArgumentException("El usuario que intenta comentar no existe.");
        }
        if (publicacionOpt.isEmpty()) {
            throw new IllegalArgumentException("La publicación en la que intenta comentar no existe.");
        }
        
        String contenidoSeguro = Jsoup.clean(comentarioDTO.getContenido(), Safelist.basic());

        Comentario comentario = new Comentario(
                contenidoSeguro,
                comentarioDTO.getFechaComentario(),
                0,
                autorOpt.get(),
                publicacionOpt.get()
        );
        comentarioRepository.save(comentario);
    }

    public boolean actualizarComentario(Long id, ComentarioDTO comentarioDTO) {
        Optional<Comentario> comentarioOpt = comentarioRepository.findById(id);
        if (comentarioOpt.isPresent()) {
            Comentario comentario = comentarioOpt.get();
            if (!comentario.getAutor().getUsername().equals(comentarioDTO.getUsernameAutor())) {
                throw new SecurityException("No puedes editar un comentario que no es tuyo.");
            }
            if (comentarioDTO.getContenido() == null || comentarioDTO.getContenido().trim().isEmpty()) {
                throw new IllegalArgumentException("El comentario no puede estar vacío.");
            }
            if (comentarioDTO.getContenido().length() > 1000) {
                throw new IllegalArgumentException("El comentario no puede superar los 1000 caracteres.");
            }
            
            String contenidoSeguro = Jsoup.clean(comentarioDTO.getContenido(), Safelist.basic());
            comentario.setContenido(contenidoSeguro);
            comentario.setFechaComentario(comentarioDTO.getFechaComentario());
            comentarioRepository.save(comentario);
            return true;
        }
        return false;
    }

    public boolean eliminarComentario(Long id, String username) {
        Optional<Comentario> comentarioOpt = comentarioRepository.findById(id);
        if (comentarioOpt.isPresent()) {
            Comentario comentario = comentarioOpt.get();
            if (!comentario.getAutor().getUsername().equals(username)) {
                throw new SecurityException("No puedes eliminar un comentario que no es tuyo.");
            }
            comentarioRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Comentario> obtenerComentariosDePublicacion(Long idPublicacion) {
        return comentarioRepository.findByPublicacionId(idPublicacion);
    }

    public boolean darLike(String username, Long idComentario) {
        Optional<Persona> personaOpt = personaRepository.findByUsername(username).stream().findFirst();
        Optional<Comentario> comentarioOpt = comentarioRepository.findById(idComentario);
        
        if (personaOpt.isPresent() && comentarioOpt.isPresent()) {
            Persona persona = personaOpt.get();
            Comentario comentario = comentarioOpt.get();
            
            if (!comentario.getLikes().contains(persona)) {
                comentario.getLikes().add(persona);
                comentario.setNumLikes(comentario.getNumLikes() + 1);
                comentarioRepository.save(comentario);
                return true;
            }
        }
        return false;
    }

    public List<ComentarioDTO> obtenerTodosLosComentarios() {
        List<Comentario> comentarios = comentarioRepository.findAll();
        
        return comentarios.stream()
            .map(comentario -> new ComentarioDTO(
                comentario.getId(),
                comentario.getContenido(),
                comentario.getFechaComentario(),
                comentario.getNumLikes(),
                comentario.getAutor().getUsername() ,
                comentario.getPublicacion() != null ? comentario.getPublicacion().getId() : null
            ))
            .toList();
    }
    
    public Optional<ComentarioDTO> obtenerComentarioPorId(Long id) {
        return comentarioRepository.findById(id)
            .map(comentario -> new ComentarioDTO(
                comentario.getId(),
                comentario.getContenido(),
                comentario.getFechaComentario(),
                comentario.getNumLikes(),
                comentario.getAutor() != null ? comentario.getAutor().getUsername() : null, 
                comentario.getPublicacion() != null ? comentario.getPublicacion().getId() : null 
            ));
    }

    public boolean asignarAutor(Long id, String username) {
        Optional<Comentario> comentarioOpt = comentarioRepository.findById(id);
        Optional<Persona> personaOpt = personaRepository.findByUsername(username).stream().findFirst();

        if (comentarioOpt.isPresent() && personaOpt.isPresent()) {
            Comentario comentario = comentarioOpt.get();
            comentario.setAutor(personaOpt.get());
            comentarioRepository.save(comentario);
            return true;
        }
        return false;
    }

    public boolean asignarPublicacion(Long id, Long idPublicacion) {
        Optional<Comentario> comentarioOpt = comentarioRepository.findById(id);
        Optional<Publicacion> publicacionOpt = publicacionRepository.findById(idPublicacion);

        if (comentarioOpt.isPresent() && publicacionOpt.isPresent()) {
            Comentario comentario = comentarioOpt.get();
            comentario.setPublicacion(publicacionOpt.get());
            comentarioRepository.save(comentario);
            return true;
        }
        return false;
    }

    public boolean quitarLike(String username, Long idComentario, String contenidoComentario) {
        Optional<Persona> personaOpt = personaRepository.findByUsername(username).stream().findFirst();
        Optional<Comentario> comentarioOpt = comentarioRepository.findById(idComentario);
        
        if (personaOpt.isEmpty() || comentarioOpt.isEmpty()) {
            return false;  
        }

        Comentario comentario = comentarioOpt.get();

        String cypherQuery = "MATCH (p:Persona)-[r:DA_LIKE]->(c:Comentario) WHERE p.username = $username AND c.contenido = $contenidoComentario DELETE r";
        
        Map<String, Object> params = new HashMap<>();
        params.put("username", username);
        params.put("idComentario", idComentario);  
        params.put("contenidoComentario", contenidoComentario);  
    
        System.out.println("Parámetros: " + params);
        
        try {
            neo4jClient.query(cypherQuery)
                        .bindAll(params)  
                        .run();  
            
            System.out.println("Like eliminado con éxito para usuario: " + username + " y comentario con ID: " + idComentario);
        
        } catch (Exception e) {
            e.printStackTrace();
            return false; 
        }

        comentario.setNumLikes(Math.max(0, comentario.getNumLikes() - 1));
        comentarioRepository.save(comentario);
        
        return true;  
    }
}
