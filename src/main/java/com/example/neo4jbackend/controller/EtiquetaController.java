package com.example.neo4jbackend.controller;

import com.example.neo4jbackend.dto.EtiquetaDTO;
import com.example.neo4jbackend.service.EtiquetaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/etiquetas")
public class EtiquetaController {
    private final EtiquetaService etiquetaService;

    public EtiquetaController(EtiquetaService etiquetaService) {
        this.etiquetaService = etiquetaService;
    }

    @PostMapping
    public ResponseEntity<String> agregarEtiqueta(@RequestBody EtiquetaDTO etiquetaDTO) {
        etiquetaService.crearEtiqueta(etiquetaDTO);
        return ResponseEntity.ok("Etiqueta creada con éxito");
    }

    @PostMapping("/asignar")
    public ResponseEntity<String> asignarEtiquetaAPublicacion(@RequestParam String nombreEtiqueta, @RequestParam Long idPublicacion) {
        etiquetaService.agregarEtiquetaAPublicacion(nombreEtiqueta, idPublicacion);
        return ResponseEntity.ok("Etiqueta asignada a la publicación");
    }
}
