package com.reto.ms_reporte.adapters.out.repository;

import com.reto.ms_reporte.adapters.out.document.ReporteBootcampDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ReporteMongoRepository extends ReactiveMongoRepository<ReporteBootcampDocument, String> {
	Mono<ReporteBootcampDocument> findByBootcampId(String bootcampId);
}


