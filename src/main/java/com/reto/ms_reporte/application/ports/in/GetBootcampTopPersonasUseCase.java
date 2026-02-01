package com.reto.ms_reporte.application.ports.in;

import com.reto.ms_reporte.domain.PersonaInscrita;
import reactor.core.publisher.Flux;

public interface GetBootcampTopPersonasUseCase {
	Flux<PersonaInscrita> execute(String bootcampId, int top);
}


