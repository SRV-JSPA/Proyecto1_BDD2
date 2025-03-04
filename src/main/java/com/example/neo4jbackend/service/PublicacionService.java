package com.example.neo4jbackend.service;

import com.example.neo4jbackend.dto.PublicacionDTO;
import com.example.neo4jbackend.model.Etiqueta;
import com.example.neo4jbackend.model.Persona;
import com.example.neo4jbackend.model.Publicacion;
import com.example.neo4jbackend.repository.EtiquetaRepository;
import com.example.neo4jbackend.repository.PersonaRepository;
import com.example.neo4jbackend.repository.PublicacionRepository;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class PublicacionService {
    private final PublicacionRepository publicacionRepository;
    private final PersonaRepository personaRepository;
    private final EtiquetaRepository etiquetaRepository;

    public PublicacionService(PublicacionRepository publicacionRepository, PersonaRepository personaRepository, EtiquetaRepository etiquetaRepository) {
        this.publicacionRepository = publicacionRepository;
        this.personaRepository = personaRepository;
        this.etiquetaRepository = etiquetaRepository;
    }

    public boolean crearPublicacion(PublicacionDTO publicacionDTO) {
        Optional<Persona> autorOpt = personaRepository.findByUsername(publicacionDTO.getUsernameAutor()).stream().findFirst();
        if (autorOpt.isEmpty()) return false;

        Publicacion publicacion = new Publicacion(
                publicacionDTO.getId(),
                publicacionDTO.getContenido(),
                publicacionDTO.getFechaPublicacion(),
                publicacionDTO.getImagenUrl(),
                publicacionDTO.getEtiquetas(),
                0,
                0,
                autorOpt.get()
        );
        publicacionRepository.save(publicacion);
        return true;
    }

    public List<Publicacion> obtenerPublicaciones() {
        return publicacionRepository.findAll();
    }

    public Optional<Publicacion> obtenerPublicacionPorId(Long id) {
        return publicacionRepository.findById(id);
    }

    public boolean actualizarPublicacion(Long id, PublicacionDTO publicacionDTO) {
        Optional<Publicacion> publicacionOpt = publicacionRepository.findById(id);
        if (publicacionOpt.isEmpty()) return false;

        Publicacion publicacion = publicacionOpt.get();
        publicacion.setContenido(publicacionDTO.getContenido());
        publicacion.setFechaPublicacion(publicacionDTO.getFechaPublicacion());
        publicacion.setImagenUrl(publicacionDTO.getImagenUrl());
        publicacion.setEtiquetas(publicacionDTO.getEtiquetas());

        publicacionRepository.save(publicacion);
        return true;
    }

    public boolean eliminarPublicacion(Long id) {
        Optional<Publicacion> publicacionOpt = publicacionRepository.findById(id);
        if (publicacionOpt.isEmpty()) return false;

        publicacionRepository.delete(publicacionOpt.get());
        return true;
    }

    public String darLike(String username, Long idPublicacion) {
        Optional<Persona> personaOpt = personaRepository.findByUsername(username).stream().findFirst();
        Optional<Publicacion> publicacionOpt = publicacionRepository.findById(idPublicacion);

        if (personaOpt.isEmpty() || publicacionOpt.isEmpty()) {
            return "Error: Usuario o publicación no encontrados.";
        }

        Persona persona = personaOpt.get();
        Publicacion publicacion = publicacionOpt.get();

        if (publicacion.getLikes().contains(persona)) {
            return "Error: " + username + " ya ha dado like a esta publicación.";
        }

        publicacion.getLikes().add(persona);
        publicacion.setNumLikes(publicacion.getNumLikes() + 1);
        publicacionRepository.save(publicacion);

        return "Éxito: " + username + " le dio like a la publicación " + idPublicacion;
    }

    public String quitarLike(String username, Long idPublicacion) {
        Optional<Persona> personaOpt = personaRepository.findByUsername(username).stream().findFirst();
        Optional<Publicacion> publicacionOpt = publicacionRepository.findById(idPublicacion);

        if (personaOpt.isEmpty() || publicacionOpt.isEmpty()) {
            return "Error: Usuario o publicación no encontrados.";
        }

        Persona persona = personaOpt.get();
        Publicacion publicacion = publicacionOpt.get();

        if (!publicacion.getLikes().contains(persona)) {
            return "Error: " + username + " no ha dado like a esta publicación.";
        }

        publicacion.getLikes().remove(persona);
        publicacion.setNumLikes(publicacion.getNumLikes() - 1);
        publicacionRepository.save(publicacion);

        return "Éxito: " + username + " quitó su like de la publicación " + idPublicacion;
    }

    public List<Persona> obtenerLikesDePublicacion(Long idPublicacion) {
        return publicacionRepository.findLikesDePublicacion(idPublicacion);
    }

    public List<Publicacion> obtenerPublicacionesLikeadasPorUsuario(String username) {
        return publicacionRepository.findPublicacionesLikeadasPorUsuario(username);
    }

    public void loadPublicacionesFromCSV(String filePath) {
        int totalProcesadas = 0, totalGuardadas = 0, totalDescartadas = 0;

        try (CSVReader reader = new CSVReaderBuilder(new FileReader(Paths.get(filePath).toFile()))
                .withCSVParser(new CSVParserBuilder().withQuoteChar('"').build()) 
                .build()) {
            
            List<String[]> records = reader.readAll();
            records.remove(0); 

            System.out.println("Cargando publicaciones desde: " + filePath);

            for (String[] record : records) {
                try {
                    totalProcesadas++;

                    System.out.println("\nProcesando publicación:");
                    System.out.println("Contenido: " + record[1]);
                    System.out.println("Fecha: " + record[2]);
                    System.out.println("Imagen: " + record[3]);
                    System.out.println("Etiquetas: " + record[4]);
                    System.out.println("NumLikes: " + record[5]);
                    System.out.println("NumComentarios: " + record[6]);
                    System.out.println("Autor ID: " + record[7]);

                    Long autorId = Long.parseLong(record[7]); 
                    Optional<Persona> autorOpt = personaRepository.findById(autorId);

                    if (autorOpt.isEmpty()) {
                        System.out.println("⚠️ Autor con ID " + autorId + " no encontrado. Saltando publicación.");
                        totalDescartadas++;
                        continue;
                    }

                    String etiquetasRaw = record[4].trim();
                    List<String> etiquetas = Arrays.asList(etiquetasRaw
                        .replace("[", "").replace("]", "").replace("\"", "") 
                        .split("\\s*,\\s*")); 

                    Long id = Long.parseLong(record[0]);

                    Publicacion publicacion = new Publicacion(
                        id,
                        record[1], 
                        LocalDateTime.parse(record[2]), 
                        record[3], 
                        etiquetas, 
                        Integer.parseInt(record[5]), 
                        Integer.parseInt(record[6]), 
                        autorOpt.get() 
                    );

                    publicacionRepository.save(publicacion);
                    totalGuardadas++;
                    System.out.println("Publicación guardada correctamente.");

                } catch (NumberFormatException e) {
                    System.err.println("Error de formato en números: " + e.getMessage());
                } catch (DateTimeParseException e) {
                    System.err.println("Error en el formato de fecha: " + e.getMessage());
                } catch (Exception e) {
                    System.err.println("Error inesperado al procesar una publicación: " + e.getMessage());
                }
            }

            System.out.println("\nResumen de carga de publicaciones:");
            System.out.println("Total de publicaciones en el CSV: " + totalProcesadas);
            System.out.println("Publicaciones guardadas en Neo4j: " + totalGuardadas);
            System.out.println("Publicaciones descartadas por autor no encontrado: " + totalDescartadas);
            System.out.println("Proceso completado.");

        } catch (IOException e) {
            System.err.println("Error al leer el archivo CSV: " + e.getMessage());
        } catch (CsvException e) {
            System.err.println("Error en el formato del CSV: " + e.getMessage());
        }
    }

    public void loadRelacionPublicacionEtiquetaFromCSV(String filePath) {
        int totalProcesadas = 0, totalGuardadas = 0, totalDescartadas = 0;

        try (CSVReader reader = new CSVReader(new FileReader(Paths.get(filePath).toFile()))) {
            List<String[]> records = reader.readAll();
            records.remove(0); 

            System.out.println("Cargando relaciones Publicación-Etiqueta desde: " + filePath);

            for (String[] record : records) {
                try {
                    totalProcesadas++;

                    Long publicacionId = Long.parseLong(record[0]); 
                    String etiquetaNombre = record[1]; 

                    Optional<Publicacion> publicacionOpt = publicacionRepository.findById(publicacionId);
                    Optional<Etiqueta> etiquetaOpt = etiquetaRepository.findById(etiquetaNombre);

                    if (publicacionOpt.isEmpty() || etiquetaOpt.isEmpty()) {
                        System.out.println("Publicación ID " + publicacionId + " o Etiqueta '" + etiquetaNombre + "' no encontrada. Saltando.");
                        totalDescartadas++;
                        continue;
                    }

                    Publicacion publicacion = publicacionOpt.get();
                    Etiqueta etiqueta = etiquetaOpt.get();

                    if (!publicacion.getHashtags().contains(etiqueta)) {
                        publicacion.getHashtags().add(etiqueta);
                        publicacionRepository.save(publicacion);
                        totalGuardadas++;
                        System.out.println("Relación creada: Publicación " + publicacionId + " -> Etiqueta '" + etiquetaNombre + "'");
                    }

                } catch (NumberFormatException e) {
                    System.err.println("Error de formato en números: " + e.getMessage());
                } catch (Exception e) {
                    System.err.println("Error inesperado al procesar una relación: " + e.getMessage());
                }
            }

            System.out.println("\nResumen de carga de relaciones:");
            System.out.println("Total en el CSV: " + totalProcesadas);
            System.out.println("Relaciones creadas: " + totalGuardadas);
            System.out.println("Relaciones descartadas (Publicación/Etiqueta no encontradas): " + totalDescartadas);
            System.out.println("Proceso completado.");

        } catch (IOException e) {
            System.err.println("Error al leer el archivo CSV: " + e.getMessage());
        } catch (CsvException e) {
            System.err.println("Error en el formato del CSV: " + e.getMessage());
        }
    }
}
