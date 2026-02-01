package com.reto.ms_reporte.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReporteBootcamp {
	private String id;
	private String bootcampId;
	private String nombre;
	private String descripcion;
	private LocalDate fechaLanzamiento;
	private Integer duracionSemanas;
	private List<CapacidadRef> capacidades;
	private List<TecnologiaRef> tecnologias;
	private Integer cantidadCapacidades;
	private Integer cantidadTecnologias;
	private Integer cantidadPersonasInscritas;
	private List<PersonaInscrita> personas;

	public void agregarPersonaInscrita(PersonaInscrita persona) {
		if (personas == null) {
			personas = new ArrayList<>();
		}
		personas.add(persona);
		cantidadPersonasInscritas = personas.size();
	}
}


