package com.reto.ms_reporte.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonaInscrita {
	private String id;
	private String nombre;
	private String email;
	private List<TecnologiaRef> tecnologias;
	private List<CapacidadRef> capacidades;
}


