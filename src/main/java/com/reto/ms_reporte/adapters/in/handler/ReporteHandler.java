package com.reto.ms_reporte.adapters.in.handler;

import com.reto.ms_reporte.adapters.in.dto.CreateReporteBootcampRequest;
import com.reto.ms_reporte.adapters.in.dto.CreateReporteRequest;
import com.reto.ms_reporte.adapters.in.dto.RegistrarInscripcionRequest;
import com.reto.ms_reporte.adapters.in.mapper.ReporteMapper;
import com.reto.ms_reporte.application.ports.in.CreateReporteBootcampUseCase;
import com.reto.ms_reporte.application.ports.in.GetBootcampTopPersonasUseCase;
import com.reto.ms_reporte.application.ports.in.RegistrarInscripcionEnReporteUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ReporteHandler {
	private final CreateReporteBootcampUseCase createReporteBootcampUseCase;
	private final RegistrarInscripcionEnReporteUseCase registrarInscripcionEnReporteUseCase;
	private final GetBootcampTopPersonasUseCase getBootcampTopPersonasUseCase;
	private final ReporteMapper mapper;

	public Mono<ServerResponse> createReporte(ServerRequest request) {
		return request.bodyToMono(CreateReporteRequest.class)
				.map(mapper::toDomain)
				.flatMap(createReporteBootcampUseCase::execute)
				.map(mapper::toResponse)
				.flatMap(reporte -> ServerResponse.status(HttpStatus.CREATED)
						.contentType(MediaType.APPLICATION_JSON)
						.bodyValue(reporte))
				.onErrorResume(this::handleError);
	}

	public Mono<ServerResponse> registrarInscripcion(ServerRequest request) {
		String reporteId = request.pathVariable("id");
		return request.bodyToMono(RegistrarInscripcionRequest.class)
				.map(RegistrarInscripcionRequest::getPersona)
				.map(mapper::toDomain)
				.flatMap(persona -> registrarInscripcionEnReporteUseCase.execute(reporteId, persona))
				.then(ServerResponse.status(HttpStatus.NO_CONTENT).build())
				.onErrorResume(this::handleError);
	}

	public Mono<ServerResponse> createReporteBootcamp(ServerRequest request) {
		return request.bodyToMono(CreateReporteBootcampRequest.class)
				.map(mapper::toDomain)
				.flatMap(createReporteBootcampUseCase::execute)
				.map(mapper::toBootcampResponse)
				.flatMap(reporte -> ServerResponse.status(HttpStatus.CREATED)
						.contentType(MediaType.APPLICATION_JSON)
						.bodyValue(reporte))
				.onErrorResume(this::handleError);
	}

	public Mono<ServerResponse> getTopPersonas(ServerRequest request) {
		String bootcampId = request.pathVariable("bootcampId");
		int top = request.queryParam("top")
				.map(Integer::parseInt)
				.orElse(10);
		return getBootcampTopPersonasUseCase.execute(bootcampId, top)
				.map(mapper::toDTO)
				.collectList()
				.flatMap(personas -> ServerResponse.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.bodyValue(personas))
				.onErrorResume(this::handleError);
	}

	private Mono<ServerResponse> handleError(Throwable error) {
		if (error instanceof com.reto.ms_reporte.domain.exceptions.ReporteNotFoundException) {
			return ServerResponse.status(HttpStatus.NOT_FOUND)
					.contentType(MediaType.APPLICATION_JSON)
					.bodyValue(Map.of("error", error.getMessage()));
		}
		if (error instanceof com.reto.ms_reporte.domain.exceptions.BootcampIdDuplicadoException) {
			return ServerResponse.status(HttpStatus.CONFLICT)
					.contentType(MediaType.APPLICATION_JSON)
					.bodyValue(Map.of("error", error.getMessage()));
		}
		if (error instanceof com.reto.ms_reporte.domain.exceptions.ReporteValidationException) {
			return ServerResponse.status(HttpStatus.BAD_REQUEST)
					.contentType(MediaType.APPLICATION_JSON)
					.bodyValue(Map.of("error", error.getMessage()));
		}
		return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(Map.of("error", "Error interno del servidor"));
	}
}

