package br.edu.ufrn.payment.saga.orchestration.command;

public record Command(
    CommandType type,
    String orderId,
    Double amount,
    Integer splitInto,
    String cardNumber
) {}
