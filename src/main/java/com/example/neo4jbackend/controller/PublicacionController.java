package com.example.neo4jbackend.controller;

import com.example.neo4jbackend.dto.PublicacionDTO;
import com.example.neo4jbackend.model.Publicacion;
import com.example.neo4jbackend.service.PublicacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.neo4jbackend.dto.LikeRequest;

import java.util.List;

@RestController
@RequestMapping("/publicaciones")
public class PublicacionController {
    private final PublicacionService publicacionService;

    public PublicacionController(PublicacionService publicacionService) {
        this.publicacionService = publicacionService;
    }

    @PostMapping
    public ResponseEntity<String> agregarPublicacion(@RequestBody PublicacionDTO publicacionDTO) {
        publicacionService.crearPublicacion(publicacionDTO);
        return ResponseEntity.ok("Publicación creada con éxito");
    }

    @GetMapping
    public List<Publicacion> obtenerPublicaciones() {
        return publicacionService.obtenerPublicaciones();
    }

    @PostMapping("/darLike")
    public ResponseEntity<String> darLike(@RequestBody LikeRequest likeRequest) {
        boolean success = publicacionService.darLike(likeRequest.getUsername(), likeRequest.getIdPublicacion());
        if (success) {
            return ResponseEntity.ok(likeRequest.getUsername() + " le dio like a la publicación " + likeRequest.getIdPublicacion());
        } else {
            return ResponseEntity.badRequest().body("Error: el usuario o la publicación no existen o ya ha dado like.");
        }
    }
}
