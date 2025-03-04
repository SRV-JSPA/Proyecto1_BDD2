package com.example.neo4jbackend.service;

import com.example.neo4jbackend.dto.PersonaDTO;
import com.example.neo4jbackend.model.Persona;
import com.example.neo4jbackend.repository.PersonaRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonaService {
    private final PersonaRepository personaRepository;

    public PersonaService(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    public boolean crearPersona(PersonaDTO personaDTO) {
        if (personaRepository.findByUsername(personaDTO.getUsername()).stream().findFirst().isPresent()) {
            return false;
        }
    
        Persona persona = new Persona(
                personaDTO.getNombre(),
                personaDTO.getUsername(),
                personaDTO.getEmail(),
                personaDTO.getPassword(),
                personaDTO.getFechaRegistro(),
                personaDTO.getBiografia(),
                personaDTO.getIntereses(),
                personaDTO.isCuentaVerificada(),
                personaDTO.isEsCreador(),      
                personaDTO.isEsEmpresa(),      
                personaDTO.isEsPaginaDeFans(), 
                personaDTO.isEsCantante()       
        );
    
        personaRepository.save(persona);
        return true;
    }
    
    public List<Persona> obtenerPersonas() {
        return personaRepository.findAll();
    }

    public Optional<Persona> obtenerPersonaPorUsername(String username) {
        return personaRepository.findByUsername(username).stream().findFirst();
    }

    public boolean actualizarPersona(String username, PersonaDTO personaDTO) {
        Optional<Persona> personaOpt = obtenerPersonaPorUsername(username);
        if (personaOpt.isEmpty()) return false;

        Persona persona = personaOpt.get();
        persona.setNombre(personaDTO.getNombre());
        persona.setEmail(personaDTO.getEmail());
        persona.setPassword(personaDTO.getPassword());
        persona.setBiografia(personaDTO.getBiografia());
        persona.setIntereses(personaDTO.getIntereses());
        persona.setCuentaVerificada(personaDTO.isCuentaVerificada());
        personaRepository.save(persona);
        return true;
    }

    public boolean eliminarPersona(String username) {
        Optional<Persona> personaOpt = obtenerPersonaPorUsername(username);
        if (personaOpt.isEmpty()) return false;

        personaRepository.delete(personaOpt.get());
        return true;
    }

    public String seguirUsuario(String seguidorUsername, String seguidoUsername) {
        Optional<Persona> seguidorOpt = obtenerPersonaPorUsername(seguidorUsername);
        Optional<Persona> seguidoOpt = obtenerPersonaPorUsername(seguidoUsername);

        if (seguidorOpt.isEmpty() || seguidoOpt.isEmpty()) {
            return "Error: Uno o ambos usuarios no existen.";
        }

        Persona seguidor = seguidorOpt.get();
        Persona seguido = seguidoOpt.get();

        if (seguidor.getSeguidos().stream().anyMatch(p -> p.getUsername().equals(seguidoUsername))) {
            return "Error: " + seguidorUsername + " ya sigue a " + seguidoUsername;
        }

        seguidor.getSeguidos().add(seguido);
        seguido.getSeguidores().add(seguidor);

        personaRepository.save(seguidor);
        personaRepository.save(seguido);

        return "Éxito: " + seguidorUsername + " ahora sigue a " + seguidoUsername;
    }

    public String dejarDeSeguirUsuario(String seguidorUsername, String seguidoUsername) {
        Optional<Persona> seguidorOpt = obtenerPersonaPorUsername(seguidorUsername);
        Optional<Persona> seguidoOpt = obtenerPersonaPorUsername(seguidoUsername);

        if (seguidorOpt.isEmpty() || seguidoOpt.isEmpty()) {
            return "Error: Uno o ambos usuarios no existen.";
        }

        Persona seguidor = seguidorOpt.get();
        Persona seguido = seguidoOpt.get();

        boolean eliminadoSeguidor = seguidor.getSeguidos().removeIf(p -> p.getUsername().equals(seguidoUsername));
        boolean eliminadoSeguido = seguido.getSeguidores().removeIf(p -> p.getUsername().equals(seguidorUsername));

        if (eliminadoSeguidor && eliminadoSeguido) {
            personaRepository.save(seguidor);
            personaRepository.save(seguido);
            return "Éxito: " + seguidorUsername + " dejó de seguir a " + seguidoUsername;
        }

        return "Error: " + seguidorUsername + " no sigue a " + seguidoUsername;
    }

    public List<Persona> obtenerSeguidores(String username) {
        return personaRepository.findSeguidores(username);
    }

    public List<Persona> obtenerSeguidos(String username) {
        return personaRepository.findSeguidos(username);
    }

    public void loadPersonasFromCSV(String filePath) {
        try (CSVReader reader = new CSVReader(new FileReader(Paths.get(filePath).toFile()))) {
            List<String[]> records = reader.readAll();
            records.remove(0); 

            System.out.println("Cargando personas desde: " + filePath);

            for (String[] record : records) {
                if (!personaRepository.findByUsername(record[2]).isEmpty()) {
                    continue; 
                }

                Persona persona = new Persona(
                    record[1],  
                    record[2],  
                    record[3],  
                    record[4],  
                    LocalDate.parse(record[5]),  
                    record[6], 
                    Arrays.asList(record[7].replace("[", "").replace("]", "").replace("\"", "").split(",")), 
                    Boolean.parseBoolean(record[8]), 
                    determinarEsCreador(record[6], record[7]),
                    determinarEsEmpresa(record[6], record[7]),
                    determinarEsPaginaDeFans(record[7]),
                    determinarEsCantante(record[7])
                );

                personaRepository.save(persona); 
            }

            System.out.println("Usuarios cargados correctamente desde " + filePath);

        } catch (IOException | CsvException e) {
            System.err.println("Error al leer el archivo CSV: " + e.getMessage());
        }
    }

    public void loadRelacionPersonaSigueFromCSV(String filePath) {
        Map<Long, Persona> personasMap = personaRepository.findAll().stream()
                .collect(Collectors.toMap(Persona::getId, p -> p));

        try (CSVReader reader = new CSVReader(new FileReader(Paths.get(filePath).toFile()))) {
            List<String[]> records = reader.readAll();
            records.remove(0); 

            System.out.println("Cargando relaciones SIGUE_A desde: " + filePath);

            for (String[] record : records) {
                Long seguidorId = Long.parseLong(record[0]);
                Long seguidoId = Long.parseLong(record[1]);

                Persona seguidor = personasMap.get(seguidorId);
                Persona seguido = personasMap.get(seguidoId);

                if (seguidor == null || seguido == null) {
                    System.out.println("Persona " + seguidorId + " o " + seguidoId + " no encontrada. Saltando.");
                    continue;
                }

                if (!seguidor.getSeguidos().contains(seguido)) {
                    seguidor.getSeguidos().add(seguido);
                    personaRepository.save(seguidor); 
                }
            }

            System.out.println("Relaciones SIGUE_A cargadas correctamente desde " + filePath);

        } catch (IOException | CsvException e) {
            System.err.println("Error al leer el archivo CSV: " + e.getMessage());
        }
    }

    private boolean determinarEsCreador(String biografia, String interesesRaw) {
        List<String> intereses = Arrays.asList(interesesRaw.replace("[", "").replace("]", "").replace("\"", "").split(","));
        List<String> palabrasClave = Arrays.asList("artista", "escritor", "músico", "fotógrafo", "diseñador", "cineasta", "arquitecto");
    
        return palabrasClave.stream().anyMatch(biografia.toLowerCase()::contains) ||
               intereses.stream().anyMatch(inter -> palabrasClave.contains(inter.toLowerCase()));
    }
    
    private boolean determinarEsEmpresa(String biografia, String interesesRaw) {
        List<String> intereses = Arrays.asList(interesesRaw.replace("[", "").replace("]", "").replace("\"", "").split(","));
        List<String> palabrasClave = Arrays.asList("negocios", "empresa", "emprendedor", "marketing", "inversiones");
    
        return palabrasClave.stream().anyMatch(biografia.toLowerCase()::contains) ||
               intereses.stream().anyMatch(inter -> palabrasClave.contains(inter.toLowerCase()));
    }
    
    private boolean determinarEsPaginaDeFans(String interesesRaw) {
        List<String> intereses = Arrays.asList(interesesRaw.replace("[", "").replace("]", "").replace("\"", "").split(","));
        return intereses.contains("fans") || intereses.contains("seguidores");
    }
    
    private boolean determinarEsCantante(String interesesRaw) {
        List<String> intereses = Arrays.asList(interesesRaw.replace("[", "").replace("]", "").replace("\"", "").split(","));
        return intereses.contains("música") || intereses.contains("conciertos") || intereses.contains("composición");
    }
}
