package com.reto.ms_reporte.adapters.in.mapper;

import com.reto.ms_reporte.adapters.in.dto.CapacidadRefDTO;
import com.reto.ms_reporte.adapters.in.dto.CreateReporteBootcampRequest;
import com.reto.ms_reporte.adapters.in.dto.PersonaInscritaDTO;
import com.reto.ms_reporte.adapters.in.dto.PersonaSimpleDTO;
import com.reto.ms_reporte.adapters.in.dto.RegistrarInscripcionBootcampRequest;
import com.reto.ms_reporte.adapters.in.dto.ReporteBootcampResponse;
import com.reto.ms_reporte.adapters.in.dto.ReporteResponse;
import com.reto.ms_reporte.adapters.in.dto.TecnologiaRefDTO;
import com.reto.ms_reporte.adapters.in.dto.TopPersonasResponse;
import com.reto.ms_reporte.domain.CapacidadRef;
import com.reto.ms_reporte.domain.PersonaInscrita;
import com.reto.ms_reporte.domain.ReporteBootcamp;
import com.reto.ms_reporte.domain.TecnologiaRef;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReporteMapper {

	public ReporteBootcamp toDomain(com.reto.ms_reporte.adapters.in.dto.CreateReporteRequest request) {
		return ReporteBootcamp.builder()
				.bootcampId(request.getBootcampId())
				.nombre(request.getNombreBootcamp())
				.build();
	}

	public ReporteBootcamp toDomain(CreateReporteBootcampRequest request) {
		return ReporteBootcamp.builder()
				.bootcampId(request.getBootcampId())
				.nombre(request.getNombre())
				.descripcion(request.getDescripcion())
				.fechaLanzamiento(request.getFechaLanzamiento())
				.duracionSemanas(request.getDuracionSemanas())
				.capacidades(toCapacidadDomainList(request.getCapacidades()))
				.tecnologias(toTecnologiaDomainList(request.getTecnologias()))
				.build();
	}

	public ReporteBootcampResponse toBootcampResponse(ReporteBootcamp domain) {
		return ReporteBootcampResponse.builder()
				.id(domain.getId())
				.bootcampId(domain.getBootcampId())
				.nombre(domain.getNombre())
				.descripcion(domain.getDescripcion())
				.fechaLanzamiento(domain.getFechaLanzamiento())
				.duracionSemanas(domain.getDuracionSemanas())
				.capacidades(toCapacidadDTOList(domain.getCapacidades()))
				.tecnologias(toTecnologiaDTOList(domain.getTecnologias()))
				.cantidadCapacidades(domain.getCantidadCapacidades())
				.cantidadTecnologias(domain.getCantidadTecnologias())
				.cantidadPersonasInscritas(domain.getCantidadPersonasInscritas())
				.build();
	}

	public PersonaInscrita toDomain(PersonaInscritaDTO dto) {
		return PersonaInscrita.builder()
				.id(dto.getId())
				.nombre(dto.getNombre())
				.email(dto.getEmail())
				.tecnologias(toTecnologiaDomainList(dto.getTecnologias()))
				.capacidades(toCapacidadDomainList(dto.getCapacidades()))
				.build();
	}

	public ReporteResponse toResponse(ReporteBootcamp domain) {
		return ReporteResponse.builder()
				.id(domain.getId())
				.bootcampId(domain.getBootcampId())
				.nombreBootcamp(domain.getNombre())
				.personasInscritas(toPersonaDTOList(domain.getPersonas()))
				.build();
	}

	public PersonaInscrita toDomain(RegistrarInscripcionBootcampRequest request) {
		return PersonaInscrita.builder()
				.nombre(request.getNombre())
				.email(request.getCorreo())
				.build();
	}

	public TopPersonasResponse toTopPersonasResponse(ReporteBootcamp domain) {
		return TopPersonasResponse.builder()
				.id(domain.getId())
				.bootcampId(domain.getBootcampId())
				.nombre(domain.getNombre())
				.descripcion(domain.getDescripcion())
				.fechaLanzamiento(domain.getFechaLanzamiento())
				.duracionSemanas(domain.getDuracionSemanas())
				.capacidades(toCapacidadDTOList(domain.getCapacidades()))
				.tecnologias(toTecnologiaDTOList(domain.getTecnologias()))
				.cantidadCapacidades(domain.getCantidadCapacidades())
				.cantidadTecnologias(domain.getCantidadTecnologias())
				.cantidadPersonasInscritas(domain.getCantidadPersonasInscritas())
				.personas(toPersonaSimpleDTOList(domain.getPersonas()))
				.build();
	}

	public PersonaInscritaDTO toDTO(PersonaInscrita domain) {
		return PersonaInscritaDTO.builder()
				.id(domain.getId())
				.nombre(domain.getNombre())
				.email(domain.getEmail())
				.tecnologias(toTecnologiaDTOList(domain.getTecnologias()))
				.capacidades(toCapacidadDTOList(domain.getCapacidades()))
				.build();
	}

	private List<PersonaSimpleDTO> toPersonaSimpleDTOList(List<PersonaInscrita> personas) {
		if (personas == null) {
			return null;
		}
		return personas.stream()
				.map(p -> PersonaSimpleDTO.builder()
						.nombre(p.getNombre())
						.correo(p.getEmail())
						.build())
				.collect(Collectors.toList());
	}

	private List<PersonaInscritaDTO> toPersonaDTOList(List<PersonaInscrita> personas) {
		if (personas == null) {
			return null;
		}
		return personas.stream()
				.map(this::toDTO)
				.collect(Collectors.toList());
	}

	private List<TecnologiaRef> toTecnologiaDomainList(List<TecnologiaRefDTO> tecnologias) {
		if (tecnologias == null) {
			return null;
		}
		return tecnologias.stream()
				.map(t -> TecnologiaRef.builder()
						.id(t.getId())
						.nombre(t.getNombre())
						.build())
				.collect(Collectors.toList());
	}

	private List<TecnologiaRefDTO> toTecnologiaDTOList(List<TecnologiaRef> tecnologias) {
		if (tecnologias == null) {
			return null;
		}
		return tecnologias.stream()
				.map(t -> TecnologiaRefDTO.builder()
						.id(t.getId())
						.nombre(t.getNombre())
						.build())
				.collect(Collectors.toList());
	}

	private List<CapacidadRef> toCapacidadDomainList(List<CapacidadRefDTO> capacidades) {
		if (capacidades == null) {
			return null;
		}
		return capacidades.stream()
				.map(c -> CapacidadRef.builder()
						.id(c.getId())
						.nombre(c.getNombre())
						.build())
				.collect(Collectors.toList());
	}

	private List<CapacidadRefDTO> toCapacidadDTOList(List<CapacidadRef> capacidades) {
		if (capacidades == null) {
			return null;
		}
		return capacidades.stream()
				.map(c -> CapacidadRefDTO.builder()
						.id(c.getId())
						.nombre(c.getNombre())
						.build())
				.collect(Collectors.toList());
	}
}


