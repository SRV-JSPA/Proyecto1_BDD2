package com.example.neo4jbackend.service;

import com.example.neo4jbackend.dto.EtiquetaDTO;
import com.example.neo4jbackend.model.Etiqueta;
import com.example.neo4jbackend.model.Publicacion;
import com.example.neo4jbackend.repository.EtiquetaRepository;
import com.example.neo4jbackend.repository.PublicacionRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EtiquetaService {
    private final EtiquetaRepository etiquetaRepository;
    private final PublicacionRepository publicacionRepository;

    public EtiquetaService(EtiquetaRepository etiquetaRepository, PublicacionRepository publicacionRepository) {
        this.etiquetaRepository = etiquetaRepository;
        this.publicacionRepository = publicacionRepository;
    }

    //C
    public void crearEtiqueta(EtiquetaDTO etiquetaDTO) {
        Optional<Etiqueta> etiquetaOpt = etiquetaRepository.findByNombre(etiquetaDTO.getNombre());

        if (etiquetaOpt.isEmpty()) {
            Etiqueta etiqueta = new Etiqueta(etiquetaDTO.getNombre(), etiquetaDTO.getPopularidad());
            etiquetaRepository.save(etiqueta);
        }
    }

    //R
    public List<EtiquetaDTO> obtenerTodasLasEtiquetas() {
        List<Etiqueta> etiquetas = etiquetaRepository.findAll();
        return etiquetas.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public EtiquetaDTO obtenerEtiquetaPorNombre(String nombre) {
        Optional<Etiqueta> etiquetaOpt = etiquetaRepository.findByNombre(nombre);
        return etiquetaOpt.map(this::convertirADTO)
                .orElseThrow(() -> new RuntimeException("Etiqueta no encontrada"));
    }

    //U
    public void actualizarEtiqueta(String nombre, EtiquetaDTO etiquetaDTO) {
        Optional<Etiqueta> etiquetaOpt = etiquetaRepository.findByNombre(nombre);
        
        if (etiquetaOpt.isPresent()) {
            Etiqueta etiqueta = etiquetaOpt.get();
            etiqueta.setPopularidad(etiquetaDTO.getPopularidad());
            etiquetaRepository.save(etiqueta);
        } else {
            throw new RuntimeException("Etiqueta no encontrada");
        }
    }

    //D
    public void eliminarEtiqueta(String nombre) {
        Optional<Etiqueta> etiquetaOpt = etiquetaRepository.findByNombre(nombre);
        
        if (etiquetaOpt.isPresent()) {
            etiquetaRepository.delete(etiquetaOpt.get());
        } else {
            throw new RuntimeException("Etiqueta no encontrada");
        }
    }

    public void agregarEtiquetaAPublicacion(String nombreEtiqueta, Long idPublicacion) {
        Optional<Etiqueta> etiquetaOpt = etiquetaRepository.findByNombre(nombreEtiqueta);
        Optional<Publicacion> publicacionOpt = publicacionRepository.findById(idPublicacion);

        if (etiquetaOpt.isPresent() && publicacionOpt.isPresent()) {
            Publicacion publicacion = publicacionOpt.get();
            Etiqueta etiqueta = etiquetaOpt.get();

            publicacion.getHashtags().add(etiqueta);
            etiqueta.setPopularidad(etiqueta.getPopularidad() + 1);

            publicacionRepository.save(publicacion);
            etiquetaRepository.save(etiqueta);
        }
    }

    private EtiquetaDTO convertirADTO(Etiqueta etiqueta) {
        return new EtiquetaDTO(etiqueta.getNombre(), etiqueta.getPopularidad());
    }
}