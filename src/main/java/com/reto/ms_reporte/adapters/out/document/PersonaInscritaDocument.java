package com.reto.ms_reporte.adapters.out.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonaInscritaDocument {
	private String id;
	private String nombre;
	private String email;
	private List<TecnologiaRefDocument> tecnologias;
	private List<CapacidadRefDocument> capacidades;
}


