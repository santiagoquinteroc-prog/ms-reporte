package com.reto.ms_reporte.adapters.in.router;

import com.reto.ms_reporte.adapters.in.dto.CreateReporteBootcampRequest;
import com.reto.ms_reporte.adapters.in.dto.RegistrarInscripcionBootcampRequest;
import com.reto.ms_reporte.adapters.in.dto.ReporteBootcampResponse;
import com.reto.ms_reporte.adapters.in.dto.TopPersonasResponse;
import com.reto.ms_reporte.adapters.in.handler.ReporteHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class ReporteRouter {

	@Bean
	@RouterOperations({
		@RouterOperation(
			path = "/reportes/bootcamps",
			produces = {MediaType.APPLICATION_JSON_VALUE},
			method = RequestMethod.POST,
			beanClass = ReporteHandler.class,
			beanMethod = "createReporteBootcamp",
			operation = @Operation(
				operationId = "createReporteBootcamp",
				summary = "Crear reporte de bootcamp",
				tags = {"Reportes"},
				requestBody = @RequestBody(
					required = true,
					content = @Content(schema = @Schema(implementation = CreateReporteBootcampRequest.class))
				),
				responses = {
					@ApiResponse(responseCode = "201", description = "Reporte creado",
						content = @Content(schema = @Schema(implementation = ReporteBootcampResponse.class))),
					@ApiResponse(responseCode = "400", description = "Solicitud inválida"),
					@ApiResponse(responseCode = "409", description = "BootcampId duplicado")
				}
			)
		),
		@RouterOperation(
			path = "/reportes/bootcamps/{bootcampId}/inscripciones",
			produces = {MediaType.APPLICATION_JSON_VALUE},
			method = RequestMethod.POST,
			beanClass = ReporteHandler.class,
			beanMethod = "registrarInscripcionBootcamp",
			operation = @Operation(
				operationId = "registrarInscripcionBootcamp",
				summary = "Registrar inscripción en reporte de bootcamp",
				tags = {"Inscripciones"},
				requestBody = @RequestBody(
					required = true,
					content = @Content(schema = @Schema(implementation = RegistrarInscripcionBootcampRequest.class))
				),
				responses = {
					@ApiResponse(responseCode = "204", description = "Inscripción registrada"),
					@ApiResponse(responseCode = "400", description = "Solicitud inválida"),
					@ApiResponse(responseCode = "404", description = "Reporte no encontrado"),
					@ApiResponse(responseCode = "409", description = "Persona duplicada")
				}
			)
		),
		@RouterOperation(
			path = "/reportes/bootcamps/top-personas",
			produces = {MediaType.APPLICATION_JSON_VALUE},
			method = RequestMethod.GET,
			beanClass = ReporteHandler.class,
			beanMethod = "getTopPersonas",
			operation = @Operation(
				operationId = "getTopPersonas",
				summary = "Obtener reporte del bootcamp con más personas inscritas",
				tags = {"Reportes"},
				responses = {
					@ApiResponse(responseCode = "200", description = "OK",
						content = @Content(schema = @Schema(implementation = TopPersonasResponse.class))),
					@ApiResponse(responseCode = "404", description = "No hay reportes disponibles")
				}
			)
		)
	})
	public RouterFunction<ServerResponse> reporteRoutes(ReporteHandler handler) {
		return RouterFunctions.route()
				.POST("/reportes/bootcamps", accept(MediaType.APPLICATION_JSON), handler::createReporteBootcamp)
				.POST("/reportes/bootcamps/{bootcampId}/inscripciones", accept(MediaType.APPLICATION_JSON), handler::registrarInscripcionBootcamp)
				.GET("/reportes/bootcamps/top-personas", handler::getTopPersonas)
				.build();
	}
}


