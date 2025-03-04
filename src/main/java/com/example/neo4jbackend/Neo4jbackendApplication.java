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
            personaService.loadPersonasFromCSV("csv/MOCK_DATA-4.csv");
            publicacionService.loadPublicacionesFromCSV("csv/MOCK_DATA-5.csv");
			grupoService.loadGruposFromCSV("csv/MOCK_DATA-6.csv");
			etiquetaService.loadEtiquetasFromCSV("csv/MOCK_DATA-7.csv");
			comentarioService.loadComentariosFromCSV("csv/MOCK_DATA-8.csv");
			//publicacionService.loadRelacionPublicacionEtiquetaFromCSV("csv/publicacion_tiene_hashtag_etiqueta.csv");
        };
	}


}
