package com.peliculasapi.controllerDTO;

import com.peliculasapi.ServiceDTO.PeliculaPublicaServiceDTO;
import com.peliculasapi.dto.PeliculaPublicaDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cinemax/peliculas")
public class PeliculaPublicaControllerDTO {

    private final PeliculaPublicaServiceDTO peliculaPublicaServiceDTO;

    // Inyección de dependencia mediante constructor
    public PeliculaPublicaControllerDTO(PeliculaPublicaServiceDTO peliculaPublicaServiceDTO) {
        this.peliculaPublicaServiceDTO = peliculaPublicaServiceDTO;
    }

    @GetMapping
    public ResponseEntity<List<PeliculaPublicaDTO>> getAllPeliculas() {
        List<PeliculaPublicaDTO> peliculas = peliculaPublicaServiceDTO.getAllPeliculas();
        return ResponseEntity.ok(peliculas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PeliculaPublicaDTO> getPeliculaById(@PathVariable Long id) {
        PeliculaPublicaDTO pelicula = peliculaPublicaServiceDTO.getPeliculaById(id);
        if (pelicula != null) {
            return ResponseEntity.ok(pelicula);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}