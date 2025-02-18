package com.example.neo4jbackend.controller;
import com.example.neo4jbackend.dto.PersonaDTO;
import com.example.neo4jbackend.dto.SeguirRequest;
import com.example.neo4jbackend.model.Persona;
import com.example.neo4jbackend.service.PersonaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/personas")
public class PersonaController {
    private final PersonaService personaService;

    public PersonaController(PersonaService personaService) {
        this.personaService = personaService;
    }

    @PostMapping
    public ResponseEntity<String> agregarPersona(@RequestBody PersonaDTO personaDTO) {
        personaService.crearPersona(personaDTO);
        return ResponseEntity.ok("Persona creada con éxito");
    }

    @GetMapping
    public List<Persona> obtenerPersonas() {
        return personaService.obtenerPersonas();
    }

    @PostMapping("/seguir")
    public ResponseEntity<String> seguirUsuario(@RequestBody SeguirRequest request) {
        boolean success = personaService.seguirUsuario(request);
        if (success) {
            return ResponseEntity.ok(request.getUsernameSeguidor() + " ahora sigue a " + request.getUsernameSeguido());
        } else {
            return ResponseEntity.badRequest().body("Error: uno o ambos usuarios no existen o ya existe la relación.");
        }
    }
}