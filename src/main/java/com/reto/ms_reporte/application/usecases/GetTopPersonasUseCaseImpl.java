package com.reto.ms_reporte.application.usecases;

import com.reto.ms_reporte.application.ports.in.GetTopPersonasUseCase;
import com.reto.ms_reporte.application.ports.out.ReporteRepository;
import com.reto.ms_reporte.domain.ReporteBootcamp;
import com.reto.ms_reporte.domain.exceptions.ReporteNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GetTopPersonasUseCaseImpl implements GetTopPersonasUseCase {
	private final ReporteRepository reporteRepository;

	@Override
	public Mono<ReporteBootcamp> execute() {
		return reporteRepository.findTopByCantidadPersonasInscritas()
				.switchIfEmpty(Mono.error(new ReporteNotFoundException("No hay reportes disponibles")));
	}
}


