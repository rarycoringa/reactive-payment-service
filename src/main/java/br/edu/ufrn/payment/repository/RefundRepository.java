package br.edu.ufrn.payment.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import br.edu.ufrn.payment.model.Refund;

@Repository
public interface RefundRepository extends ReactiveMongoRepository<Refund, String> {
    
}
