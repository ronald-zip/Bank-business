package com.ronald.microservicios.representante.empresarial.services.impl;

import com.ronald.microservicios.representante.empresarial.document.BusinessRepresentative;
import com.ronald.microservicios.representante.empresarial.repository.BusinessRepresentativeRepository;
import com.ronald.microservicios.representante.empresarial.services.BusinessRepresentativeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BusinessRepresentativeServiceImpl implements BusinessRepresentativeService {
	private static final Logger LOGGER = LoggerFactory.getLogger(BusinessRepresentativeServiceImpl.class);
	@Autowired
    BusinessRepresentativeRepository repository;

	@Override
	public Mono<BusinessRepresentative> findById(String id) {

		return repository.findById(id);
	}

	@Override
	public Flux<BusinessRepresentative> findAlls() {

		return repository.findAll();
	}

	@Override
	public Mono<BusinessRepresentative> saves(BusinessRepresentative document) {

		return repository.save(document);
	}

	@Override
	public Mono<Void> delete(BusinessRepresentative document) {

		return repository.delete(document);
	}

	@Override
	public Flux<BusinessRepresentative> findByCorporateClientId(
			String CorporateClientId) {

		return repository.findByCorporateClientId(CorporateClientId);
	}

}
