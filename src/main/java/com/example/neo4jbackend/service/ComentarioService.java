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

import java.util.List;
import java.util.Optional;

@Service
public class ComentarioService {
    private final ComentarioRepository comentarioRepository;
    private final PersonaRepository personaRepository;
    private final PublicacionRepository publicacionRepository;

    public ComentarioService(ComentarioRepository comentarioRepository, PersonaRepository personaRepository, PublicacionRepository publicacionRepository) {
        this.comentarioRepository = comentarioRepository;
        this.personaRepository = personaRepository;
        this.publicacionRepository = publicacionRepository;
    }

    public void crearComentario(ComentarioDTO comentarioDTO) {
        if (comentarioDTO.getContenido() == null || comentarioDTO.getContenido().trim().isEmpty()) {
            throw new IllegalArgumentException("El comentario no puede estar vacío.");
        }
        if (comentarioDTO.getContenido().length() > 1000) {
            throw new IllegalArgumentException("El comentario no puede superar los 1000 caracteres.");
        }
        
        Optional<Persona> autorOpt = personaRepository.findByUsername(comentarioDTO.getUsernameAutor()).stream().findFirst();
        Optional<Publicacion> publicacionOpt = publicacionRepository.findById(comentarioDTO.getIdPublicacion());

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

    public List<Comentario> obtenerTodosLosComentarios() {
        return comentarioRepository.findAll();
    }
    
    public Optional<Comentario> obtenerComentarioPorId(Long id) {
        return comentarioRepository.findById(id);
    }
}
