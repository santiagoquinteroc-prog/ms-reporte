package com.reto.ms_reporte.adapters.out.adapter;

import com.reto.ms_reporte.adapters.out.document.CapacidadRefDocument;
import com.reto.ms_reporte.adapters.out.document.PersonaInscritaDocument;
import com.reto.ms_reporte.adapters.out.document.ReporteBootcampDocument;
import com.reto.ms_reporte.adapters.out.document.TecnologiaRefDocument;
import com.reto.ms_reporte.adapters.out.repository.ReporteMongoRepository;
import com.reto.ms_reporte.application.ports.out.ReporteRepository;
import com.reto.ms_reporte.domain.CapacidadRef;
import com.reto.ms_reporte.domain.PersonaInscrita;
import com.reto.ms_reporte.domain.ReporteBootcamp;
import com.reto.ms_reporte.domain.TecnologiaRef;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReporteMongoAdapter implements ReporteRepository {
	private final ReporteMongoRepository repository;

	@Override
	public Mono<ReporteBootcamp> save(ReporteBootcamp reporte) {
		ReporteBootcampDocument document = toDocument(reporte);
		return repository.save(document)
				.map(this::toDomain);
	}

	@Override
	public Mono<ReporteBootcamp> findById(String id) {
		return repository.findById(id)
				.map(this::toDomain);
	}

	@Override
	public Mono<ReporteBootcamp> findByBootcampId(String bootcampId) {
		return repository.findByBootcampId(bootcampId)
				.map(this::toDomain);
	}

	private ReporteBootcampDocument toDocument(ReporteBootcamp domain) {
		return ReporteBootcampDocument.builder()
				.id(domain.getId())
				.bootcampId(domain.getBootcampId())
				.nombre(domain.getNombre())
				.descripcion(domain.getDescripcion())
				.fechaLanzamiento(domain.getFechaLanzamiento())
				.duracionSemanas(domain.getDuracionSemanas())
				.capacidades(toCapacidadDocumentList(domain.getCapacidades()))
				.tecnologias(toTecnologiaDocumentList(domain.getTecnologias()))
				.cantidadCapacidades(domain.getCantidadCapacidades())
				.cantidadTecnologias(domain.getCantidadTecnologias())
				.cantidadPersonasInscritas(domain.getCantidadPersonasInscritas())
				.personas(toPersonaDocumentList(domain.getPersonas()))
				.build();
	}

	private ReporteBootcamp toDomain(ReporteBootcampDocument document) {
		return ReporteBootcamp.builder()
				.id(document.getId())
				.bootcampId(document.getBootcampId())
				.nombre(document.getNombre())
				.descripcion(document.getDescripcion())
				.fechaLanzamiento(document.getFechaLanzamiento())
				.duracionSemanas(document.getDuracionSemanas())
				.capacidades(toCapacidadDomainList(document.getCapacidades()))
				.tecnologias(toTecnologiaDomainList(document.getTecnologias()))
				.cantidadCapacidades(document.getCantidadCapacidades())
				.cantidadTecnologias(document.getCantidadTecnologias())
				.cantidadPersonasInscritas(document.getCantidadPersonasInscritas())
				.personas(toPersonaDomainList(document.getPersonas()))
				.build();
	}

	private List<PersonaInscritaDocument> toPersonaDocumentList(List<PersonaInscrita> personas) {
		if (personas == null) {
			return null;
		}
		return personas.stream()
				.map(this::toPersonaDocument)
				.collect(Collectors.toList());
	}

	private PersonaInscritaDocument toPersonaDocument(PersonaInscrita persona) {
		return PersonaInscritaDocument.builder()
				.id(persona.getId())
				.nombre(persona.getNombre())
				.email(persona.getEmail())
				.tecnologias(toTecnologiaDocumentList(persona.getTecnologias()))
				.capacidades(toCapacidadDocumentList(persona.getCapacidades()))
				.build();
	}

	private List<PersonaInscrita> toPersonaDomainList(List<PersonaInscritaDocument> personas) {
		if (personas == null) {
			return null;
		}
		return personas.stream()
				.map(this::toPersonaDomain)
				.collect(Collectors.toList());
	}

	private PersonaInscrita toPersonaDomain(PersonaInscritaDocument persona) {
		return PersonaInscrita.builder()
				.id(persona.getId())
				.nombre(persona.getNombre())
				.email(persona.getEmail())
				.tecnologias(toTecnologiaDomainList(persona.getTecnologias()))
				.capacidades(toCapacidadDomainList(persona.getCapacidades()))
				.build();
	}

	private List<TecnologiaRefDocument> toTecnologiaDocumentList(List<TecnologiaRef> tecnologias) {
		if (tecnologias == null) {
			return null;
		}
		return tecnologias.stream()
				.map(t -> TecnologiaRefDocument.builder()
						.id(t.getId())
						.nombre(t.getNombre())
						.build())
				.collect(Collectors.toList());
	}

	private List<TecnologiaRef> toTecnologiaDomainList(List<TecnologiaRefDocument> tecnologias) {
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

	private List<CapacidadRefDocument> toCapacidadDocumentList(List<CapacidadRef> capacidades) {
		if (capacidades == null) {
			return null;
		}
		return capacidades.stream()
				.map(c -> CapacidadRefDocument.builder()
						.id(c.getId())
						.nombre(c.getNombre())
						.build())
				.collect(Collectors.toList());
	}

	private List<CapacidadRef> toCapacidadDomainList(List<CapacidadRefDocument> capacidades) {
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
}


