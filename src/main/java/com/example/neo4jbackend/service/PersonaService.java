package com.example.neo4jbackend.service;

import com.example.neo4jbackend.dto.PersonaDTO;
import com.example.neo4jbackend.model.Persona;
import com.example.neo4jbackend.repository.PersonaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonaService {
    private final PersonaRepository personaRepository;

    public PersonaService(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    public boolean crearPersona(PersonaDTO personaDTO) {
        if (personaRepository.findByUsername(personaDTO.getUsername()).stream().findFirst().isPresent()) {
            return false;
        }

        Persona persona = new Persona(
                personaDTO.getNombre(),
                personaDTO.getUsername(),
                personaDTO.getEmail(),
                personaDTO.getPassword(),
                personaDTO.getFechaRegistro(),
                personaDTO.getBiografia(),
                personaDTO.getIntereses(),
                personaDTO.isCuentaVerificada()
        );
        personaRepository.save(persona);
        return true;
    }

    public List<Persona> obtenerPersonas() {
        return personaRepository.findAll();
    }

    public Optional<Persona> obtenerPersonaPorUsername(String username) {
        return personaRepository.findByUsername(username).stream().findFirst();
    }

    public boolean actualizarPersona(String username, PersonaDTO personaDTO) {
        Optional<Persona> personaOpt = obtenerPersonaPorUsername(username);
        if (personaOpt.isEmpty()) return false;

        Persona persona = personaOpt.get();
        persona.setNombre(personaDTO.getNombre());
        persona.setEmail(personaDTO.getEmail());
        persona.setPassword(personaDTO.getPassword());
        persona.setBiografia(personaDTO.getBiografia());
        persona.setIntereses(personaDTO.getIntereses());
        persona.setCuentaVerificada(personaDTO.isCuentaVerificada());
        personaRepository.save(persona);
        return true;
    }

    public boolean eliminarPersona(String username) {
        Optional<Persona> personaOpt = obtenerPersonaPorUsername(username);
        if (personaOpt.isEmpty()) return false;

        personaRepository.delete(personaOpt.get());
        return true;
    }

    public boolean seguirUsuario(String seguidorUsername, String seguidoUsername) {
        Optional<Persona> seguidorOpt = obtenerPersonaPorUsername(seguidorUsername);
        Optional<Persona> seguidoOpt = obtenerPersonaPorUsername(seguidoUsername);

        if (seguidorOpt.isPresent() && seguidoOpt.isPresent()) {
            Persona seguidor = seguidorOpt.get();
            Persona seguido = seguidoOpt.get();

            List<Persona> seguidos = seguidor.getSeguidos();
            if (seguidos.stream().anyMatch(p -> p.getUsername().equals(seguidoUsername))) {
                return false;
            }

            seguidos.add(seguido);
            seguidor.setSeguidos(seguidos);
            personaRepository.save(seguidor);
            return true;
        }
        return false;
    }

    public boolean dejarDeSeguirUsuario(String seguidorUsername, String seguidoUsername) {
        Optional<Persona> seguidorOpt = obtenerPersonaPorUsername(seguidorUsername);
        Optional<Persona> seguidoOpt = obtenerPersonaPorUsername(seguidoUsername);

        if (seguidorOpt.isPresent() && seguidoOpt.isPresent()) {
            Persona seguidor = seguidorOpt.get();
            Persona seguido = seguidoOpt.get();

            List<Persona> seguidos = seguidor.getSeguidos();
            if (seguidos.removeIf(p -> p.getUsername().equals(seguidoUsername))) {
                seguidor.setSeguidos(seguidos);
                personaRepository.save(seguidor);
                return true;
            }
        }
        return false;
    }

    public List<Persona> obtenerSeguidores(String username) {
        return personaRepository.findSeguidores(username);
    }

    public List<Persona> obtenerSeguidos(String username) {
        return personaRepository.findSeguidos(username);
    }
}
