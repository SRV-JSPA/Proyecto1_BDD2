package com.example.neo4jbackend.service;

import com.example.neo4jbackend.dto.PersonaDTO;
import com.example.neo4jbackend.dto.SeguirRequest;
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

    public void crearPersona(PersonaDTO personaDTO) {
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
    }

    public List<Persona> obtenerPersonas() {
        return personaRepository.findAll();
    }

    public Optional<Persona> obtenerPersonaPorUsername(String username) {
        return personaRepository.findByUsername(username).stream().findFirst();
    }

    public boolean actualizarPersona(String username, PersonaDTO personaDTO) {
        Optional<Persona> personaOpt = obtenerPersonaPorUsername(username);
        if (personaOpt.isPresent()) {
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
        return false;
    }

    public boolean eliminarPersona(String username) {
        Optional<Persona> personaOpt = obtenerPersonaPorUsername(username);
        if (personaOpt.isPresent()) {
            personaRepository.delete(personaOpt.get());
            return true;
        }
        return false;
    }

    public boolean seguirUsuario(SeguirRequest request) {
        Optional<Persona> seguidorOpt = obtenerPersonaPorUsername(request.getUsernameSeguidor());
        Optional<Persona> seguidoOpt = obtenerPersonaPorUsername(request.getUsernameSeguido());

        if (seguidorOpt.isPresent() && seguidoOpt.isPresent()) {
            Persona seguidor = seguidorOpt.get();
            Persona seguido = seguidoOpt.get();

            if (!seguidor.getSeguidos().contains(seguido)) {
                seguidor.getSeguidos().add(seguido);
                seguido.getSeguidores().add(seguidor);

                personaRepository.save(seguidor);
                personaRepository.save(seguido);
                return true;
            }
        }
        return false;
    }
}
