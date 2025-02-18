package com.example.neo4jbackend.service;

import com.example.neo4jbackend.dto.ComentarioDTO;
import com.example.neo4jbackend.model.Comentario;
import com.example.neo4jbackend.model.Persona;
import com.example.neo4jbackend.model.Publicacion;
import com.example.neo4jbackend.repository.ComentarioRepository;
import com.example.neo4jbackend.repository.PersonaRepository;
import com.example.neo4jbackend.repository.PublicacionRepository;
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
        Optional<Persona> autorOpt = personaRepository.findByUsername(comentarioDTO.getUsernameAutor()).stream().findFirst();
        Optional<Publicacion> publicacionOpt = publicacionRepository.findById(comentarioDTO.getIdPublicacion());

        if (autorOpt.isPresent() && publicacionOpt.isPresent()) {
            Comentario comentario = new Comentario(
                    comentarioDTO.getContenido(),
                    comentarioDTO.getFechaComentario(),
                    0,  // Número de likes inicia en 0
                    autorOpt.get(),
                    publicacionOpt.get()
            );
            comentarioRepository.save(comentario);
        }
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
    
}
