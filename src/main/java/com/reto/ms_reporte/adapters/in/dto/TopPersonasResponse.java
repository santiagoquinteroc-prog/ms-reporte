package com.reto.ms_reporte.adapters.in.dto;

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
public class TopPersonasResponse {
	private String id;
	private String bootcampId;
	private String nombre;
	private String descripcion;
	private LocalDate fechaLanzamiento;
	private Integer duracionSemanas;
	private List<CapacidadRefDTO> capacidades;
	private List<TecnologiaRefDTO> tecnologias;
	private Integer cantidadCapacidades;
	private Integer cantidadTecnologias;
	private Integer cantidadPersonasInscritas;
	private List<PersonaSimpleDTO> personas;
}


