package com.peliculasapi.controller;

import com.peliculasapi.entity.Actor;
import com.peliculasapi.service.ActorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/api/actores")
public class ActorController {

    private final ActorService actorService;

    // Constructor
    public ActorController(ActorService actorService) {
        this.actorService = actorService;
    }

    // ========================================================================
    // Métodos para vistas
    // ========================================================================

    // Obtener todos los actores - Vista
    @GetMapping
    public String obtenerTodos(Model model) {
        List<Actor> actores = actorService.obtenerActores();
        model.addAttribute("actores", actores);
        return "Api/Actores/listar"; // Renderiza la vista listar.html
    }

    // Obtener un actor por ID - Vista
    @GetMapping("/{id}")
    public String obtenerPorId(@PathVariable Long id, Model model) {
        Optional<Actor> actor = actorService.obtenerPorId(id);
        if (actor.isPresent()) {
            model.addAttribute("actor", actor.get());
            return "Api/Actores/detalle"; // Renderiza la vista detalle.html
        } else {
            model.addAttribute("error", "Actor no encontrado");
            return "Api/Actores/error"; // Renderiza una vista de error genérica
        }
    }

    // Formulario para crear un nuevo actor - Vista
    @GetMapping("/crear")
    public String formularioCrear(Model model) {
        model.addAttribute("actor", new Actor()); // Objeto vacío para el formulario
        return "Api/Actores/crear"; // Renderiza la vista crear.html
    }

    // Guardar un nuevo actor - Vista
    @PostMapping("/guardar")
    public String crearActor(@ModelAttribute Actor actor) {
        actorService.guardarActor(actor);
        return "redirect:/api/actores"; // Redirige a la lista de actores
    }

    // Formulario para actualizar un actor existente - Vista
    @GetMapping("/editar/{id}")
    public String formularioActualizar(@PathVariable Long id, Model model) {
        Optional<Actor> actor = actorService.obtenerPorId(id);
        if (actor.isPresent()) {
            model.addAttribute("actor", actor.get());
            return "Api/Actores/editar"; // Renderiza la vista editar.html
        } else {
            model.addAttribute("error", "Actor no encontrado");
            return "Api/Actores/error"; // Renderiza una vista de error genérica
        }
    }

    // Actualizar un actor existente - Vista
    @PostMapping("/actualizar/{id}")
    public String actualizarActor(@PathVariable Long id, @ModelAttribute Actor actorActualizado) {
        actorService.actualizarActor(id, actorActualizado);
        return "redirect:/api/actores"; // Redirige a la lista de actores
    }

    // Eliminar un actor por ID - Vista
    @GetMapping("/eliminar/{id}")
    public String eliminarActorVista(@PathVariable Long id) {
        actorService.eliminarActor(id);
        return "redirect:/api/actores"; // Redirige a la lista de actores
    }

    // ========================================================================
    // Métodos para API (JSON)
    // ========================================================================

    // Obtener todos los actores - API
    @GetMapping("/json")
    public ResponseEntity<?> obtenerTodosJson() {
        List<Actor> actores = actorService.obtenerActores();
        if (actores.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body("No se encontraron actores en la base de datos.");
        }
        return ResponseEntity.ok(actores);
    }

    // Obtener un actor por ID - API
    @GetMapping("/json/{id}")
    public ResponseEntity<String> obtenerPorIdJson(@PathVariable Long id) {
        Optional<Actor> actor = actorService.obtenerPorId(id);
        return actor.map(value -> ResponseEntity.ok("Actor encontrado: " + value.toString())).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Error: Actor no encontrado con ID: " + id));
    }

    // Crear un nuevo actor - API
        @PostMapping("/json")
        public ResponseEntity<?> crearJson(@RequestBody @Valid Actor actor) {
            try {
                Actor nuevoActor = actorService.guardarActor(actor);
                return ResponseEntity.status(HttpStatus.CREATED).body(nuevoActor);
            } catch (jakarta.validation.ConstraintViolationException e) {
                List<String> errores = e.getConstraintViolations().stream()
                        .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                        .toList();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("mensaje", "Errores de validación", "detalles", errores));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error inesperado al crear el actor. Detalles: " + e.getMessage());
            }
        }
    // Actualizar un actor existente - API
    @PutMapping("/json/{id}")
    public ResponseEntity<?> actualizarJson(@PathVariable Long id, @RequestBody Actor actorActualizado) {
        try {
            Actor actorActual = actorService.actualizarActor(id, actorActualizado);
            return ResponseEntity.ok(actorActual);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró el actor con ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("No se pudo actualizar el actor. Verifica los datos ingresados.");
        }
    }

    // Eliminar un actor por ID - API
    @DeleteMapping("/json/{id}")
    public ResponseEntity<String> eliminarJson(@PathVariable Long id) {
        try {
            actorService.eliminarActor(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Actor eliminado con éxito");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Actor no encontrado con ID: " + id);
        }
    }
}