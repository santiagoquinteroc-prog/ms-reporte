package com.reto.ms_reporte.adapters.in.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonaSimpleDTO {
	private String nombre;
	private String correo;
}


