package com.reto.ms_reporte.adapters.in.router;

import com.reto.ms_reporte.adapters.in.dto.CreateReporteBootcampRequest;
import com.reto.ms_reporte.adapters.in.dto.ReporteBootcampResponse;
import com.reto.ms_reporte.adapters.in.handler.ReporteHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
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
	)
	public RouterFunction<ServerResponse> reporteRoutes(ReporteHandler handler) {
		return RouterFunctions.route()
				.POST("/reportes/bootcamps", accept(MediaType.APPLICATION_JSON), handler::createReporteBootcamp)
				.build();
	}
}


