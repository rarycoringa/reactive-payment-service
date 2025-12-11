package br.edu.ufrn.payment.saga.choreography.event;

public record ShippingEvent(
    EventType type
) implements Event{}
