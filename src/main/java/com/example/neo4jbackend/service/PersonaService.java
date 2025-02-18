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

    public boolean seguirUsuario(SeguirRequest request) {
        Optional<Persona> seguidorOpt = personaRepository.findByUsername(request.getUsernameSeguidor()).stream().findFirst();
        Optional<Persona> seguidoOpt = personaRepository.findByUsername(request.getUsernameSeguido()).stream().findFirst();

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