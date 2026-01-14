package com.example.quicksells.domain.auction.service;

import com.example.quicksells.common.enums.ExceptionCode;
import com.example.quicksells.common.exception.CustomException;
import com.example.quicksells.domain.appraise.entity.Appraise;
import com.example.quicksells.domain.appraise.repository.AppraiseRepository;
import com.example.quicksells.domain.auction.dto.request.AuctionCreateRequest;
import com.example.quicksells.domain.auction.dto.response.AuctionCreateResponse;
import com.example.quicksells.domain.auction.dto.response.AuctionGetAllResponse;
import com.example.quicksells.domain.auction.entity.Auction;
import com.example.quicksells.domain.auction.repository.AuctionRepository;
import com.example.quicksells.domain.deal.entity.Deal;
import com.example.quicksells.domain.deal.repository.DealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final DealRepository dealRepository;
    private final AppraiseRepository appraiseRepository;

    @Transactional
    public AuctionCreateResponse saveAuction(AuctionCreateRequest request) {

        // 예외
        Deal founddDeal = dealRepository.findById(request.getDealId())
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_DEAL));

        Appraise foundAppraise = appraiseRepository.findById(request.getAppraiseId())
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_APPRAISE));

        // 경매 생성
        Auction newAuction = new Auction(
                founddDeal,
                foundAppraise,
                foundAppraise.getBidPrice()
        );

        Auction saveAuction = auctionRepository.save(newAuction);

        return AuctionCreateResponse.from(saveAuction);
    }

    @Transactional(readOnly = true)
    public Page<AuctionGetAllResponse> getAllAuction(Pageable pageable) {

        Page<Auction> foundAuctionPage = auctionRepository.findAll(pageable);

        return foundAuctionPage.map(AuctionGetAllResponse::from);
    }

}
