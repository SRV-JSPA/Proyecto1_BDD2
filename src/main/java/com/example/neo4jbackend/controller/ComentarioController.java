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
    public ResponseEntity<List<ComentarioDTO>> obtenerTodosLosComentarios() {
        List<ComentarioDTO> comentarios = comentarioService.obtenerTodosLosComentarios();
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
    public ResponseEntity<ComentarioDTO> obtenerComentarioPorId(@PathVariable Long id) {
        Optional<ComentarioDTO> comentarioOpt = comentarioService.obtenerComentarioPorId(id);
        
        return comentarioOpt
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.badRequest().body(null)); 
    }

    @PostMapping("/{id}/asignarAutor")
    public ResponseEntity<String> asignarAutor(@PathVariable Long id, @RequestParam String username) {
        boolean success = comentarioService.asignarAutor(id, username);
        return success ? ResponseEntity.ok("Autor asignado con éxito") : ResponseEntity.badRequest().body("Error al asignar autor");
    }

    @PostMapping("/{id}/asignarPublicacion")
    public ResponseEntity<String> asignarPublicacion(@PathVariable Long id, @RequestParam Long idPublicacion) {
        boolean success = comentarioService.asignarPublicacion(id, idPublicacion);
        return success ? ResponseEntity.ok("Publicación asignada con éxito") : ResponseEntity.badRequest().body("Error al asignar publicación");
    }

    @PostMapping("/{id}/darLike")
    public ResponseEntity<String> darLike(@PathVariable Long id, @RequestParam String username) {
        boolean success = comentarioService.darLike(username, id);
        return success ? ResponseEntity.ok("Like agregado con éxito") : ResponseEntity.badRequest().body("Error al dar like");
    }

    @PostMapping("/{id}/quitarLike")
    public ResponseEntity<String> quitarLike(@PathVariable Long id, 
                                             @RequestParam String username, 
                                             @RequestParam String contenido) {
        boolean success = comentarioService.quitarLike(username, id, contenido);
        return success ? ResponseEntity.ok("Like eliminado con éxito") : ResponseEntity.badRequest().body("Error al quitar like");
    }

}