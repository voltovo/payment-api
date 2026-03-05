package com.suman.payment.api.dto;

import java.time.Instant;
import java.util.UUID;

import com.suman.payment.domain.PaymentStatus;

public record AuthorizePaymentResponse(
        UUID paymentId,
        PaymentStatus status,
        Instant approvedAt) {
    
}
