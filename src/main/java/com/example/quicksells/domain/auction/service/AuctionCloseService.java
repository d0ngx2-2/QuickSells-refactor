package com.example.quicksells.domain.auction.service;

import com.example.quicksells.common.enums.ExceptionCode;
import com.example.quicksells.common.exception.CustomException;
import com.example.quicksells.domain.auction.entity.Auction;
import com.example.quicksells.domain.auction.repository.AuctionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuctionCloseService {

    private final AuctionRepository auctionRepository;

    // DB에 논리삭제와 상태변경을 반영
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void auctionIsCloseCheckResult(Long auctionId) {

        Auction foundAuction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_AUCTION));

        foundAuction.auctionEndTimeCheck(); // 종료시간 확인
    }
}
