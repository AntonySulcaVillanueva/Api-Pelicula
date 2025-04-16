package com.peliculasapi.controller;

import com.peliculasapi.entity.Director;
import com.peliculasapi.service.DirectorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/directores")
public class DirectorController {

    private final DirectorService directorService;

    // Constructor
    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }

    // ========================================================================
    // Métodos para vistas
    // ========================================================================

    // Obtener todos los directores - Vista
    @GetMapping
    public String obtenerTodos(Model model) {
        List<Director> directores = directorService.obtenerDirectores();
        model.addAttribute("directores", directores);
        return "Api/Directores/listar"; // Renderiza la vista listar.html
    }

    // Obtener un director por ID - Vista
    @GetMapping("/{id}")
    public String obtenerPorId(@PathVariable Long id, Model model) {
        Optional<Director> director = directorService.obtenerPorId(id);
        if (director.isPresent()) {
            model.addAttribute("director", director.get());
            return "Api/Directores/detalle"; // Renderiza la vista detalle.html
        } else {
            model.addAttribute("error", "Director no encontrado");
            return "Api/Directores/error"; // Renderiza una vista de error genérica
        }
    }

    // Formulario para crear un nuevo director - Vista
    @GetMapping("/crear")
    public String formularioCrear(Model model) {
        model.addAttribute("director", new Director()); // Objeto vacío para el formulario
        return "Api/Directores/crear"; // Renderiza la vista crear.html
    }

    // Guardar un nuevo director - Vista
    @PostMapping("/guardar")
    public String crearDirector(@ModelAttribute Director director) {
        directorService.guardarDirector(director);
        return "redirect:/api/directores"; // Redirige a la lista de directores
    }

    // Formulario para actualizar un director existente - Vista
    @GetMapping("/editar/{id}")
    public String formularioActualizar(@PathVariable Long id, Model model) {
        Optional<Director> director = directorService.obtenerPorId(id);
        if (director.isPresent()) {
            model.addAttribute("director", director.get());
            return "Api/Directores/editar"; // Renderiza la vista editar.html
        } else {
            model.addAttribute("error", "Director no encontrado");
            return "Api/Directores/error"; // Renderiza una vista de error genérica
        }
    }

    // Actualizar un director existente - Vista
    @PostMapping("/actualizar/{id}")
    public String actualizarDirector(@PathVariable Long id, @ModelAttribute Director directorActualizado) {
        directorService.actualizarDirector(id, directorActualizado);
        return "redirect:/api/directores"; // Redirige a la lista de directores
    }

    // Eliminar un director por ID - Vista
    @GetMapping("/eliminar/{id}")
    public String eliminarDirectorVista(@PathVariable Long id) {
        directorService.eliminarDirector(id);
        return "redirect:/api/directores"; // Redirige a la lista de directores
    }

    // ========================================================================
    // Métodos para API (JSON)
    // ========================================================================

    // Obtener todos los directores - API
    @GetMapping("/json")
    public ResponseEntity<List<Director>> obtenerTodosJson() {
        List<Director> directores = directorService.obtenerDirectores();
        return ResponseEntity.ok(directores);
    }

    // Obtener un director por ID - API
    @GetMapping("/json/{id}")
    public ResponseEntity<Director> obtenerPorIdJson(@PathVariable Long id) {
        return directorService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Crear un nuevo director - API
    @PostMapping("/json")
    public ResponseEntity<Director> crearJson(@RequestBody Director director) {
        try {
            Director nuevoDirector = directorService.guardarDirector(director);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoDirector);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Actualizar un director existente - API
    @PutMapping("/json/{id}")
    public ResponseEntity<Director> actualizarJson(@PathVariable Long id, @RequestBody Director directorActualizado) {
        try {
            Director directorActual = directorService.actualizarDirector(id, directorActualizado);
            return ResponseEntity.ok(directorActual);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Eliminar un director por ID - API
    @DeleteMapping("/json/{id}")
    public ResponseEntity<String> eliminarJson(@PathVariable Long id) {
        try {
            directorService.eliminarDirector(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Director eliminado con éxito");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Director no encontrado con ID: " + id);
        }
    }
}