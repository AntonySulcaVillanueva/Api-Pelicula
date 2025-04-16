package com.peliculasapi.controller;

import com.peliculasapi.entity.Actor;
import com.peliculasapi.entity.Director;
import com.peliculasapi.entity.Genero;
import com.peliculasapi.entity.Pelicula;
import com.peliculasapi.service.ActorService;
import com.peliculasapi.service.DirectorService;
import com.peliculasapi.service.GeneroService;
import com.peliculasapi.service.PeliculaService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/peliculas")
public class PeliculaController {

    private static final Logger logger = LoggerFactory.getLogger(PeliculaController.class);

    private final PeliculaService peliculaService;
    private final GeneroService generoService;
    private final DirectorService directorService;
    private final ActorService actorService;

    // Constructor
    public PeliculaController(PeliculaService peliculaService, GeneroService generoService, DirectorService directorService, ActorService actorService) {
        this.peliculaService = peliculaService;
        this.generoService = generoService;
        this.directorService = directorService;
        this.actorService = actorService;
    }

    // ========================================================================
    // Métodos para vistas
    // ========================================================================

    // Manejador de excepciones general para este controlador
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        logger.error("Error en el controlador: {}", e.getMessage(), e);
        model.addAttribute("error", "Ocurrió un error inesperado: " + e.getMessage());
        return "Api/Peliculas/error";
    }

    // Obtener todas las películas - Vista
    @GetMapping
    public String obtenerTodas(Model model) {
        List<Pelicula> peliculas = peliculaService.obtenerPeliculas();
        model.addAttribute("peliculas", peliculas);
        return "Api/Peliculas/listar"; // Renderiza la vista listar.html
    }

    // Obtener una película por ID - Vista
    @GetMapping("/{id}")
    public String obtenerPorId(@PathVariable Long id, Model model) {
        Optional<Pelicula> pelicula = peliculaService.obtenerPorId(id);
        if (pelicula.isPresent()) {
            model.addAttribute("pelicula", pelicula.get());
            return "Api/Peliculas/detalle"; // Renderiza la vista detalle.html
        } else {
            model.addAttribute("error", "Película no encontrada");
            return "Api/Peliculas/error"; // Renderiza una vista de error genérica
        }
    }

    // Formulario para crear una nueva película - Vista
    @GetMapping("/crear")
    public String formularioCrear(Model model) {
        try {
            // Cargar los datos necesarios para el formulario
            cargarDatosFormulario(model);
            // Añadir una nueva instancia de Pelicula para el formulario
            model.addAttribute("pelicula", new Pelicula());
            return "Api/Peliculas/crear"; // Renderiza la vista crear.html
        } catch (Exception e) {
            // Manejo de errores en la carga del formulario
            logger.error("Error al cargar el formulario de creación: {}", e.getMessage(), e);
            model.addAttribute("error", "Ocurrió un error al cargar el formulario: " + e.getMessage());
            return "Api/Peliculas/error"; // Redirige a la vista de error
        }
    }

    // Guardar una nueva película desde formulario - Vista
    @PostMapping("/guardar")
    public String crearPelicula(@Valid @ModelAttribute Pelicula pelicula, BindingResult result, Model model) {
        // Verificar si hay errores de validación en los datos enviados
        if (result.hasErrors()) {
            // Si hay errores, recargar los datos necesarios para el formulario
            cargarDatosFormulario(model);
            // Volver a la vista de creación con los errores
            return "Api/Peliculas/crear";
        }
        // Si no hay errores, guardar la película en la base de datos
        peliculaService.guardarPelicula(pelicula);
        // Redirigir a la lista de películas
        return "redirect:/api/peliculas";
    }
    // Formulario para editar una película existente
    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable Long id, Model model) {
        Optional<Pelicula> pelicula = peliculaService.obtenerPorId(id);
        if (pelicula.isPresent()) {
            cargarDatosFormulario(model); // Carga las listas de generos y directores
            model.addAttribute("pelicula", pelicula.get());
            return "Api/Peliculas/editar"; // Renderiza la vista editar.html
        } else {
            model.addAttribute("error", "Película no encontrada");
            return "Api/Peliculas/error"; // Renderiza la vista error.html
        }
    }



    // Actualizar una película existente
    @PostMapping("/actualizar/{id}")
    public String actualizarPelicula(@PathVariable Long id, @Valid @ModelAttribute Pelicula pelicula, BindingResult result, Model model) {
        if (result.hasErrors()) {
            cargarDatosFormulario(model);
            return "Api/Peliculas/editar"; // Renderiza la vista editar.html con errores
        }
        peliculaService.actualizarPelicula(id, pelicula);
        return "redirect:/api/peliculas"; // Redirige a la lista de películas
    }

    // Eliminar una película
    @GetMapping("/eliminar/{id}")
    public String eliminarPelicula(@PathVariable Long id, Model model) {
        try {
            peliculaService.eliminarPelicula(id);
            return "redirect:/api/peliculas";
        } catch (DataIntegrityViolationException ex) {
            model.addAttribute("error", "No se pudo eliminar la película porque está relacionada con otros datos.");
            return "Api/Peliculas/error";
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            return "Api/Peliculas/error";
        }
    }




    // ========================================================================
    // Métodos para API (JSON)
    // ========================================================================

    // Obtener todas las películas - API
    @GetMapping("/json")
    public ResponseEntity<List<Pelicula>> obtenerTodasJson() {
        List<Pelicula> peliculas = peliculaService.obtenerPeliculas();
        return ResponseEntity.ok(peliculas);
    }

    // Obtener una película por ID - API
    @GetMapping("/json/{id}")
    public ResponseEntity<?> obtenerPorIdJson(@PathVariable Long id) {
        Optional<Pelicula> pelicula = peliculaService.obtenerPorId(id);
        if (pelicula.isPresent()) {
            return ResponseEntity.ok(pelicula.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: Película no encontrada con ID: " + id);
        }
    }

    // Crear una nueva película - API
    @PostMapping("/json")
    public ResponseEntity<Pelicula> crearJson(@Valid @RequestBody Pelicula pelicula) {
        Pelicula nuevaPelicula = peliculaService.guardarPelicula(pelicula);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaPelicula);
    }

    // Actualizar una película existente - API
    @PutMapping("/json/{id}")
    public ResponseEntity<?> actualizarJson(@PathVariable Long id, @Valid @RequestBody Pelicula peliculaActualizada) {
        Optional<Pelicula> peliculaExistente = peliculaService.obtenerPorId(id);

        if (peliculaExistente.isPresent()) {
            Pelicula peliculaActualizadaResultado = peliculaService.actualizarPelicula(id, peliculaActualizada);
            return ResponseEntity.ok(peliculaActualizadaResultado);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: No se pudo actualizar. Película no encontrada con ID: " + id);
        }
    }

    // Eliminar una película por ID - API
    @DeleteMapping("/json/{id}")
    public ResponseEntity<String> eliminarJson(@PathVariable Long id) {
        try {
            peliculaService.eliminarPelicula(id);
            return ResponseEntity.status(HttpStatus.OK).body("Película eliminada con éxito");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Película no encontrada con ID: " + id);
        }
    }

    // ========================================================================
    // Métodos auxiliares
    // ========================================================================

    private void cargarDatosFormulario(Model model) {
        List<Genero> generos = generoService.obtenerGeneros();
        List<Director> directores = directorService.obtenerDirectores();
        List<Actor> actores = actorService.obtenerActores();

        if (generos == null || generos.isEmpty() || directores == null || directores.isEmpty() || actores == null || actores.isEmpty()) {
            throw new RuntimeException("No se pudieron cargar los datos necesarios para el formulario.");
        }

        model.addAttribute("generos", generos);
        model.addAttribute("directores", directores);
        model.addAttribute("actores", actores);
    }
}