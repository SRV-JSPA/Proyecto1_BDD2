package com.example.neo4jbackend.service;

import com.example.neo4jbackend.dto.GrupoDTO;
import com.example.neo4jbackend.model.Grupo;
import com.example.neo4jbackend.model.Persona;
import com.example.neo4jbackend.repository.GrupoRepository;
import com.example.neo4jbackend.repository.PersonaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GrupoService {
    private final GrupoRepository grupoRepository;
    private final PersonaRepository personaRepository;

    public GrupoService(GrupoRepository grupoRepository, PersonaRepository personaRepository) {
        this.grupoRepository = grupoRepository;
        this.personaRepository = personaRepository;
    }

    //C
    public void crearGrupo(GrupoDTO grupoDTO) {
        Optional<Persona> creadorOpt = personaRepository.findByUsername(grupoDTO.getUsernameCreador()).stream().findFirst();

        if (creadorOpt.isPresent()) {
            Grupo grupo = new Grupo(
                    grupoDTO.getNombre(),
                    grupoDTO.getDescripcion(),
                    grupoDTO.getFechaCreacion(),
                    grupoDTO.getPrivacidad(),
                    creadorOpt.get()
            );
            grupoRepository.save(grupo);
        } else {
            throw new RuntimeException("Creador no encontrado");
        }
    }

    //R
    public List<GrupoDTO> obtenerGrupos() {
        return grupoRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public GrupoDTO obtenerGrupoPorNombre(String nombre) {
        return grupoRepository.findByNombre(nombre).stream()
                .findFirst()
                .map(this::convertirADTO)
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado"));
    }

    //U
    public void actualizarGrupo(String nombre, GrupoDTO grupoDTO) {
        Optional<Grupo> grupoOpt = grupoRepository.findByNombre(nombre).stream().findFirst();
        
        if (grupoOpt.isPresent()) {
            Grupo grupo = grupoOpt.get();
            grupo.setDescripcion(grupoDTO.getDescripcion());
            grupo.setPrivacidad(grupoDTO.getPrivacidad());
            
            grupoRepository.save(grupo);
        } else {
            throw new RuntimeException("Grupo no encontrado");
        }
    }

    //D
    public void eliminarGrupo(String nombre) {
        Optional<Grupo> grupoOpt = grupoRepository.findByNombre(nombre).stream().findFirst();
        
        if (grupoOpt.isPresent()) {
            grupoRepository.delete(grupoOpt.get());
        } else {
            throw new RuntimeException("Grupo no encontrado");
        }
    }

    public boolean unirseAGrupo(String username, String nombreGrupo) {
        Optional<Persona> personaOpt = personaRepository.findByUsername(username).stream().findFirst();
        Optional<Grupo> grupoOpt = grupoRepository.findByNombre(nombreGrupo).stream().findFirst();

        if (personaOpt.isPresent() && grupoOpt.isPresent()) {
            Persona persona = personaOpt.get();
            Grupo grupo = grupoOpt.get();

            if (!grupo.getMiembros().contains(persona)) {
                grupo.getMiembros().add(persona);
                grupoRepository.save(grupo);
                return true;
            }
        }
        return false;
    }

    public boolean moderarGrupo(String username, String nombreGrupo) {
        Optional<Persona> personaOpt = personaRepository.findByUsername(username).stream().findFirst();
        Optional<Grupo> grupoOpt = grupoRepository.findByNombre(nombreGrupo).stream().findFirst();

        if (personaOpt.isPresent() && grupoOpt.isPresent()) {
            Persona persona = personaOpt.get();
            Grupo grupo = grupoOpt.get();

            if (!grupo.getModeradores().contains(persona)) {
                grupo.getModeradores().add(persona);
                grupoRepository.save(grupo);
                return true;
            }
        }
        return false;
    }
    private GrupoDTO convertirADTO(Grupo grupo) {
        return new GrupoDTO(
            grupo.getNombre(),
            grupo.getDescripcion(),
            grupo.getFechaCreacion(),
            grupo.getPrivacidad(),
            grupo.getCreador().getUsername()
        );
    }
}