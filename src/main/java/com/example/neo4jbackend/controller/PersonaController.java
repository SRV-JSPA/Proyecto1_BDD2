package com.example.neo4jbackend.controller;

import com.example.neo4jbackend.dto.PersonaDTO;
import com.example.neo4jbackend.model.Persona;
import com.example.neo4jbackend.service.PersonaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/personas")
public class PersonaController {
    private final PersonaService personaService;

    public PersonaController(PersonaService personaService) {
        this.personaService = personaService;
    }

    @PostMapping
    public ResponseEntity<String> agregarPersona(@RequestBody PersonaDTO personaDTO) {
        boolean creada = personaService.crearPersona(personaDTO);
        return creada ? ResponseEntity.ok("Persona creada con éxito") :
                        ResponseEntity.badRequest().body("Error: El username ya está en uso.");
    }

    @GetMapping
    public List<Persona> obtenerPersonas() {
        return personaService.obtenerPersonas();
    }

    @GetMapping("/{username}")
    public ResponseEntity<Persona> obtenerPersonaPorUsername(@PathVariable String username) {
        Optional<Persona> persona = personaService.obtenerPersonaPorUsername(username);
        return persona.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{username}")
    public ResponseEntity<String> actualizarPersona(@PathVariable String username, @RequestBody PersonaDTO personaDTO) {
        boolean actualizado = personaService.actualizarPersona(username, personaDTO);
        return actualizado ? ResponseEntity.ok("Persona actualizada") : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<String> eliminarPersona(@PathVariable String username) {
        boolean eliminado = personaService.eliminarPersona(username);
        return eliminado ? ResponseEntity.ok("Persona eliminada") : ResponseEntity.notFound().build();
    }

    @PostMapping("/{seguidor}/seguir/{seguido}")
    public ResponseEntity<String> seguirUsuario(@PathVariable String seguidor, @PathVariable String seguido) {
        String result = personaService.seguirUsuario(seguidor, seguido);
        return result.startsWith("Éxito") ? ResponseEntity.ok(result) :
                                            ResponseEntity.badRequest().body(result);
    }

    @DeleteMapping("/{seguidor}/dejar-de-seguir/{seguido}")
    public ResponseEntity<String> dejarDeSeguir(@PathVariable String seguidor, @PathVariable String seguido) {
        String result = personaService.dejarDeSeguirUsuario(seguidor, seguido);
        return result.startsWith("Éxito") ? ResponseEntity.ok(result) :
                                            ResponseEntity.badRequest().body(result);
    }

    @GetMapping("/{username}/seguidores")
    public List<Persona> obtenerSeguidores(@PathVariable String username) {
        return personaService.obtenerSeguidores(username);
    }

    @GetMapping("/{username}/seguidos")
    public List<Persona> obtenerSeguidos(@PathVariable String username) {
        return personaService.obtenerSeguidos(username);
    }
}
