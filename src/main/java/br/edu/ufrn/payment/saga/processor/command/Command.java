package br.edu.ufrn.payment.saga.processor.command;

public record Command(
    CommandType type,
    String orderId,
    Double amount,
    Integer splitInto,
    String cardNumber
) {}
