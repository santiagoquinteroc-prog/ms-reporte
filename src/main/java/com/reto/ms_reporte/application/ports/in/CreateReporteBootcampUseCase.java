package com.reto.ms_reporte.application.ports.in;

import com.reto.ms_reporte.domain.ReporteBootcamp;
import reactor.core.publisher.Mono;

public interface CreateReporteBootcampUseCase {
	Mono<ReporteBootcamp> execute(ReporteBootcamp reporte);
}
