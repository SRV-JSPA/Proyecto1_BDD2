package com.example.neo4jbackend.controller;

import com.example.neo4jbackend.dto.ComentarioDTO;
import com.example.neo4jbackend.dto.LikeRequest;
import com.example.neo4jbackend.model.Comentario;
import com.example.neo4jbackend.service.ComentarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        comentarioService.crearComentario(comentarioDTO);
        return ResponseEntity.ok("Comentario creado con éxito");
    }

    @GetMapping("/publicacion/{idPublicacion}")
    public List<Comentario> obtenerComentariosDePublicacion(@PathVariable Long idPublicacion) {
        return comentarioService.obtenerComentariosDePublicacion(idPublicacion);
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


}
