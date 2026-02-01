package com.reto.ms_reporte.application.ports.in;

import com.reto.ms_reporte.domain.PersonaInscrita;
import reactor.core.publisher.Mono;

public interface RegistrarInscripcionEnReporteUseCase {
	Mono<Void> execute(String reporteId, PersonaInscrita persona);
}


