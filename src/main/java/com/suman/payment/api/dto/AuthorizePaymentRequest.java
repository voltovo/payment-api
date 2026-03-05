package com.suman.payment.api.dto;

import java.math.BigDecimal;

import com.suman.payment.domain.PgProvider;

import jakarta.validation.constraints.*;

public record AuthorizePaymentRequest(
        @NotBlank String merchantId,
        @NotBlank String orderId,
        @NotNull @DecimalMin(value = "0.01") BigDecimal amount,
        @NotBlank @Size(min = 3, max = 3) String currency,
        @NotNull PgProvider pgProvider) {
    
}
