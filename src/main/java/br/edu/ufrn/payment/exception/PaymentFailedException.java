package br.edu.ufrn.payment.exception;

public class PaymentFailedException extends ArithmeticException {
    private static final String message = "Payment failed.";

    public PaymentFailedException() {
        super(message);
    }
}
