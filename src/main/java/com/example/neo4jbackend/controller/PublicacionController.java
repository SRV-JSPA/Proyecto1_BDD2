package com.example.neo4jbackend.controller;

import com.example.neo4jbackend.dto.PublicacionDTO;
import com.example.neo4jbackend.dto.LikeRequest;
import com.example.neo4jbackend.model.Persona;
import com.example.neo4jbackend.model.Publicacion;
import com.example.neo4jbackend.service.PublicacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/publicaciones")
public class PublicacionController {
    private final PublicacionService publicacionService;

    public PublicacionController(PublicacionService publicacionService) {
        this.publicacionService = publicacionService;
    }

    @PostMapping
    public ResponseEntity<String> agregarPublicacion(@RequestBody PublicacionDTO publicacionDTO) {
        boolean creada = publicacionService.crearPublicacion(publicacionDTO);
        return creada ? ResponseEntity.ok("Publicación creada con éxito") :
                        ResponseEntity.badRequest().body("Error: Autor no encontrado.");
    }

    @GetMapping
    public List<Publicacion> obtenerPublicaciones() {
        return publicacionService.obtenerPublicaciones();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Publicacion> obtenerPublicacionPorId(@PathVariable Long id) {
        Optional<Publicacion> publicacion = publicacionService.obtenerPublicacionPorId(id);
        return publicacion.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarPublicacion(@PathVariable Long id, @RequestBody PublicacionDTO publicacionDTO) {
        boolean actualizada = publicacionService.actualizarPublicacion(id, publicacionDTO);
        return actualizada ? ResponseEntity.ok("Publicación actualizada") :
                             ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarPublicacion(@PathVariable Long id) {
        boolean eliminada = publicacionService.eliminarPublicacion(id);
        return eliminada ? ResponseEntity.ok("Publicación eliminada") :
                           ResponseEntity.notFound().build();
    }

    @PostMapping("/darLike")
    public ResponseEntity<String> darLike(@RequestBody LikeRequest likeRequest) {
        String result = publicacionService.darLike(likeRequest.getUsername(), likeRequest.getIdPublicacion());
        return result.startsWith("Éxito") ? ResponseEntity.ok(result) :
                                            ResponseEntity.badRequest().body(result);
    }

    @DeleteMapping("/quitarLike")
    public ResponseEntity<String> quitarLike(@RequestBody LikeRequest likeRequest) {
        String result = publicacionService.quitarLike(likeRequest.getUsername(), likeRequest.getIdPublicacion());
        return result.startsWith("Éxito") ? ResponseEntity.ok(result) :
                                            ResponseEntity.badRequest().body(result);
    }

    @GetMapping("/{id}/likes")
    public List<Persona> obtenerLikesDePublicacion(@PathVariable Long id) {
        return publicacionService.obtenerLikesDePublicacion(id);
    }

    @GetMapping("/likes/{username}")
    public List<Publicacion> obtenerPublicacionesLikeadas(@PathVariable String username) {
        return publicacionService.obtenerPublicacionesLikeadasPorUsuario(username);
    }
}
