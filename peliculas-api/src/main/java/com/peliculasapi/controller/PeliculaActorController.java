package com.peliculasapi.controller;

import com.peliculasapi.entity.PeliculaActor;
import com.peliculasapi.service.PeliculaActorService;
import com.peliculasapi.service.PeliculaService;
import com.peliculasapi.service.ActorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/peliculas-actores")
public class PeliculaActorController {

    private final PeliculaActorService peliculaActorService;
    private final PeliculaService peliculaService;
    private final ActorService actorService;

    public PeliculaActorController(PeliculaActorService peliculaActorService, PeliculaService peliculaService, ActorService actorService) {
        this.peliculaActorService = peliculaActorService;
        this.peliculaService = peliculaService;
        this.actorService = actorService;
    }

    // ==========================================================
    // Métodos para vistas
    // ==========================================================

    @GetMapping
    public String listarPeliculaActores(Model model) {
        List<PeliculaActor> relaciones = peliculaActorService.obtenerPeliculaActores();
        model.addAttribute("relaciones", relaciones);
        return "Api/PeliculasActores/listar"; // Vista listar.html
    }

    @GetMapping("/{id}")
    public String detallePeliculaActor(@PathVariable Long id, Model model) {
        Optional<PeliculaActor> relacion = peliculaActorService.obtenerPorId(id);
        if (relacion.isPresent()) {
            model.addAttribute("relacion", relacion.get());
            return "Api/PeliculasActores/detalle"; // Vista detalle.html
        } else {
            model.addAttribute("error", "Relación Película-Actor no encontrada");
            return "Api/PeliculasActores/error"; // Vista genérica de error
        }
    }

    @GetMapping("/crear")
    public String formularioCrear(Model model) {
        cargarDatosFormulario(model);
        model.addAttribute("relacion", new PeliculaActor());
        return "Api/PeliculasActores/crear"; // Vista crear.html
    }

    @PostMapping("/guardar")
    public String guardarPeliculaActor(@Valid @ModelAttribute PeliculaActor relacion, BindingResult result, Model model) {
        if (result.hasErrors()) {
            cargarDatosFormulario(model);
            return "Api/PeliculasActores/crear"; // Si hay errores, vuelve al formulario
        }
        peliculaActorService.guardarPeliculaActor(relacion);
        return "redirect:/api/peliculas-actores"; // Redirige a la lista
    }


    // ==========================================================
    // Métodos para API JSON
    // ==========================================================

    @GetMapping("/json")
    public ResponseEntity<List<PeliculaActor>> obtenerTodasJson() {
        List<PeliculaActor> relaciones = peliculaActorService.obtenerPeliculaActores();
        return ResponseEntity.ok(relaciones);
    }

    @GetMapping("/json/{id}")
    public ResponseEntity<?> obtenerPorIdJson(@PathVariable Long id) {
        try {
            Optional<PeliculaActor> relacion = peliculaActorService.obtenerPorId(id);
            return ResponseEntity.ok(relacion.orElseThrow(() -> new RuntimeException("Relación Película-Actor no encontrada con ID: " + id)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/json")
    public ResponseEntity<?> crearJson(@Valid @RequestBody PeliculaActor relacion) {
        try {
            PeliculaActor nuevaRelacion = peliculaActorService.guardarPeliculaActor(relacion);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaRelacion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/json/{id}")
    public ResponseEntity<?> actualizarJson(@PathVariable Long id, @Valid @RequestBody PeliculaActor relacion) {
        try {
            PeliculaActor relacionActualizada = peliculaActorService.actualizarPeliculaActor(id, relacion);
            return ResponseEntity.ok(relacionActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/json/{id}")
    public ResponseEntity<?> eliminarJson(@PathVariable Long id) {
        try {
            peliculaActorService.eliminarPeliculaActor(id);
            return ResponseEntity.ok("Relación Película-Actor eliminada con éxito");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // ==========================================================
    // Métodos auxiliares
    // ==========================================================

    private void cargarDatosFormulario(Model model) {
        model.addAttribute("peliculas", peliculaService.obtenerPeliculas());
        model.addAttribute("actores", actorService.obtenerActores());
    }
}