package com.reto.ms_reporte.adapters.in.router;

import com.reto.ms_reporte.adapters.in.dto.CapacidadRefDTO;
import com.reto.ms_reporte.adapters.in.dto.CreateReporteBootcampRequest;
import com.reto.ms_reporte.adapters.in.dto.ReporteBootcampResponse;
import com.reto.ms_reporte.adapters.in.dto.TecnologiaRefDTO;
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
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Testcontainers
class CreateReporteBootcampIT {

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
	void shouldCreateReporteBootcamp() {
		List<CapacidadRefDTO> capacidades = Arrays.asList(
				CapacidadRefDTO.builder().id("1").nombre("Java").build(),
				CapacidadRefDTO.builder().id("2").nombre("Spring").build()
		);
		List<TecnologiaRefDTO> tecnologias = Arrays.asList(
				TecnologiaRefDTO.builder().id("1").nombre("React").build()
		);

		CreateReporteBootcampRequest request = CreateReporteBootcampRequest.builder()
				.bootcampId("bootcamp-123")
				.nombre("Bootcamp Full Stack")
				.descripcion("Bootcamp completo de desarrollo")
				.fechaLanzamiento(LocalDate.of(2026, 3, 1))
				.duracionSemanas(12)
				.capacidades(capacidades)
				.tecnologias(tecnologias)
				.build();

		webTestClient.post()
				.uri("/reportes/bootcamps")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(request)
				.exchange()
				.expectStatus().isCreated()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody(ReporteBootcampResponse.class)
				.value(response -> {
					assert response.getId() != null;
					assert response.getBootcampId().equals("bootcamp-123");
					assert response.getNombre().equals("Bootcamp Full Stack");
					assert response.getDescripcion().equals("Bootcamp completo de desarrollo");
					assert response.getFechaLanzamiento().equals(LocalDate.of(2026, 3, 1));
					assert response.getDuracionSemanas().equals(12);
					assert response.getCantidadCapacidades().equals(2);
					assert response.getCantidadTecnologias().equals(1);
					assert response.getCantidadPersonasInscritas().equals(0);
					assert response.getCapacidades() != null;
					assert response.getCapacidades().size() == 2;
					assert response.getTecnologias() != null;
					assert response.getTecnologias().size() == 1;
				});
	}

	@Test
	void shouldReturn409WhenBootcampIdAlreadyExists() {
		List<CapacidadRefDTO> capacidades = Arrays.asList(
				CapacidadRefDTO.builder().id("1").nombre("Java").build()
		);

		CreateReporteBootcampRequest request = CreateReporteBootcampRequest.builder()
				.bootcampId("bootcamp-duplicado")
				.nombre("Bootcamp Test")
				.descripcion("Descripción test")
				.fechaLanzamiento(LocalDate.of(2026, 3, 1))
				.duracionSemanas(8)
				.capacidades(capacidades)
				.build();

		webTestClient.post()
				.uri("/reportes/bootcamps")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(request)
				.exchange()
				.expectStatus().isCreated();

		webTestClient.post()
				.uri("/reportes/bootcamps")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(request)
				.exchange()
				.expectStatus().isEqualTo(org.springframework.http.HttpStatus.CONFLICT)
				.expectBody(String.class)
				.value(errorMessage -> {
					assert errorMessage.contains("bootcamp-duplicado") || errorMessage.contains("ya existe");
				});
	}

	@Test
	void shouldCalculateDerivedFields() {
		List<CapacidadRefDTO> capacidades = Arrays.asList(
				CapacidadRefDTO.builder().id("1").nombre("Java").build(),
				CapacidadRefDTO.builder().id("2").nombre("Spring").build(),
				CapacidadRefDTO.builder().id("3").nombre("MongoDB").build()
		);
		List<TecnologiaRefDTO> tecnologias = Arrays.asList(
				TecnologiaRefDTO.builder().id("1").nombre("React").build(),
				TecnologiaRefDTO.builder().id("2").nombre("Node.js").build()
		);

		CreateReporteBootcampRequest request = CreateReporteBootcampRequest.builder()
				.bootcampId("bootcamp-derived-" + System.currentTimeMillis())
				.nombre("Bootcamp Derived Test")
				.descripcion("Test campos derivados")
				.fechaLanzamiento(LocalDate.of(2026, 4, 1))
				.duracionSemanas(16)
				.capacidades(capacidades)
				.tecnologias(tecnologias)
				.build();

		webTestClient.post()
				.uri("/reportes/bootcamps")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(request)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(ReporteBootcampResponse.class)
				.value(response -> {
					assert response.getCantidadCapacidades().equals(3);
					assert response.getCantidadTecnologias().equals(2);
					assert response.getCantidadPersonasInscritas().equals(0);
				});
	}

	@Test
	void shouldReturn400WhenRequestIsInvalid() {
		CreateReporteBootcampRequest request = CreateReporteBootcampRequest.builder()
				.bootcampId("")
				.nombre("")
				.build();

		webTestClient.post()
				.uri("/reportes/bootcamps")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(request)
				.exchange()
				.expectStatus().isBadRequest();
	}
}

