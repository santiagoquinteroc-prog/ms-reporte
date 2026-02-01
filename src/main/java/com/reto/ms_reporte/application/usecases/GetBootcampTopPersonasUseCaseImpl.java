package com.reto.ms_reporte.application.usecases;

import com.reto.ms_reporte.application.ports.in.GetBootcampTopPersonasUseCase;
import com.reto.ms_reporte.application.ports.out.ReporteRepository;
import com.reto.ms_reporte.domain.PersonaInscrita;
import com.reto.ms_reporte.domain.exceptions.ReporteNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetBootcampTopPersonasUseCaseImpl implements GetBootcampTopPersonasUseCase {
	private final ReporteRepository reporteRepository;

	@Override
	public Flux<PersonaInscrita> execute(String bootcampId, int top) {
		return reporteRepository.findByBootcampId(bootcampId)
				.switchIfEmpty(Mono.error(new ReporteNotFoundException("Reporte no encontrado para bootcamp: " + bootcampId)))
				.flatMapMany(reporte -> {
					var personas = reporte.getPersonas() != null
							? reporte.getPersonas().stream()
							.sorted(Comparator.comparing((PersonaInscrita p) -> 
								p.getTecnologias() != null ? p.getTecnologias().size() : 0)
								.thenComparing(p -> p.getCapacidades() != null ? p.getCapacidades().size() : 0)
								.reversed())
							.limit(top)
							.collect(Collectors.toList())
							: java.util.Collections.<PersonaInscrita>emptyList();
					return Flux.fromIterable(personas);
				});
	}
}

