package com.example.neo4jbackend.service;

import com.example.neo4jbackend.dto.EtiquetaDTO;
import com.example.neo4jbackend.model.Etiqueta;
import com.example.neo4jbackend.model.Publicacion;
import com.example.neo4jbackend.repository.EtiquetaRepository;
import com.example.neo4jbackend.repository.PublicacionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class EtiquetaService {
    private final EtiquetaRepository etiquetaRepository;
    private final PublicacionRepository publicacionRepository;
    private static final int MAX_LONGITUD_NOMBRE = 50;
    private static final Pattern NOMBRE_VALIDO = Pattern.compile("^[a-zA-Z0-9_]+$");

    public EtiquetaService(EtiquetaRepository etiquetaRepository, PublicacionRepository publicacionRepository) {
        this.etiquetaRepository = etiquetaRepository;
        this.publicacionRepository = publicacionRepository;
    }

    public void crearEtiqueta(EtiquetaDTO etiquetaDTO) {
        validarNombreEtiqueta(etiquetaDTO.getNombre());
        
        Optional<Etiqueta> etiquetaOpt = etiquetaRepository.findByNombre(etiquetaDTO.getNombre());
        if (etiquetaOpt.isEmpty()) {
            Etiqueta etiqueta = new Etiqueta(etiquetaDTO.getNombre(), Math.max(0, etiquetaDTO.getPopularidad()));
            etiquetaRepository.save(etiqueta);
        }
    }

    public List<EtiquetaDTO> obtenerTodasLasEtiquetas() {
        List<Etiqueta> etiquetas = etiquetaRepository.findAll();
        return etiquetas.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public EtiquetaDTO obtenerEtiquetaPorNombre(String nombre) {
        return etiquetaRepository.findByNombre(nombre)
                .map(this::convertirADTO)
                .orElseThrow(() -> new RuntimeException("Etiqueta no encontrada"));
    }

    public void actualizarEtiqueta(String nombre, EtiquetaDTO etiquetaDTO) {
        validarNombreEtiqueta(etiquetaDTO.getNombre());
        
        Etiqueta etiqueta = etiquetaRepository.findByNombre(nombre)
                .orElseThrow(() -> new RuntimeException("Etiqueta no encontrada"));
        
        etiqueta.setPopularidad(Math.max(0, etiquetaDTO.getPopularidad()));
        etiquetaRepository.save(etiqueta);
    }

    public void eliminarEtiqueta(String nombre) {
        Etiqueta etiqueta = etiquetaRepository.findByNombre(nombre)
                .orElseThrow(() -> new RuntimeException("Etiqueta no encontrada"));
        etiquetaRepository.delete(etiqueta);
    }

    public void agregarEtiquetaAPublicacion(String nombreEtiqueta, Long idPublicacion) {
        Etiqueta etiqueta = etiquetaRepository.findByNombre(nombreEtiqueta)
                .orElseThrow(() -> new RuntimeException("Etiqueta no encontrada"));
        
        Publicacion publicacion = publicacionRepository.findById(idPublicacion)
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada"));
        
        publicacion.getHashtags().add(etiqueta);
        etiqueta.setPopularidad(etiqueta.getPopularidad() + 1);
        
        publicacionRepository.save(publicacion);
        etiquetaRepository.save(etiqueta);
    }

    private void validarNombreEtiqueta(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new RuntimeException("El nombre de la etiqueta no puede estar vacío");
        }
        if (nombre.length() > MAX_LONGITUD_NOMBRE) {
            throw new RuntimeException("El nombre de la etiqueta no puede superar los " + MAX_LONGITUD_NOMBRE + " caracteres");
        }
        if (!NOMBRE_VALIDO.matcher(nombre).matches()) {
            throw new RuntimeException("El nombre de la etiqueta solo puede contener letras, números y guiones bajos");
        }
    }

    private EtiquetaDTO convertirADTO(Etiqueta etiqueta) {
        return new EtiquetaDTO(etiqueta.getNombre(), etiqueta.getPopularidad());
    }
}
