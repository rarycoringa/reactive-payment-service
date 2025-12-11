package br.edu.ufrn.payment.saga.choreography.event;

public record ProductEvent(
    EventType type
) implements Event{}
