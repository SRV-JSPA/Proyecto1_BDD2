package com.example.neo4jbackend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.neo4jbackend.service.PersonaService;
import com.example.neo4jbackend.service.PublicacionService;
import com.example.neo4jbackend.service.GrupoService;
import com.example.neo4jbackend.service.EtiquetaService;
import com.example.neo4jbackend.service.ComentarioService;

@SpringBootApplication
public class Neo4jbackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(Neo4jbackendApplication.class, args);
	}

	@Bean
	CommandLineRunner loadData(PersonaService personaService, PublicacionService publicacionService, GrupoService grupoService, EtiquetaService etiquetaService, ComentarioService comentarioService) {
		return _ -> {
            personaService.loadPersonasFromCSV("csv/personas.csv");
            publicacionService.loadPublicacionesFromCSV("csv/publicaciones.csv");
			grupoService.loadGruposFromCSV("csv/grupos.csv");
			etiquetaService.loadEtiquetasFromCSV("csv/etiquetas.csv");
			comentarioService.loadComentariosFromCSV("csv/comentarios.csv");
			publicacionService.loadRelacionPublicacionEtiquetaFromCSV("csv/publicacion_tiene_hashtag_etiqueta.csv");
        };
	}


}
