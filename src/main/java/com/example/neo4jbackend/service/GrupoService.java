package com.example.neo4jbackend.service;

import com.example.neo4jbackend.dto.GrupoDTO;
import com.example.neo4jbackend.model.Grupo;
import com.example.neo4jbackend.model.Persona;
import com.example.neo4jbackend.repository.GrupoRepository;
import com.example.neo4jbackend.repository.PersonaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GrupoService {
    private final GrupoRepository grupoRepository;
    private final PersonaRepository personaRepository;

    public GrupoService(GrupoRepository grupoRepository, PersonaRepository personaRepository) {
        this.grupoRepository = grupoRepository;
        this.personaRepository = personaRepository;
    }

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
        }
    }

    public List<Grupo> obtenerGrupos() {
        return grupoRepository.findAll();
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
}
