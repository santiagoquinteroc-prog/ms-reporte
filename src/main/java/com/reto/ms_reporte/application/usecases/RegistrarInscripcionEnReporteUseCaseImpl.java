package com.reto.ms_reporte.application.usecases;

import com.reto.ms_reporte.application.ports.in.RegistrarInscripcionEnReporteUseCase;
import com.reto.ms_reporte.application.ports.out.ReporteRepository;
import com.reto.ms_reporte.domain.PersonaInscrita;
import com.reto.ms_reporte.domain.exceptions.ReporteNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RegistrarInscripcionEnReporteUseCaseImpl implements RegistrarInscripcionEnReporteUseCase {
	private final ReporteRepository reporteRepository;

	@Override
	public Mono<Void> execute(String reporteId, PersonaInscrita persona) {
		return reporteRepository.findById(reporteId)
				.switchIfEmpty(Mono.error(new ReporteNotFoundException("Reporte no encontrado: " + reporteId)))
				.flatMap(reporte -> {
					reporte.agregarPersonaInscrita(persona);
					return reporteRepository.save(reporte);
				})
				.then();
	}
}

