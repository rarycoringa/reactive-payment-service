package br.edu.ufrn.payment.saga.choreography;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import br.edu.ufrn.payment.saga.choreography.event.EventType;
import br.edu.ufrn.payment.saga.choreography.event.OrderEvent;
import br.edu.ufrn.payment.saga.choreography.event.PaymentEvent;
import br.edu.ufrn.payment.saga.choreography.event.ProductEvent;
import br.edu.ufrn.payment.saga.choreography.event.ShippingEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Configuration
@Profile("choreography")
public class Choreographer {
    private static final Logger logger = LoggerFactory.getLogger(Choreographer.class);

    @Bean
    public Function<Flux<ProductEvent>, Flux<PaymentEvent>> processProductEvent() {
        return flux -> flux
            .doOnNext(event -> logger.info("Received product event: {}", event))
            .concatMap(this::handleProductEvent)
            .concatMap(this::process)
            .doOnNext(event -> logger.info("Sending payment event: {}", event));
    }

    @Bean
    public Function<Flux<ShippingEvent>, Flux<PaymentEvent>> processShippingEvent() {
        return flux -> flux
            .doOnNext(event -> logger.info("Received shipping event: {}", event))
            .concatMap(this::handleShippingEvent)
            .concatMap(this::process)
            .doOnNext(event -> logger.info("Sending payment event: {}", event));
    }

    private Mono<PaymentEvent> handleProductEvent(ProductEvent event) {
        return switch (event.type()) {
            case PRODUCT_RESERVED -> Mono.just(new PaymentEvent(EventType.PAYMENT_CHARGED));
            case PRODUCT_UNAVAILABLE, PRODUCT_RETURNED -> Mono.empty();
            default -> Mono.empty();
        };
    }

    private Mono<PaymentEvent> handleShippingEvent(ShippingEvent event) {
        return switch (event.type()) {
            case SHIPPING_REFUSED -> Mono.just(new PaymentEvent(EventType.PAYMENT_REFUNDED));
            case SHIPPING_ACCEPTED -> Mono.empty();
            default -> Mono.empty();
        };
    }

    private Mono<PaymentEvent> process(PaymentEvent event) {
        return switch (event.type()) {
            case PAYMENT_CHARGED -> Mono.just(event)
                .doOnNext(e -> logger.info("Payment charged: {}", e));
                
            case PAYMENT_REFUSED -> Mono.just(event)
                .doOnNext(e -> logger.info("Payment refused: {}", e));
                
            case PAYMENT_REFUNDED -> Mono.just(event)
                .doOnNext(e -> logger.info("Payment refunded: {}", e));
                
            default -> Mono.empty();
        };
    }
}
