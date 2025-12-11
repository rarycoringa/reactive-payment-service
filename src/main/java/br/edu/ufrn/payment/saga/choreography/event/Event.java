package br.edu.ufrn.payment.saga.choreography.event;

public sealed interface Event permits ProductEvent, PaymentEvent, ShippingEvent {
    EventType type();
    String orderId();
    Double amount();
    Integer splitInto();
    String cardNumber();
}
