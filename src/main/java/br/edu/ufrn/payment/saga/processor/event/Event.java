package br.edu.ufrn.payment.saga.processor.event;

public record Event(
    EventType type,
    String orderId,
    String paymentChargeId,
    String paymentRefundId,
    Double amount,
    Integer splitInto,
    String cardNumber
) {}
