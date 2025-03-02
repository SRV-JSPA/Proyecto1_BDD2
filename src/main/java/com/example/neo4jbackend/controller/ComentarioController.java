package com.example.neo4jbackend.controller;

import com.example.neo4jbackend.dto.ComentarioDTO;
import com.example.neo4jbackend.dto.LikeRequest;
import com.example.neo4jbackend.model.Comentario;
import com.example.neo4jbackend.service.ComentarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/comentarios")
public class ComentarioController {
    private final ComentarioService comentarioService;

    public ComentarioController(ComentarioService comentarioService) {
        this.comentarioService = comentarioService;
    }

    @GetMapping
    public ResponseEntity<List<Comentario>> obtenerTodosLosComentarios() {
        List<Comentario> comentarios = comentarioService.obtenerTodosLosComentarios();
        return ResponseEntity.ok(comentarios);
    }

    @PostMapping
    public ResponseEntity<String> agregarComentario(@RequestBody ComentarioDTO comentarioDTO) {
        try {
            comentarioService.crearComentario(comentarioDTO);
            return ResponseEntity.ok("Comentario creado con éxito");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/publicacion/{idPublicacion}")
    public ResponseEntity<List<Comentario>> obtenerComentariosDePublicacion(@PathVariable Long idPublicacion) {
        return ResponseEntity.ok(comentarioService.obtenerComentariosDePublicacion(idPublicacion));
    }

    @PostMapping("/darLike")
    public ResponseEntity<String> darLike(@RequestBody LikeRequest likeRequest) {
        boolean success = comentarioService.darLike(likeRequest.getUsername(), likeRequest.getIdComentario());
        if (success) {
            return ResponseEntity.ok(likeRequest.getUsername() + " le dio like al comentario " + likeRequest.getIdComentario());
        } else {
            return ResponseEntity.badRequest().body("Error: el usuario o el comentario no existen o ya ha dado like.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarComentario(@PathVariable Long id, @RequestBody ComentarioDTO comentarioDTO) {
        try {
            boolean success = comentarioService.actualizarComentario(id, comentarioDTO);
            if (success) {
                return ResponseEntity.ok("Comentario actualizado con éxito");
            } else {
                return ResponseEntity.badRequest().body("No se encontró el comentario con ID: " + id);
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarComentario(@PathVariable Long id, @RequestParam String username) {
        try {
            boolean success = comentarioService.eliminarComentario(id, username);
            if (success) {
                return ResponseEntity.ok("Comentario eliminado con éxito");
            } else {
                return ResponseEntity.badRequest().body("No se encontró el comentario con ID: " + id);
            }
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerComentarioPorId(@PathVariable Long id) {
        Optional<Comentario> comentarioOpt = comentarioService.obtenerComentarioPorId(id);
        
        if (comentarioOpt.isPresent()) {
            return ResponseEntity.ok(comentarioOpt.get());
        } else {
            return ResponseEntity.badRequest().body("No se encontró el comentario con ID: " + id);
        }
    }
}