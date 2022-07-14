package com.ronald.microservicios.representante.empresarial.repository;

import com.ronald.microservicios.representante.empresarial.document.BusinessRepresentative;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;

@Repository
public interface BusinessRepresentativeRepository extends ReactiveMongoRepository<BusinessRepresentative, String> {

	Flux<BusinessRepresentative> findByCorporateClientId(String CorporateClientId);

}
