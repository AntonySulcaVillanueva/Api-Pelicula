package com.peliculasapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Data
@Entity
@Table(name = "directores")
public class Director {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Email(message = "El email debe ser válido.")
    private String emailContacto;

    @Size(max = 15, message = "El teléfono de contacto no puede exceder los 15 caracteres.")
    private String telefonoContacto;

    @Size(max = 255, message = "La dirección de la oficina no puede exceder los 255 caracteres.")
    private String direccionOficina;

    @Size(max = 500, message = "La URL de la imagen no puede exceder los 500 caracteres.")
    @Pattern(regexp = "^(http|https)://.*$", message = "La URL de la imagen debe ser válida.")
    private String imagenUrl;

    @Positive(message = "El salario mensual debe ser un valor positivo.")
    private BigDecimal salarioMensual;

    @Column(updatable = false)
    private LocalDate fechaRegistro;

    private BigDecimal totalPagado;

    @NotBlank(message = "El estado no puede estar vacío.")
    @Pattern(regexp = "^(Activo|Inactivo)$", message = "El estado debe ser 'Activo' o 'Inactivo'.")
    private String estado;

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