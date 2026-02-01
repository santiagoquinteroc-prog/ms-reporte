package com.reto.ms_reporte.adapters.out.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Document(collection = "reportes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReporteBootcampDocument {
	@Id
	private String id;
	private String bootcampId;
	private String nombre;
	private String descripcion;
	private LocalDate fechaLanzamiento;
	private Integer duracionSemanas;
	private List<CapacidadRefDocument> capacidades;
	private List<TecnologiaRefDocument> tecnologias;
	private Integer cantidadCapacidades;
	private Integer cantidadTecnologias;
	private Integer cantidadPersonasInscritas;
	private List<PersonaInscritaDocument> personas;
}


