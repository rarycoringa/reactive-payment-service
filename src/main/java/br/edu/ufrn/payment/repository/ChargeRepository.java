package br.edu.ufrn.payment.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import br.edu.ufrn.payment.model.Charge;
import reactor.core.publisher.Mono;

@Repository
public interface ChargeRepository extends ReactiveMongoRepository<Charge, String> {
    
    Mono<Charge> findByOrderId(String orderId);

}
