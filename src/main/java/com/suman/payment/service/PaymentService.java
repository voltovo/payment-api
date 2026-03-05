package com.suman.payment.service;

import com.suman.payment.api.dto.AuthorizePaymentRequest;
import com.suman.payment.api.dto.AuthorizePaymentResponse;
import com.suman.payment.common.ApiException;
import com.suman.payment.domain.Payment;
import com.suman.payment.domain.PaymentStatus;
import com.suman.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Transactional
    public AuthorizePaymentResponse authorize(AuthorizePaymentRequest req) {

        // 1) 중복 주문 방지 (merchantId + orderId)
        paymentRepository.findByMerchantIdAndOrderId(req.merchantId(), req.orderId())
                .ifPresent(p -> {
                    throw new ApiException("Duplicate orderId for this merchant");
                });

        // 2) PG Mock 실패 규칙 (토이 방지용 현실 규칙)
        // - VISA는 1000 초과면 실패 (예시)
        // - orderId가 "FAIL-"로 시작하면 실패 (테스트용)
        boolean shouldFail = req.orderId().startsWith("FAIL-") ||
                ("VISA".equals(req.pgProvider().name()) && req.amount().doubleValue() > 1000.0);

        Payment payment = Payment.builder()
                .merchantId(req.merchantId())
                .orderId(req.orderId())
                .amount(req.amount())
                .currency(req.currency().toUpperCase())
                .pgProvider(req.pgProvider())
                .status(shouldFail ? PaymentStatus.FAILED : PaymentStatus.AUTHORIZED)
                .build();

        paymentRepository.save(payment);

        return new AuthorizePaymentResponse(
                payment.getId(),
                payment.getStatus(),
                Instant.now());
    }
}