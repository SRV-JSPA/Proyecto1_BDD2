package com.example.neo4jbackend.controller;

import com.example.neo4jbackend.dto.EtiquetaDTO;
import com.example.neo4jbackend.service.EtiquetaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/etiquetas")
@CrossOrigin(origins = "*")
public class EtiquetaController {
    private final EtiquetaService etiquetaService;

    public EtiquetaController(EtiquetaService etiquetaService) {
        this.etiquetaService = etiquetaService;
    }

    //C
    @PostMapping
    public ResponseEntity<String> agregarEtiqueta(@RequestBody EtiquetaDTO etiquetaDTO) {
        etiquetaService.crearEtiqueta(etiquetaDTO);
        return ResponseEntity.ok("Etiqueta creada con éxito");
    }

    //R
    @GetMapping
    public ResponseEntity<List<EtiquetaDTO>> obtenerTodasLasEtiquetas() {
        return ResponseEntity.ok(etiquetaService.obtenerTodasLasEtiquetas());
    }

    @GetMapping("/{nombre}")
    public ResponseEntity<EtiquetaDTO> obtenerEtiquetaPorNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(etiquetaService.obtenerEtiquetaPorNombre(nombre));
    }

    //U
    @PutMapping("/{nombre}")
    public ResponseEntity<String> actualizarEtiqueta(
            @PathVariable String nombre,
            @RequestBody EtiquetaDTO etiquetaDTO) {
        etiquetaService.actualizarEtiqueta(nombre, etiquetaDTO);
        return ResponseEntity.ok("Etiqueta actualizada con éxito");
    }

    //D
    @DeleteMapping("/{nombre}")
    public ResponseEntity<String> eliminarEtiqueta(@PathVariable String nombre) {
        etiquetaService.eliminarEtiqueta(nombre);
        return ResponseEntity.ok("Etiqueta eliminada con éxito");
    }
    @PostMapping("/asignar")
    public ResponseEntity<String> asignarEtiquetaAPublicacion(
            @RequestParam String nombreEtiqueta,
            @RequestParam Long idPublicacion) {
        etiquetaService.agregarEtiquetaAPublicacion(nombreEtiqueta, idPublicacion);
        return ResponseEntity.ok("Etiqueta asignada a la publicación");
    }
}