package com.reto.ms_reporte.adapters.in.handler;

import com.reto.ms_reporte.adapters.in.dto.CreateReporteBootcampRequest;
import com.reto.ms_reporte.adapters.in.dto.RegistrarInscripcionBootcampRequest;
import com.reto.ms_reporte.adapters.in.mapper.ReporteMapper;
import com.reto.ms_reporte.application.ports.in.CreateReporteBootcampUseCase;
import com.reto.ms_reporte.application.ports.in.GetTopPersonasUseCase;
import com.reto.ms_reporte.application.ports.in.RegistrarInscripcionBootcampUseCase;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ReporteHandler {
	private final CreateReporteBootcampUseCase createReporteBootcampUseCase;
	private final RegistrarInscripcionBootcampUseCase registrarInscripcionBootcampUseCase;
	private final GetTopPersonasUseCase getTopPersonasUseCase;
	private final ReporteMapper mapper;
	private final Validator validator;

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

	public Mono<ServerResponse> registrarInscripcionBootcamp(ServerRequest request) {
		String bootcampId = request.pathVariable("bootcampId");
		return request.bodyToMono(RegistrarInscripcionBootcampRequest.class)
				.flatMap(body -> {
					Set<jakarta.validation.ConstraintViolation<RegistrarInscripcionBootcampRequest>> violations = validator.validate(body);
					if (!violations.isEmpty()) {
						String errorMessage = violations.stream()
								.map(v -> v.getPropertyPath() + ": " + v.getMessage())
								.reduce((a, b) -> a + "; " + b)
								.orElse("Validación fallida");
						return Mono.error(new com.reto.ms_reporte.domain.exceptions.ReporteValidationException(errorMessage));
					}
					return Mono.just(body);
				})
				.map(mapper::toDomain)
				.flatMap(persona -> registrarInscripcionBootcampUseCase.execute(bootcampId, persona))
				.then(ServerResponse.status(HttpStatus.NO_CONTENT).build())
				.onErrorResume(this::handleError);
	}

	public Mono<ServerResponse> getTopPersonas(ServerRequest request) {
		return getTopPersonasUseCase.execute()
				.map(mapper::toTopPersonasResponse)
				.flatMap(response -> ServerResponse.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.bodyValue(response))
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
		if (error instanceof com.reto.ms_reporte.domain.exceptions.PersonaDuplicadaException) {
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

