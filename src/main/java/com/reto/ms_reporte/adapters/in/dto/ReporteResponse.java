package com.reto.ms_reporte.adapters.in.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReporteResponse {
	private String id;
	private String bootcampId;
	private String nombreBootcamp;
	private List<PersonaInscritaDTO> personasInscritas;
}


