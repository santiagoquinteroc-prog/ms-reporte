package com.reto.ms_reporte.adapters.in.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateReporteRequest {
	@NotBlank(message = "bootcampId es requerido")
	private String bootcampId;
	@NotBlank(message = "nombreBootcamp es requerido")
	private String nombreBootcamp;
}


