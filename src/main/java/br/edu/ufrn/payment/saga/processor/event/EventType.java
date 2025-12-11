package br.edu.ufrn.payment.saga.processor.event;

public enum EventType {
    PAYMENT_CHARGED,
    PAYMENT_REFUSED,
    PAYMENT_REFUNDED
}
