package br.edu.ufrn.payment.saga.choreography.event;

public record OrderEvent(
    EventType type
) implements Event{}
