package br.edu.ufrn.payment.model;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Positive;

@Document(collection = "charges")
public class Charge {

    @Id
    private String id;

    @Indexed
    private String orderId;

    @Positive(message = "Amount should be higher than zero.")
    private Double amount;

    @Positive(message = "Split Into should be higher than zero.")
    private Integer splitInto;
    
    private String cardNumber;

    @Indexed(direction = IndexDirection.DESCENDING)
    private Instant createdAt;

    public Charge(String orderId, Double amount, Integer splitInto, String cardNumber) {
        this.orderId = orderId;
        this.amount = amount;
        this.splitInto = splitInto;
        this.cardNumber = cardNumber;
        this.createdAt = Instant.now();
    }

    public String getId() {
        return id;
    }

    public String getOrderId() {
        return orderId;
    }

    public Double getAmount() {
        return amount;
    }

    public Integer getSplitInto() {
        return splitInto;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
    
}
