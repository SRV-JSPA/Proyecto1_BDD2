package com.example.neo4jbackend.service;

import com.example.neo4jbackend.dto.EtiquetaDTO;
import com.example.neo4jbackend.model.Etiqueta;
import com.example.neo4jbackend.model.Publicacion;
import com.example.neo4jbackend.repository.EtiquetaRepository;
import com.example.neo4jbackend.repository.PublicacionRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EtiquetaService {
    private final EtiquetaRepository etiquetaRepository;
    private final PublicacionRepository publicacionRepository;

    public EtiquetaService(EtiquetaRepository etiquetaRepository, PublicacionRepository publicacionRepository) {
        this.etiquetaRepository = etiquetaRepository;
        this.publicacionRepository = publicacionRepository;
    }

    public void crearEtiqueta(EtiquetaDTO etiquetaDTO) {
        Optional<Etiqueta> etiquetaOpt = etiquetaRepository.findByNombre(etiquetaDTO.getNombre());

        if (etiquetaOpt.isEmpty()) {
            Etiqueta etiqueta = new Etiqueta(etiquetaDTO.getNombre(), etiquetaDTO.getPopularidad());
            etiquetaRepository.save(etiqueta);
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
}
