package com.suman.payment.api;

import org.springframework.web.bind.annotation.*;

import com.suman.payment.api.dto.AuthorizePaymentRequest;
import com.suman.payment.api.dto.AuthorizePaymentResponse;
import com.suman.payment.service.PaymentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/authorize")
    public AuthorizePaymentResponse authorize(@Valid @RequestBody AuthorizePaymentRequest request) {
        return paymentService.authorize(request);
    }
}
