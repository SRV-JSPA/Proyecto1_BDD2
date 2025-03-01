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

    public void crearGrupo(GrupoDTO grupoDTO) {
        if (grupoDTO.getDescripcion().length() > 500) {
            throw new RuntimeException("La descripción del grupo no puede superar los 500 caracteres");
        }

        if (!grupoDTO.getPrivacidad().equalsIgnoreCase("publico") && !grupoDTO.getPrivacidad().equalsIgnoreCase("privado")) {
            throw new RuntimeException("Tipo de privacidad no válido. Debe ser 'publico' o 'privado'");
        }

        Optional<Persona> creadorOpt = personaRepository.findByUsername(grupoDTO.getUsernameCreador()).stream().findFirst();

        if (creadorOpt.isPresent()) {
            Persona creador = creadorOpt.get();
            Grupo grupo = new Grupo(
                    grupoDTO.getNombre(),
                    grupoDTO.getDescripcion(),
                    grupoDTO.getFechaCreacion(),
                    grupoDTO.getPrivacidad(),
                    creador
            );
            grupo.getModeradores().add(creador);
            grupoRepository.save(grupo);
        } else {
            throw new RuntimeException("Creador no encontrado");
        }
    }

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

    public void actualizarGrupo(String nombre, GrupoDTO grupoDTO) {
        Optional<Grupo> grupoOpt = grupoRepository.findByNombre(nombre).stream().findFirst();
        
        if (grupoOpt.isPresent()) {
            Grupo grupo = grupoOpt.get();
            
            if (grupoDTO.getDescripcion().length() > 500) {
                throw new RuntimeException("La descripción del grupo no puede superar los 500 caracteres");
            }
            
            if (!grupoDTO.getPrivacidad().equalsIgnoreCase("publico") && !grupoDTO.getPrivacidad().equalsIgnoreCase("privado")) {
                throw new RuntimeException("Tipo de privacidad no válido. Debe ser 'publico' o 'privado'");
            }
            
            grupo.setDescripcion(grupoDTO.getDescripcion());
            grupo.setPrivacidad(grupoDTO.getPrivacidad());
            
            grupoRepository.save(grupo);
        } else {
            throw new RuntimeException("Grupo no encontrado");
        }
    }

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

            if (grupo.getPrivacidad().equalsIgnoreCase("privado")) {
                throw new RuntimeException("No puedes unirte a un grupo privado sin invitación o aprobación");
            }

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
    public boolean agregarMiembro(String nombreGrupo, String username) {
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
    public boolean removerMiembro(String nombreGrupo, String username) {
        Optional<Grupo> grupoOpt = grupoRepository.findByNombre(nombreGrupo).stream().findFirst();

        if (grupoOpt.isPresent()) {
            Grupo grupo = grupoOpt.get();
            grupo.getMiembros().removeIf(persona -> persona.getUsername().equals(username));
            grupoRepository.save(grupo);
            return true;
        }
        return false;
    }
    public boolean agregarModerador(String nombreGrupo, String username) {
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
    public boolean removerModerador(String nombreGrupo, String username) {
        Optional<Grupo> grupoOpt = grupoRepository.findByNombre(nombreGrupo).stream().findFirst();

        if (grupoOpt.isPresent()) {
            Grupo grupo = grupoOpt.get();
            grupo.getModeradores().removeIf(persona -> persona.getUsername().equals(username));
            grupoRepository.save(grupo);
            return true;
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
