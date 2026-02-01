package com.reto.ms_reporte.adapters.in.router;

import com.reto.ms_reporte.adapters.in.dto.CapacidadRefDTO;
import com.reto.ms_reporte.adapters.in.dto.CreateReporteBootcampRequest;
import com.reto.ms_reporte.adapters.in.dto.RegistrarInscripcionBootcampRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.Arrays;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Testcontainers
class GetTopPersonasIT {

	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6")
			.withReuse(true);

	@Autowired
	private WebTestClient webTestClient;

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@BeforeEach
	void setUp() {
	}

	@Test
	void shouldReturnTopPersonasWhenBootcampExists() {
		String bootcampId1 = "bootcamp-1-" + System.currentTimeMillis();
		String bootcampId2 = "bootcamp-2-" + System.currentTimeMillis();

		CreateReporteBootcampRequest createRequest1 = CreateReporteBootcampRequest.builder()
				.bootcampId(bootcampId1)
				.nombre("Bootcamp 1")
				.descripcion("Descripción 1")
				.fechaLanzamiento(LocalDate.of(2026, 3, 1))
				.duracionSemanas(12)
				.capacidades(Arrays.asList(
						CapacidadRefDTO.builder().id("1").nombre("Java").build()
				))
				.build();

		CreateReporteBootcampRequest createRequest2 = CreateReporteBootcampRequest.builder()
				.bootcampId(bootcampId2)
				.nombre("Bootcamp 2")
				.descripcion("Descripción 2")
				.fechaLanzamiento(LocalDate.of(2026, 4, 1))
				.duracionSemanas(16)
				.capacidades(Arrays.asList(
						CapacidadRefDTO.builder().id("2").nombre("Python").build()
				))
				.build();

		webTestClient.post()
				.uri("/reportes/bootcamps")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(createRequest1)
				.exchange()
				.expectStatus().isCreated();

		webTestClient.post()
				.uri("/reportes/bootcamps")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(createRequest2)
				.exchange()
				.expectStatus().isCreated();

		RegistrarInscripcionBootcampRequest inscripcion1 = RegistrarInscripcionBootcampRequest.builder()
				.nombre("Persona 1")
				.correo("persona1@example.com")
				.build();

		RegistrarInscripcionBootcampRequest inscripcion2 = RegistrarInscripcionBootcampRequest.builder()
				.nombre("Persona 2")
				.correo("persona2@example.com")
				.build();

		RegistrarInscripcionBootcampRequest inscripcion3 = RegistrarInscripcionBootcampRequest.builder()
				.nombre("Persona 3")
				.correo("persona3@example.com")
				.build();

		webTestClient.post()
				.uri("/reportes/bootcamps/{bootcampId}/inscripciones", bootcampId2)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(inscripcion1)
				.exchange()
				.expectStatus().isNoContent();

		webTestClient.post()
				.uri("/reportes/bootcamps/{bootcampId}/inscripciones", bootcampId2)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(inscripcion2)
				.exchange()
				.expectStatus().isNoContent();

		webTestClient.post()
				.uri("/reportes/bootcamps/{bootcampId}/inscripciones", bootcampId2)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(inscripcion3)
				.exchange()
				.expectStatus().isNoContent();

		webTestClient.get()
				.uri("/reportes/bootcamps/top-personas")
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.bootcampId").isEqualTo(bootcampId2)
				.jsonPath("$.cantidadPersonasInscritas").isEqualTo(3)
				.jsonPath("$.personas.length()").isEqualTo(3)
				.jsonPath("$.personas[0].nombre").exists()
				.jsonPath("$.personas[0].correo").exists();
	}

	@Test
	void shouldReturn404WhenNoReportesExist() {
		webTestClient.get()
				.uri("/reportes/bootcamps/top-personas")
				.exchange()
				.expectStatus().isNotFound();
	}
}

