package com.ronald.microservicios.representante.empresarial.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import com.ronald.microservicios.representante.empresarial.document.BusinessRepresentative;
import com.ronald.microservicios.representante.empresarial.services.BusinessRepresentativeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class BusinessRepresentativeController {
	private static final Logger LOGGER = LoggerFactory.getLogger(BusinessRepresentativeController.class);

	@Autowired
    BusinessRepresentativeService service;

	@Value("${config.balanceador.test}")
	private String balanceadorTest;

	@GetMapping("/balanceador-test")
	public ResponseEntity<?> balanceadorTest() {

		Map<String, Object> response = new HashMap<String, Object>();
		response.put("balanceador", balanceadorTest);
		response.put("representantes", service.findAlls());
		return ResponseEntity.ok(response);

	}

	@GetMapping("/all")
	public Flux<BusinessRepresentative> searchAll() {
		Flux<BusinessRepresentative> nat = service.findAlls();
		LOGGER.info("BUSINESS REPRESENTATIVE registered: " + nat);
		return nat;
	}

	@GetMapping("/corporate-client/{corporateClientId}")
	public ResponseEntity<Flux<?>> searchbyCorporateClientIdAll(@PathVariable String corporateClientId) {


		Flux<BusinessRepresentative> newBusinessRepresentative = service.findByCorporateClientId(corporateClientId);



		if (newBusinessRepresentative != null) {

			return new ResponseEntity<>(newBusinessRepresentative, HttpStatus.CREATED);
		}

		return new ResponseEntity<>(Flux.just(new BusinessRepresentative()), HttpStatus.I_AM_A_TEAPOT);
	}

	@GetMapping("/id/{id}")
	public Mono<BusinessRepresentative> searchById(@PathVariable String id) {
		LOGGER.info("representante id: " + service.findById(id) + " con codigo: " + id);
		return service.findById(id);
	}

	@PostMapping("/create-simple-business-repre")
	public Mono<BusinessRepresentative> createSimpleRepre(@Valid @RequestBody BusinessRepresentative simplerepre) {
		LOGGER.info("Prueba feign : " + service.saves(simplerepre));
		return service.saves(simplerepre);
	}

	@PostMapping("/create-business-representative")
	public ResponseEntity<Mono<?>> createBusinessRepresentative(

			@Valid @RequestBody BusinessRepresentative businessrepresentative) {
		LOGGER.info("representante creado: " + service.saves(businessrepresentative));

		Mono.just(businessrepresentative).doOnNext(t -> {

			t.setCreateAt(new Date());

		}).onErrorReturn(businessrepresentative).onErrorResume(e -> Mono.just(businessrepresentative))
				.onErrorMap(f -> new InterruptedException(f.getMessage())).subscribe(x -> LOGGER.info(x.toString()));

		Mono<BusinessRepresentative> newBusinesRepresentative = service.saves(businessrepresentative);

		if (newBusinesRepresentative != null) {

			return new ResponseEntity<>(newBusinesRepresentative, HttpStatus.CREATED);
		}

		return new ResponseEntity<>(Mono.just(new BusinessRepresentative()), HttpStatus.I_AM_A_TEAPOT);
	}

	@PutMapping("/update-business-representative/{id}")
	public ResponseEntity<Mono<?>> updateBusinessRepresentative(
			@Valid @RequestBody BusinessRepresentative businessrepre, @PathVariable String id) {

		Mono<BusinessRepresentative> newBusinessRepresentative = service.findById(id);

		Mono.just(businessrepre).doOnNext(t -> {
			t.setId(id);
			t.setCreateAt(new Date());

		}).onErrorReturn(businessrepre).onErrorResume(e -> Mono.just(businessrepre))
				.onErrorMap(f -> new InterruptedException(f.getMessage())).subscribe(x -> LOGGER.info(x.toString()));

		newBusinessRepresentative = service.saves(businessrepre);

		if (newBusinessRepresentative != null) {

			return new ResponseEntity<>(newBusinessRepresentative, HttpStatus.CREATED);
		}

		return new ResponseEntity<>(Mono.just(new BusinessRepresentative()), HttpStatus.I_AM_A_TEAPOT);

	}

	@DeleteMapping("/delete-business-representative/{id}")
	public ResponseEntity<Mono<Void>> deleteBusinessRepresentative(@PathVariable String id) {
		BusinessRepresentative business = new BusinessRepresentative();
		business.setId(id);
		Mono<BusinessRepresentative> newBusinessRepresentative = service.findById(id);
		newBusinessRepresentative.subscribe();
		Mono<Void> test = service.delete(business);
		test.subscribe();
		return ResponseEntity.noContent().build();
	}

}
