package br.edu.ufrn.payment.saga.choreography.event;

public sealed interface Event permits OrderEvent, ProductEvent, PaymentEvent, ShippingEvent {
    EventType type();
}
