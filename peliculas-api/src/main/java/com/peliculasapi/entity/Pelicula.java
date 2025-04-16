package com.peliculasapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "peliculas")
public class Pelicula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 500, message = "La URL de la imagen no puede exceder los 500 caracteres.")
    @Pattern(regexp = "^(http|https)://.*$", message = "La URL de la imagen debe ser válida.")
    private String imagenUrl;

    @NotBlank(message = "El título no puede estar vacío.")
    @Size(max = 255, message = "El título no puede exceder los 255 caracteres.")
    private String titulo;

    @Size(max = 255, message = "El subtítulo no puede exceder los 255 caracteres.")
    private String subtitulo;

    @Positive(message = "El año debe ser un valor positivo.")
    private Integer año;

    @Positive(message = "La duración debe ser un valor positivo.")
    private Integer duracion;

    @NotBlank(message = "La clasificación no puede estar vacía.")
    private String clasificacion;

    @NotBlank(message = "El idioma original no puede estar vacío.")
    private String idiomaOriginal;

    @NotBlank(message = "La sinopsis no puede estar vacía.")
    @Size(max = 1000, message = "La sinopsis no puede exceder los 1000 caracteres.")
    private String sinopsis;

    @Positive(message = "El presupuesto total debe ser un valor positivo.")
    private BigDecimal presupuestoTotal;

    @Positive(message = "Los gastos generales deben ser un valor positivo.")
    private BigDecimal gastosGenerales;

    @Positive(message = "Los ingresos estimados deben ser un valor positivo.")
    private BigDecimal ingresosEstimados;

    private BigDecimal ingresosReales;

    @Past(message = "La fecha de lanzamiento público debe ser en el pasado.")
    private LocalDate fechaLanzamientoPublico;

    @NotBlank(message = "El estado de la película no puede estar vacío.")
    @Pattern(regexp = "^(En Producción|Estrenada|Cancelada)$", message = "El estado debe ser 'En Producción', 'Estrenada' o 'Cancelada'.")
    private String estado;

    private LocalDateTime creadoEn;

    private LocalDateTime actualizadoEn;

    @ManyToOne
    @JoinColumn(name = "id_genero", nullable = false)
    private Genero genero;

    @ManyToOne
    @JoinColumn(name = "id_director", referencedColumnName = "id")
    private Director director;

    @OneToMany(mappedBy = "pelicula", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // Evita la serialización de relaciones bidireccionales
    private List<PeliculaActor> peliculasActor;

    @PrePersist
    public void prePersist() {
        this.creadoEn = LocalDateTime.now();
        this.actualizadoEn = LocalDateTime.now();
        calcularIngresosReales();
    }

    @PreUpdate
    public void preUpdate() {
        this.actualizadoEn = LocalDateTime.now();
        calcularIngresosReales();
    }

    public void calcularIngresosReales() {
        if (this.ingresosEstimados != null && this.gastosGenerales != null) {
            this.ingresosReales = this.ingresosEstimados.subtract(this.gastosGenerales);
        }
    }
}