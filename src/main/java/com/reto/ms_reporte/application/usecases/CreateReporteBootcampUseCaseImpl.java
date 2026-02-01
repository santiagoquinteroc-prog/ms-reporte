package com.reto.ms_reporte.application.usecases;

import com.reto.ms_reporte.application.ports.in.CreateReporteBootcampUseCase;
import com.reto.ms_reporte.application.ports.out.ReporteRepository;
import com.reto.ms_reporte.domain.ReporteBootcamp;
import com.reto.ms_reporte.domain.exceptions.BootcampIdDuplicadoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CreateReporteBootcampUseCaseImpl implements CreateReporteBootcampUseCase {
	private final ReporteRepository reporteRepository;

	@Override
	public Mono<ReporteBootcamp> execute(ReporteBootcamp reporte) {
		return reporteRepository.findByBootcampId(reporte.getBootcampId())
				.flatMap(existing -> Mono.<ReporteBootcamp>error(
						new BootcampIdDuplicadoException("El bootcampId ya existe: " + reporte.getBootcampId())))
				.switchIfEmpty(calculateDerivedFields(reporte)
						.flatMap(reporteRepository::save));
	}

	private Mono<ReporteBootcamp> calculateDerivedFields(ReporteBootcamp reporte) {
		int cantidadCapacidades = reporte.getCapacidades() != null ? reporte.getCapacidades().size() : 0;
		int cantidadTecnologias = reporte.getTecnologias() != null ? reporte.getTecnologias().size() : 0;
		
		reporte.setCantidadCapacidades(cantidadCapacidades);
		reporte.setCantidadTecnologias(cantidadTecnologias);
		reporte.setCantidadPersonasInscritas(0);
		if (reporte.getPersonas() == null) {
			reporte.setPersonas(new java.util.ArrayList<>());
		}
		
		return Mono.just(reporte);
	}
}


