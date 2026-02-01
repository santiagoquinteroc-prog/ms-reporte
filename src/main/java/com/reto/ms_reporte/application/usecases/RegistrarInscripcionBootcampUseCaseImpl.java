package com.reto.ms_reporte.application.usecases;

import com.reto.ms_reporte.application.ports.in.RegistrarInscripcionBootcampUseCase;
import com.reto.ms_reporte.application.ports.out.ReporteRepository;
import com.reto.ms_reporte.domain.PersonaInscrita;
import com.reto.ms_reporte.domain.ReporteBootcamp;
import com.reto.ms_reporte.domain.exceptions.PersonaDuplicadaException;
import com.reto.ms_reporte.domain.exceptions.ReporteNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RegistrarInscripcionBootcampUseCaseImpl implements RegistrarInscripcionBootcampUseCase {
	private final ReporteRepository reporteRepository;

	@Override
	public Mono<Void> execute(String bootcampId, PersonaInscrita persona) {
		return reporteRepository.findByBootcampId(bootcampId)
				.switchIfEmpty(Mono.error(new ReporteNotFoundException("Reporte no encontrado para bootcampId: " + bootcampId)))
				.flatMap(reporte -> {
					if (reporte.getPersonas() != null && reporte.getPersonas().stream()
							.anyMatch(p -> p.getEmail() != null && p.getEmail().equals(persona.getEmail()))) {
						return Mono.error(new PersonaDuplicadaException("La persona con correo " + persona.getEmail() + " ya está inscrita"));
					}
					reporte.agregarPersonaInscrita(persona);
					return reporteRepository.save(reporte);
				})
				.then();
	}
}


