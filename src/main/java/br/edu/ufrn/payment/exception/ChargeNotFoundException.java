package br.edu.ufrn.payment.exception;

public class ChargeNotFoundException extends RuntimeException {
    private static final String message = "Payment failed.";

    public ChargeNotFoundException() {
        super(message);
    }
}
