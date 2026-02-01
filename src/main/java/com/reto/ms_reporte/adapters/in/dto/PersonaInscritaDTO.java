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
public class PersonaInscritaDTO {
	private String id;
	private String nombre;
	private String email;
	private List<TecnologiaRefDTO> tecnologias;
	private List<CapacidadRefDTO> capacidades;
}


