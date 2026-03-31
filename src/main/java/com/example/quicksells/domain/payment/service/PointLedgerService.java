package com.example.quicksells.domain.payment.service;

import com.example.quicksells.common.enums.PointTransactionType;
import com.example.quicksells.domain.payment.entity.PointTransaction;
import com.example.quicksells.domain.payment.entity.PointWallet;
import com.example.quicksells.domain.payment.model.TransactionReference;
import com.example.quicksells.domain.payment.repository.PointTransactionRepository;
import com.example.quicksells.domain.payment.repository.PointWalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PointLedgerService {

    private final PointWalletRepository pointWalletRepository;
    private final PointTransactionRepository pointTransactionRepository;

    @Transactional
    public PointWallet getOrCreate(Long userId) {

        return pointWalletRepository.findById(userId)
                .orElseGet(() -> pointWalletRepository.save(new PointWallet(userId)));
    }

    @Transactional
    public PointWallet credit(Long userId, long amount, PointTransactionType type, TransactionReference ref) {

        PointWallet wallet = getOrCreate(userId);
        wallet.increaseBalance(amount);
        saveTransaction(userId, type, amount, ref);

        return wallet;
    }

    @Transactional
    public PointWallet debit(Long userId, long amount, PointTransactionType type, TransactionReference ref) {

        PointWallet wallet = getOrCreate(userId);
        wallet.decreaseBalance(amount);
        saveTransaction(userId, type, amount, ref);

        return wallet;
    }

    @Transactional
    public void transfer(Long fromUserId, Long toUserId, long amount, PointTransactionType debitType, PointTransactionType creditType, TransactionReference ref) {

        PointWallet fromWallet = getOrCreate(fromUserId);
        PointWallet toWallet = getOrCreate(toUserId);

        fromWallet.decreaseBalance(amount);
        toWallet.increaseBalance(amount);

        saveTransaction(fromUserId, debitType, amount, ref);
        saveTransaction(toUserId, creditType, amount, ref);
    }

    private void saveTransaction(Long userId, PointTransactionType type, long amount, TransactionReference ref) {

        pointTransactionRepository.save(
                new PointTransaction(userId, type, amount, ref.getPaymentId(), ref.getAuctionId(), ref.getDealId()));
    }
}