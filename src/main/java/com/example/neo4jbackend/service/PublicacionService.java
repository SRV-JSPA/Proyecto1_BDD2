package com.example.neo4jbackend.service;

import com.example.neo4jbackend.dto.PublicacionDTO;
import com.example.neo4jbackend.model.Persona;
import com.example.neo4jbackend.model.Publicacion;
import com.example.neo4jbackend.repository.PersonaRepository;
import com.example.neo4jbackend.repository.PublicacionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PublicacionService {
    private final PublicacionRepository publicacionRepository;
    private final PersonaRepository personaRepository;

    public PublicacionService(PublicacionRepository publicacionRepository, PersonaRepository personaRepository) {
        this.publicacionRepository = publicacionRepository;
        this.personaRepository = personaRepository;
    }

    public boolean crearPublicacion(PublicacionDTO publicacionDTO) {
        Optional<Persona> autorOpt = personaRepository.findByUsername(publicacionDTO.getUsernameAutor()).stream().findFirst();
        if (autorOpt.isEmpty()) return false;

        Publicacion publicacion = new Publicacion(
                publicacionDTO.getContenido(),
                publicacionDTO.getFechaPublicacion(),
                publicacionDTO.getImagenUrl(),
                publicacionDTO.getEtiquetas(),
                0,
                0,
                autorOpt.get()
        );
        publicacionRepository.save(publicacion);
        return true;
    }

    public List<Publicacion> obtenerPublicaciones() {
        return publicacionRepository.findAll();
    }

    public Optional<Publicacion> obtenerPublicacionPorId(Long id) {
        return publicacionRepository.findById(id);
    }

    public boolean actualizarPublicacion(Long id, PublicacionDTO publicacionDTO) {
        Optional<Publicacion> publicacionOpt = publicacionRepository.findById(id);
        if (publicacionOpt.isEmpty()) return false;

        Publicacion publicacion = publicacionOpt.get();
        publicacion.setContenido(publicacionDTO.getContenido());
        publicacion.setFechaPublicacion(publicacionDTO.getFechaPublicacion());
        publicacion.setImagenUrl(publicacionDTO.getImagenUrl());
        publicacion.setEtiquetas(publicacionDTO.getEtiquetas());

        publicacionRepository.save(publicacion);
        return true;
    }

    public boolean eliminarPublicacion(Long id) {
        Optional<Publicacion> publicacionOpt = publicacionRepository.findById(id);
        if (publicacionOpt.isEmpty()) return false;

        publicacionRepository.delete(publicacionOpt.get());
        return true;
    }

    public boolean darLike(String username, Long idPublicacion) {
        Optional<Persona> personaOpt = personaRepository.findByUsername(username).stream().findFirst();
        Optional<Publicacion> publicacionOpt = publicacionRepository.findById(idPublicacion);

        if (personaOpt.isPresent() && publicacionOpt.isPresent()) {
            Persona persona = personaOpt.get();
            Publicacion publicacion = publicacionOpt.get();

            if (!publicacion.getLikes().contains(persona)) {
                publicacion.getLikes().add(persona);
                publicacion.setNumLikes(publicacion.getNumLikes() + 1);
                publicacionRepository.save(publicacion);
                return true;
            }
        }
        return false;
    }
}
