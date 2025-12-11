package br.edu.ufrn.payment.saga.choreography;

import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import br.edu.ufrn.payment.saga.choreography.event.EventType;
import br.edu.ufrn.payment.saga.choreography.event.PaymentEvent;
import br.edu.ufrn.payment.saga.choreography.event.ProductEvent;
import br.edu.ufrn.payment.saga.choreography.event.ShippingEvent;
import br.edu.ufrn.payment.service.PaymentService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Configuration
@Profile("choreography")
public class Choreographer {

    private static final Logger logger = LoggerFactory.getLogger(Choreographer.class);

    @Autowired
    private PaymentService paymentService;

    @Bean
    public Function<Flux<ProductEvent>, Flux<PaymentEvent>> processProductEvent() {
        return flux -> flux
            .doOnNext(event -> logger.info("Received product event: {}", event))
            .concatMap(this::process)
            .doOnNext(event -> logger.info("Sending payment event: {}", event));
    }

    @Bean
    public Function<Flux<ShippingEvent>, Flux<PaymentEvent>> processShippingEvent() {
        return flux -> flux
            .doOnNext(event -> logger.info("Received shipping event: {}", event))
            .concatMap(this::process)
            .doOnNext(event -> logger.info("Sending payment event: {}", event));
    }

    private Mono<PaymentEvent> process(ProductEvent event) {
        return switch (event.type()) {
            case PRODUCT_RESERVED -> paymentService.charge(event.orderId(), event.amount(), event.splitInto(), event.cardNumber())
                .map(id -> new PaymentEvent(
                    EventType.PAYMENT_CHARGED,
                    event.orderId(),
                    event.productId(),
                    event.productQuantity(),
                    event.productName(),
                    event.productPrice(),
                    id,
                    event.refundId(),
                    event.amount(),
                    event.splitInto(),
                    event.cardNumber(),
                    event.shippingId(),
                    event.address()))
                .onErrorReturn(new PaymentEvent(
                    EventType.PAYMENT_REFUSED,
                    event.orderId(),
                    event.productId(),
                    event.productQuantity(),
                    event.productName(),
                    event.productPrice(),
                    event.chargeId(),
                    event.refundId(),
                    event.amount(),
                    event.splitInto(),
                    event.cardNumber(),
                    event.shippingId(),
                    event.address()));

            case PRODUCT_UNAVAILABLE, PRODUCT_RETURNED -> Mono.empty();
                
            default -> Mono.empty();
        };
    }

    private Mono<PaymentEvent> process(ShippingEvent event) {
        return switch (event.type()) {
            case SHIPPING_REFUSED -> paymentService.refund(event.orderId())
                .map(id -> new PaymentEvent(
                    EventType.PAYMENT_REFUNDED,
                    event.orderId(),
                    event.productId(),
                    event.productQuantity(),
                    event.productName(),
                    event.productPrice(),
                    event.chargeId(),
                    id,
                    event.amount(),
                    event.splitInto(),
                    event.cardNumber(),
                    event.shippingId(),
                    event.address()));
                
            case SHIPPING_ACCEPTED -> Mono.empty();

            default -> Mono.empty();
        };
    }
}
