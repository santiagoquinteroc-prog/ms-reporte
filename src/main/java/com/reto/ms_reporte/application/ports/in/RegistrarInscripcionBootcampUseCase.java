package com.reto.ms_reporte.application.ports.in;

import com.reto.ms_reporte.domain.PersonaInscrita;
import reactor.core.publisher.Mono;

public interface RegistrarInscripcionBootcampUseCase {
	Mono<Void> execute(String bootcampId, PersonaInscrita persona);
}


