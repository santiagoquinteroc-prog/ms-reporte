# MS Reporte

Microservicio de gestión de reportes de bootcamp con arquitectura hexagonal, WebFlux y RouterFunctions.

## Requisitos

- Java 17
- Gradle
- Docker y Docker Compose

## Pasos para ejecutar

1. Iniciar MongoDB con Docker Compose:
```bash
docker-compose up -d
```

2. Compilar el proyecto:
```bash
./gradlew build
```

3. Ejecutar la aplicación:
```bash
./gradlew bootRun
```

La aplicación estará disponible en `http://localhost:8084`

## Endpoints

- POST `/reportes/bootcamps` - Crear reporte de bootcamp

## Documentación API

- Swagger UI: `http://localhost:8084/swagger-ui.html`
- API Docs: `http://localhost:8084/v3/api-docs`


