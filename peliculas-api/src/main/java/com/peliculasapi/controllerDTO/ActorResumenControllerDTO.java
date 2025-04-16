package com.peliculasapi.controllerDTO;

import com.peliculasapi.ServiceDTO.ActorResumenServiceDTO;
import com.peliculasapi.dto.ActorResumenDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cinemax/actores")
public class ActorResumenControllerDTO {

    @Autowired
    private ActorResumenServiceDTO actorResumenServiceDTO;

    @GetMapping
    public ResponseEntity<List<ActorResumenDTO>> getAllActors() {
        List<ActorResumenDTO> actores = actorResumenServiceDTO.getAllActors();
        return ResponseEntity.ok(actores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActorResumenDTO> getActorById(@PathVariable Long id) {
        ActorResumenDTO actor = actorResumenServiceDTO.getActorById(id);
        if (actor != null) {
            return ResponseEntity.ok(actor);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}