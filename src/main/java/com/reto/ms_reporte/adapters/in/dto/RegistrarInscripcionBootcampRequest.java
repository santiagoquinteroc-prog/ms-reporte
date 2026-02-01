package com.reto.ms_reporte.adapters.in.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrarInscripcionBootcampRequest {
	@NotBlank(message = "nombre es requerido")
	private String nombre;
	@NotBlank(message = "correo es requerido")
	@Email(message = "correo debe ser válido")
	private String correo;
}


