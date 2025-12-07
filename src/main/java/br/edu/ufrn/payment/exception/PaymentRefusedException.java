package br.edu.ufrn.payment.exception;

public class PaymentRefusedException extends RuntimeException {
    private static final String message = "Payment refused.";

    public PaymentRefusedException() {
        super(message);
    }
}
