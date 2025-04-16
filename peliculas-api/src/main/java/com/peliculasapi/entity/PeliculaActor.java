package com.peliculasapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

@Data
@Entity
@ToString(exclude = {"pelicula", "actor"})
@Table(name = "peliculas_actores")
public class PeliculaActor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_pelicula", referencedColumnName = "id")
    private Pelicula pelicula;

    @ManyToOne
    @JoinColumn(name = "id_actor", referencedColumnName = "id")
    private Actor actor;

    @NotBlank(message = "El personaje no puede estar vacío.")
    @Size(max = 255, message = "El personaje no puede exceder los 255 caracteres.")
    private String personaje;

    private Boolean contratoFirmado;

    @PastOrPresent(message = "La fecha del contrato no puede ser en el futuro.")
    private LocalDate fechaContrato;

    @Positive(message = "El caché final debe ser un valor positivo.")
    private Double cacheFinal;

    @PositiveOrZero(message = "Los bonos extra deben ser un valor positivo o cero.")
    private Double bonosExtra;

    @Size(max = 500, message = "Los comentarios no pueden exceder los 500 caracteres.")
    private String comentarios;

    @PrePersist
    public void prePersist() {
        // Inicializar contrato como no firmado
        if (contratoFirmado == null) {
            this.contratoFirmado = false;
        }

        // Establecer fecha de contrato automáticamente si está firmado
        if (contratoFirmado && fechaContrato == null) {
            this.fechaContrato = LocalDate.now();
        }

        // Calcular caché final si es aplicable
        if (bonosExtra != null && cacheFinal == null) {
            this.cacheFinal = bonosExtra; // Aquí puedes agregar lógica adicional si el caché depende de otros factores
        }
    }

    @PreUpdate
    public void preUpdate() {
        // Actualizar fecha de contrato automáticamente si no está establecida y el contrato se firmó
        if (contratoFirmado && fechaContrato == null) {
            this.fechaContrato = LocalDate.now();
        }

        // Recalcular caché final en caso de cambios
        if (bonosExtra != null) {
            this.cacheFinal = bonosExtra; // Agregar lógica adicional si el caché depende de otros factores
        }
    }
}