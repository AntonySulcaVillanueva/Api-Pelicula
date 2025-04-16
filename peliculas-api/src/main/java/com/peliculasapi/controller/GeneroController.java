package com.peliculasapi.controller;

import com.peliculasapi.entity.Genero;
import com.peliculasapi.service.GeneroService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/generos")
public class GeneroController {

    private final GeneroService generoService;

    // Constructor
    public GeneroController(GeneroService generoService) {
        this.generoService = generoService;
    }

    // ========================================================================
    // Métodos para vistas
    // ========================================================================

    @GetMapping
    public String obtenerTodos(Model model) {
        List<Genero> generos = generoService.obtenerGeneros();
        model.addAttribute("generos", generos);
        return "Api/Generos/listar";
    }

    @GetMapping("/{id}")
    public String obtenerPorId(@PathVariable Long id, Model model) {
        Optional<Genero> genero = generoService.obtenerPorId(id);
        if (genero.isPresent()) {
            model.addAttribute("genero", genero.get());
            return "Api/Generos/detalle";
        } else {
            model.addAttribute("error", "Género no encontrado");
            return "Api/Generos/error";
        }
    }

    @GetMapping("/crear")
    public String formularioCrear(Model model) {
        model.addAttribute("genero", new Genero());
        return "Api/Generos/crear";
    }

    @PostMapping("/guardar")
    public String crearGenero(@ModelAttribute Genero genero) {
        generoService.guardarGenero(genero);
        return "redirect:/api/generos";
    }

    @GetMapping("/editar/{id}")
    public String formularioActualizar(@PathVariable Long id, Model model) {
        Optional<Genero> genero = generoService.obtenerPorId(id);
        if (genero.isPresent()) {
            model.addAttribute("genero", genero.get());
            return "Api/Generos/editar";
        } else {
            model.addAttribute("error", "Género no encontrado");
            return "Api/Generos/error";
        }
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarGenero(@PathVariable Long id, @ModelAttribute Genero generoActualizado) {
        generoService.actualizarGenero(id, generoActualizado);
        return "redirect:/api/generos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarGeneroVista(@PathVariable Long id) {
        generoService.eliminarGenero(id);
        return "redirect:/api/generos";
    }

    // ========================================================================
    // Métodos para API (JSON)
    // ========================================================================

    @GetMapping("/json")
    public ResponseEntity<List<Genero>> obtenerTodosJson() {
        List<Genero> generos = generoService.obtenerGeneros();
        return ResponseEntity.ok(generos);
    }

    @GetMapping("/json/{id}")
    public ResponseEntity<Genero> obtenerPorIdJson(@PathVariable Long id) {
        return generoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/json")
    public ResponseEntity<Genero> crearJson(@RequestBody Genero genero) {
        try {
            Genero nuevoGenero = generoService.guardarGenero(genero);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoGenero);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/json/{id}")
    public ResponseEntity<Genero> actualizarJson(@PathVariable Long id, @RequestBody Genero generoActualizado) {
        try {
            Genero generoActual = generoService.actualizarGenero(id, generoActualizado);
            return ResponseEntity.ok(generoActual);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/json/{id}")
    public ResponseEntity<String> eliminarJson(@PathVariable Long id) {
        try {
            generoService.eliminarGenero(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Género eliminado con éxito");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Género no encontrado con ID: " + id);
        }
    }

}