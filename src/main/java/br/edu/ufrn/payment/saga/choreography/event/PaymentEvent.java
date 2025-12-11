package br.edu.ufrn.payment.saga.choreography.event;

public record PaymentEvent(
    EventType type
) implements Event{}
