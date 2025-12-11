package br.edu.ufrn.payment.saga.processor;

import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import br.edu.ufrn.payment.saga.processor.command.Command;
import br.edu.ufrn.payment.saga.processor.event.Event;
import br.edu.ufrn.payment.saga.processor.event.EventType;
import br.edu.ufrn.payment.service.PaymentService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Configuration
@Profile("orchestration")
public class CommandProcessor {
    
    private static final Logger logger = LoggerFactory.getLogger(CommandProcessor.class);

    @Autowired
    private PaymentService paymentService;

    @Bean
    public Function<Flux<Command>, Flux<Event>> processPaymentCommand() {
        return flux -> flux
            .doOnNext(command -> logger.info("Received payment command: {}", command))
            .concatMap(this::process)
            .doOnNext(event -> logger.info("Sending payment event: {}", event));
    }

    private Mono<Event> process(Command command) {
        return switch (command.type()) {
            case CHARGE_PAYMENT -> paymentService.charge(command.orderId(), command.amount(), command.splitInto(), command.cardNumber())
                .map(id -> new Event(
                    EventType.PAYMENT_CHARGED,
                    command.orderId(),
                    id,
                    null))
                .onErrorReturn(new Event(
                    EventType.PAYMENT_REFUSED,
                    command.orderId(),
                    null,
                    null));

            case REFUND_PAYMENT -> paymentService.refund(command.orderId())
                .map(id -> new Event(
                    EventType.PAYMENT_REFUNDED,
                    command.orderId(),
                    null,
                    id));
        };
    }

}
