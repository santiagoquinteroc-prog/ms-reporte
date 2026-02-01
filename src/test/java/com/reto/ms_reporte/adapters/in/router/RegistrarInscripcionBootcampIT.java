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
class RegistrarInscripcionBootcampIT {

	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6")
			.withReuse(true);

	@Autowired
	private WebTestClient webTestClient;

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	private String bootcampId;

	@BeforeEach
	void setUp() {
		bootcampId = "bootcamp-test-" + System.currentTimeMillis();
		
		CreateReporteBootcampRequest createRequest = CreateReporteBootcampRequest.builder()
				.bootcampId(bootcampId)
				.nombre("Bootcamp Test")
				.descripcion("Descripción test")
				.fechaLanzamiento(LocalDate.of(2026, 3, 1))
				.duracionSemanas(12)
				.capacidades(Arrays.asList(
						CapacidadRefDTO.builder().id("1").nombre("Java").build()
				))
				.build();

		webTestClient.post()
				.uri("/reportes/bootcamps")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(createRequest)
				.exchange()
				.expectStatus().isCreated();
	}

	@Test
	void shouldRegisterInscripcionWhenPersonaIsNew() {
		RegistrarInscripcionBootcampRequest request = RegistrarInscripcionBootcampRequest.builder()
				.nombre("Juan Pérez")
				.correo("juan@example.com")
				.build();

		webTestClient.post()
				.uri("/reportes/bootcamps/{bootcampId}/inscripciones", bootcampId)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(request)
				.exchange()
				.expectStatus().isNoContent();

		webTestClient.get()
				.uri("/reportes/bootcamps/top-personas")
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.cantidadPersonasInscritas").isEqualTo(1)
				.jsonPath("$.personas.length()").isEqualTo(1);
	}

	@Test
	void shouldReturn409WhenPersonaIsDuplicated() {
		RegistrarInscripcionBootcampRequest request = RegistrarInscripcionBootcampRequest.builder()
				.nombre("Juan Pérez")
				.correo("juan@example.com")
				.build();

		webTestClient.post()
				.uri("/reportes/bootcamps/{bootcampId}/inscripciones", bootcampId)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(request)
				.exchange()
				.expectStatus().isNoContent();

		webTestClient.post()
				.uri("/reportes/bootcamps/{bootcampId}/inscripciones", bootcampId)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(request)
				.exchange()
				.expectStatus().isEqualTo(org.springframework.http.HttpStatus.CONFLICT)
				.expectBody(String.class)
				.value(errorMessage -> {
					assert errorMessage.contains("juan@example.com");
					assert errorMessage.contains("ya está inscrita");
				});
	}

	@Test
	void shouldReturn400WhenCorreoIsInvalid() {
		RegistrarInscripcionBootcampRequest request = RegistrarInscripcionBootcampRequest.builder()
				.nombre("Juan Pérez")
				.correo("correo-invalido")
				.build();

		webTestClient.post()
				.uri("/reportes/bootcamps/{bootcampId}/inscripciones", bootcampId)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(request)
				.exchange()
				.expectStatus().isBadRequest()
				.expectBody(String.class)
				.value(errorMessage -> {
					assert errorMessage.contains("correo") || errorMessage.contains("válido");
				});
	}

	@Test
	void shouldReturn400WhenCorreoIsEmpty() {
		RegistrarInscripcionBootcampRequest request = RegistrarInscripcionBootcampRequest.builder()
				.nombre("Juan Pérez")
				.correo("")
				.build();

		webTestClient.post()
				.uri("/reportes/bootcamps/{bootcampId}/inscripciones", bootcampId)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(request)
				.exchange()
				.expectStatus().isBadRequest();
	}

	@Test
	void shouldReturn400WhenNombreIsEmpty() {
		RegistrarInscripcionBootcampRequest request = RegistrarInscripcionBootcampRequest.builder()
				.nombre("")
				.correo("juan@example.com")
				.build();

		webTestClient.post()
				.uri("/reportes/bootcamps/{bootcampId}/inscripciones", bootcampId)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(request)
				.exchange()
				.expectStatus().isBadRequest();
	}

	@Test
	void shouldEnsureCantidadPersonasInscritasEqualsPersonasLength() {
		RegistrarInscripcionBootcampRequest request1 = RegistrarInscripcionBootcampRequest.builder()
				.nombre("Persona 1")
				.correo("persona1@example.com")
				.build();

		RegistrarInscripcionBootcampRequest request2 = RegistrarInscripcionBootcampRequest.builder()
				.nombre("Persona 2")
				.correo("persona2@example.com")
				.build();

		webTestClient.post()
				.uri("/reportes/bootcamps/{bootcampId}/inscripciones", bootcampId)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(request1)
				.exchange()
				.expectStatus().isNoContent();

		webTestClient.post()
				.uri("/reportes/bootcamps/{bootcampId}/inscripciones", bootcampId)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(request2)
				.exchange()
				.expectStatus().isNoContent();

		webTestClient.get()
				.uri("/reportes/bootcamps/top-personas")
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.cantidadPersonasInscritas").isEqualTo(2)
				.jsonPath("$.personas.length()").isEqualTo(2);
	}
}

