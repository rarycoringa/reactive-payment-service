package br.edu.ufrn.payment.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ufrn.payment.exception.PaymentRefusedException;
import br.edu.ufrn.payment.model.Charge;
import br.edu.ufrn.payment.model.Refund;
import br.edu.ufrn.payment.repository.ChargeRepository;
import br.edu.ufrn.payment.repository.RefundRepository;
import reactor.core.publisher.Mono;

@Service
public class PaymentService {
    @Autowired
    private ChargeRepository chargeRepository;
    
    @Autowired
    private RefundRepository refundRepository;
    
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    public Mono<String> charge(
        String orderId,
        Double amount,
        Integer splitInto,
        String cardNumber
    ) {
        boolean refused = Math.random() < 0.1;

        if (refused) {
            return Mono.error(new PaymentRefusedException());
        }

        return chargeRepository
            .save(new Charge(orderId, amount, splitInto, cardNumber))
            .map(Charge::getId);
    }

    public Mono<String> refund(String orderId) {
        return chargeRepository
            .findByOrderId(orderId)
            .flatMap(charge -> {
                return refundRepository.save(
                    new Refund(
                        charge.getOrderId(),
                        charge.getAmount(),
                        charge.getSplitInto(),
                        charge.getCardNumber()));
            })
            .map(Refund::getId);
    }
    
}
