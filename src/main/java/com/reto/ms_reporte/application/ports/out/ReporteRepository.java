package com.reto.ms_reporte.application.ports.out;

import com.reto.ms_reporte.domain.ReporteBootcamp;
import reactor.core.publisher.Mono;

public interface ReporteRepository {
	Mono<ReporteBootcamp> save(ReporteBootcamp reporte);
	Mono<ReporteBootcamp> findById(String id);
	Mono<ReporteBootcamp> findByBootcampId(String bootcampId);
}


