package com.example.neo4jbackend.controller;

import com.example.neo4jbackend.dto.GrupoDTO;
import com.example.neo4jbackend.service.GrupoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/grupos")
@CrossOrigin(origins = "*")
public class GrupoController {
    private final GrupoService grupoService;

    public GrupoController(GrupoService grupoService) {
        this.grupoService = grupoService;
    }
    @PostMapping
    public ResponseEntity<String> agregarGrupo(@RequestBody GrupoDTO grupoDTO) {
        grupoService.crearGrupo(grupoDTO);
        return ResponseEntity.ok("Grupo creado con éxito");
    }

    @GetMapping
    public ResponseEntity<List<GrupoDTO>> obtenerGrupos() {
        return ResponseEntity.ok(grupoService.obtenerGrupos());
    }

    @GetMapping("/{nombre}")
    public ResponseEntity<GrupoDTO> obtenerGrupoPorNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(grupoService.obtenerGrupoPorNombre(nombre));
    }

    @PutMapping("/{nombre}")
    public ResponseEntity<String> actualizarGrupo(
            @PathVariable String nombre,
            @RequestBody GrupoDTO grupoDTO) {
        grupoService.actualizarGrupo(nombre, grupoDTO);
        return ResponseEntity.ok("Grupo actualizado con éxito");
    }

    @DeleteMapping("/{nombre}")
    public ResponseEntity<String> eliminarGrupo(@PathVariable String nombre) {
        grupoService.eliminarGrupo(nombre);
        return ResponseEntity.ok("Grupo eliminado con éxito");
    }
    @PostMapping("/unirse")
    public ResponseEntity<String> unirseAGrupo(
            @RequestParam String username,
            @RequestParam String nombreGrupo) {
        boolean success = grupoService.unirseAGrupo(username, nombreGrupo);
        if (success) {
            return ResponseEntity.ok(username + " se ha unido al grupo " + nombreGrupo);
        } else {
            return ResponseEntity.badRequest().body("Error: el usuario o el grupo no existen");
        }
    }

    @PostMapping("/moderar")
    public ResponseEntity<String> moderarGrupo(
            @RequestParam String username,
            @RequestParam String nombreGrupo) {
        boolean success = grupoService.moderarGrupo(username, nombreGrupo);
        if (success) {
            return ResponseEntity.ok(username + " ahora es moderador del grupo " + nombreGrupo);
        } else {
            return ResponseEntity.badRequest().body("Error: el usuario o el grupo no existen o ya es moderador.");
        }
    }

    @PostMapping("/{nombreGrupo}/miembros/agregar")
    public ResponseEntity<String> agregarMiembro(
            @PathVariable String nombreGrupo,
            @RequestParam String username) {
        boolean success = grupoService.agregarMiembro(nombreGrupo, username);
        if (success) {
            return ResponseEntity.ok(username + " ha sido agregado al grupo " + nombreGrupo);
        } else {
            return ResponseEntity.badRequest().body("Error: el usuario o el grupo no existen o ya es miembro.");
        }
    }

    @DeleteMapping("/{nombreGrupo}/miembros/eliminar")
    public ResponseEntity<String> removerMiembro(
            @PathVariable String nombreGrupo,
            @RequestParam String username) {
        boolean success = grupoService.removerMiembro(nombreGrupo, username);
        if (success) {
            return ResponseEntity.ok(username + " ha sido eliminado del grupo " + nombreGrupo);
        } else {
            return ResponseEntity.badRequest().body("Error: el usuario no es miembro del grupo o el grupo no existe.");
        }
    }

    @PostMapping("/{nombreGrupo}/moderadores/agregar")
    public ResponseEntity<String> agregarModerador(
            @PathVariable String nombreGrupo,
            @RequestParam String username) {
        boolean success = grupoService.agregarModerador(nombreGrupo, username);
        if (success) {
            return ResponseEntity.ok(username + " ahora es moderador del grupo " + nombreGrupo);
        } else {
            return ResponseEntity.badRequest().body("Error: el usuario o el grupo no existen o ya es moderador.");
        }
    }

    @DeleteMapping("/{nombreGrupo}/moderadores/eliminar")
    public ResponseEntity<String> removerModerador(
            @PathVariable String nombreGrupo,
            @RequestParam String username) {
        boolean success = grupoService.removerModerador(nombreGrupo, username);
        if (success) {
            return ResponseEntity.ok(username + " ya no es moderador del grupo " + nombreGrupo);
        } else {
            return ResponseEntity.badRequest().body("Error: el usuario no es moderador del grupo o el grupo no existe.");
        }
    }
    
}