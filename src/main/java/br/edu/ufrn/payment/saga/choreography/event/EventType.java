package br.edu.ufrn.payment.saga.choreography.event;

public enum EventType {
    PRODUCT_RESERVED,
    PRODUCT_UNAVAILABLE,
    PRODUCT_RETURNED,
    PAYMENT_CHARGED,
    PAYMENT_REFUSED,
    PAYMENT_REFUNDED,
    SHIPPING_ACCEPTED,
    SHIPPING_REFUSED
}
