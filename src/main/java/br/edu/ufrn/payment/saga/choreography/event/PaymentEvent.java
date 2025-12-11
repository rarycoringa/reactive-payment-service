package br.edu.ufrn.payment.saga.choreography.event;

public record PaymentEvent(
    EventType type,
    String orderId,
    Double amount,
    Integer splitInto,
    String cardNumber
) implements Event{}
