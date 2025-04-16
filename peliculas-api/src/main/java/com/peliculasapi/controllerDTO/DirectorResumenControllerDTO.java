package com.peliculasapi.controllerDTO;

import com.peliculasapi.ServiceDTO.DirectorResumenServiceDTO;
import com.peliculasapi.dto.DirectorResumenDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cinemax/directores")
public class DirectorResumenControllerDTO {

    @Autowired
    private DirectorResumenServiceDTO directorResumenServiceDTO;

    @GetMapping
    public ResponseEntity<List<DirectorResumenDTO>> getAllDirectors() {
        List<DirectorResumenDTO> directores = directorResumenServiceDTO.getAllDirectors();
        return ResponseEntity.ok(directores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DirectorResumenDTO> getDirectorById(@PathVariable Long id) {
        DirectorResumenDTO director = directorResumenServiceDTO.getDirectorById(id);
        if (director != null) {
            return ResponseEntity.ok(director);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}