package com.peliculasapi.controllerDTO;

import com.peliculasapi.ServiceDTO.GeneroServiceDTO;
import com.peliculasapi.dto.GeneroDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cinemax/generos")
public class GeneroControllerDTO {

    @Autowired
    private GeneroServiceDTO generoServiceDTO;

    @GetMapping
    public ResponseEntity<List<GeneroDTO>> getAllGeneros() {
        List<GeneroDTO> generos = generoServiceDTO.getAllGeneros();
        return ResponseEntity.ok(generos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneroDTO> getGeneroById(@PathVariable Long id) {
        GeneroDTO genero = generoServiceDTO.getGeneroById(id);
        if (genero != null) {
            return ResponseEntity.ok(genero);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}