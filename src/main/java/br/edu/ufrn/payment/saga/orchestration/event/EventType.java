package br.edu.ufrn.payment.saga.orchestration.event;

public enum EventType {
    PAYMENT_CHARGED,
    PAYMENT_REFUSED,
    PAYMENT_REFUNDED
}
