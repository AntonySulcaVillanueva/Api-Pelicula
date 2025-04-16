package com.peliculasapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Data
@Entity
@Table(name = "actores")
public class Actor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 1000, message = "La URL de la foto no puede exceder los 500 caracteres.")
    @Pattern(regexp = "^(http|https)://.*$", message = "La URL de la foto debe ser válida.")
    private String fotoUrl;

    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres.")
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío.")
    @Size(max = 100, message = "El apellido no puede exceder los 100 caracteres.")
    private String apellido;

    @Past(message = "La fecha de nacimiento debe ser en el pasado.")
    private LocalDate fechaNacimiento;

    @NotBlank(message = "La nacionalidad no puede estar vacía.")
    @Size(max = 100, message = "La nacionalidad no puede exceder los 100 caracteres.")
    private String nacionalidad;

    @Email(message = "El email del agente debe ser válido.")
    private String emailAgente;

    @PositiveOrZero(message = "El caché estimado debe ser un valor positivo o cero.")
    private BigDecimal cacheEstimado;

    @Positive(message = "El salario mensual debe ser un valor positivo.")
    @Digits(integer = 10, fraction = 2, message = "El salario mensual debe tener un formato válido, con máximo 10 dígitos enteros y 2 decimales.")
    private BigDecimal salarioMensual;

    private BigDecimal totalPagado;

    @NotBlank(message = "El estado no puede estar vacío.")
    @Pattern(regexp = "^(Activo|Inactivo)$", message = "El estado debe ser 'Activo' o 'Inactivo'.")
    private String estado;

    @Column(updatable = false)
    private LocalDate fechaRegistro;

    @Size(max = 500, message = "Las notas no pueden exceder los 500 caracteres.")
    private String notas;

    @OneToMany(mappedBy = "actor", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // Evita la serialización de relaciones bidireccionales
    private List<PeliculaActor> peliculasActor;

    @PrePersist
    public void prePersist() {
        this.fechaRegistro = LocalDate.now();
        this.totalPagado = BigDecimal.ZERO;
    }

    @PostLoad
    public void calcularTotalPagado() {
        long meses = ChronoUnit.MONTHS.between(this.fechaRegistro.withDayOfMonth(1), LocalDate.now().withDayOfMonth(1));
        this.totalPagado = salarioMensual.multiply(BigDecimal.valueOf(meses));
    }
}