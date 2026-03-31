package com.example.quicksells.domain.payment.service;

import com.example.quicksells.common.enums.ExceptionCode;
import com.example.quicksells.common.enums.PointTransactionType;
import com.example.quicksells.common.exception.CustomException;
import com.example.quicksells.domain.payment.entity.PointWallet;
import com.example.quicksells.domain.payment.model.TransactionReference;
import com.example.quicksells.domain.payment.model.response.AdminPointGrantResponse;
import com.example.quicksells.domain.payment.model.response.PointTransactionGetResponse;
import com.example.quicksells.domain.payment.model.response.WalletGetResponse;
import com.example.quicksells.domain.payment.repository.PointTransactionRepository;
import com.example.quicksells.domain.payment.repository.PointWalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PointWalletService {

    private final PointWalletRepository pointWalletRepository;
    private final PointLedgerService pointLedgerService;
    private final PointTransactionRepository pointTransactionRepository;

    @Transactional
    public PointWallet getOrCreate(Long userId) {

        return pointWalletRepository.findById(userId)
                .orElseGet(() -> {log.info("[Wallet] 포인트 지갑이 없어 생성합니다. userId={}", userId);
                    return pointWalletRepository.save(new PointWallet(userId));
                });
    }

    @Transactional(readOnly = true)
    public WalletGetResponse getMyWalletResponse(Long userId) {

        PointWallet wallet = getOrCreate(userId);
        return WalletGetResponse.from(wallet);
    }

    @Transactional(readOnly = true)
    public Page<PointTransactionGetResponse> getMyTransactionsResponse(Long userId, int page, int size) {

        return pointTransactionRepository.findByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(page, size)).
                map(PointTransactionGetResponse::from);
    }

    @Transactional
    public AdminPointGrantResponse grantPointResponse(Long targetUserId, Long amount) {

        PointWallet wallet = grantPoint(targetUserId, amount);
        return AdminPointGrantResponse.from(targetUserId, amount, wallet);
    }

    @Transactional(readOnly = true)
    public WalletGetResponse getUserWalletResponse(Long userId) {

        PointWallet wallet = getOrCreate(userId);
        return WalletGetResponse.from(wallet);
    }

    @Transactional(readOnly = true)
    public Page<PointTransactionGetResponse> getUserTransactionsResponse(Long userId, int page, int size) {

        return pointTransactionRepository.findByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(page, size))
                .map(PointTransactionGetResponse::from);
    }

    @Transactional
    public PointWallet grantPoint(Long targetUserId, Long amount) {

        if (amount == null || amount <= 0) {
            throw new CustomException(ExceptionCode.INVALID_CHARGE_AMOUNT);
        }

        return pointLedgerService.credit(targetUserId, amount, PointTransactionType.ADMIN_GRANT, TransactionReference.none());
    }
}