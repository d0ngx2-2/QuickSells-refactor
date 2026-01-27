package com.example.quicksells.domain.payment.repository;

import com.example.quicksells.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    boolean existsByOrderId(String orderId);

    boolean existsByTossPaymentKey(String tossPaymentKey);
}
