package com.hellosudo.orderservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;

public enum PaymentMode {
    CASH,
    PAYPAL,
    DEBIT_CARD,
    CREDIT_CARD,
    APPLE_PAY
}
