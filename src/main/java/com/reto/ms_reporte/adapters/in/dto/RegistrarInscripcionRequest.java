package com.reto.ms_reporte.adapters.in.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrarInscripcionRequest {
	@Valid
	@NotNull(message = "persona es requerida")
	private PersonaInscritaDTO persona;
}

