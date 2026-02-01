package com.reto.ms_reporte.adapters.in.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateReporteBootcampRequest {
	@NotBlank(message = "bootcampId es requerido")
	private String bootcampId;
	@NotBlank(message = "nombre es requerido")
	private String nombre;
	@NotBlank(message = "descripcion es requerida")
	private String descripcion;
	@NotNull(message = "fechaLanzamiento es requerida")
	private LocalDate fechaLanzamiento;
	@NotNull(message = "duracionSemanas es requerida")
	private Integer duracionSemanas;
	@Valid
	private List<CapacidadRefDTO> capacidades;
	@Valid
	private List<TecnologiaRefDTO> tecnologias;
}

