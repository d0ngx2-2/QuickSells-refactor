package com.example.quicksells.domain.auction.service;

import com.example.quicksells.common.enums.ExceptionCode;
import com.example.quicksells.common.exception.CustomException;
import com.example.quicksells.domain.appraise.entity.Appraise;
import com.example.quicksells.domain.appraise.repository.AppraiseRepository;
import com.example.quicksells.domain.auction.model.request.AuctionCreateRequest;
import com.example.quicksells.domain.auction.model.request.AuctionUpdateRequest;
import com.example.quicksells.domain.auction.model.response.AuctionCreateResponse;
import com.example.quicksells.domain.auction.model.response.AuctionGetAllResponse;
import com.example.quicksells.domain.auction.model.response.AuctionUpdateResponse;
import com.example.quicksells.domain.auction.entity.Auction;
import com.example.quicksells.domain.auction.repository.AuctionRepository;
import com.example.quicksells.domain.auth.model.dto.AuthUser;
import com.example.quicksells.domain.deal.entity.Deal;
import com.example.quicksells.domain.deal.repository.DealRepository;
import com.example.quicksells.domain.user.entity.User;
import com.example.quicksells.domain.user.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final AuctionCloseService auctionCloseService;

    @Transactional
    public AuctionCreateResponse saveAuction(AuctionCreateRequest request) {

        // 거래 조회
        Deal foundDeal = dealRepository.findById(request.getDealId())
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_DEAL));

        // 감정 조회
        Appraise foundAppraise = appraiseRepository.findById(request.getAppraiseId())
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_APPRAISE));

        // 경매 생성
        Auction newAuction = new Auction(
                foundDeal,
                foundAppraise,
                foundAppraise.getBidPrice() // 시작입찰가 -> 감정가격
        );

        Auction saveAuction = auctionRepository.save(newAuction);

        return AuctionCreateResponse.from(saveAuction);
    }

    @Transactional(readOnly = true)
    public Page<AuctionGetAllResponse> getAllAuction(Pageable pageable) {

        // 경매 페이지 조회
        Page<Auction> foundAuctionPage = auctionRepository.findAll(pageable);

        return foundAuctionPage.map(AuctionGetAllResponse::from);
    }

    @Transactional
    public AuctionUpdateResponse updateBidPrice(Long auctionId, AuctionUpdateRequest request, AuthUser authUser) {

        // 경매 종료 여부 확인 후 결과
        auctionCloseService.auctionIsCloseCheckResult(auctionId);

        // 경매 조회
        Auction foundAuction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_AUCTION));

        // 삭제 검증
        validateIsDelete(foundAuction.isDeleted());

        // 구매자 조회
        User foundBuyer = userRepository.findById(request.getBuyerId())
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));

        // 구매자 검증
        validateUser(authUser, foundBuyer);

        // 입찰 가격 검증
        validateBidPrice(request.getBidPrice(), foundAuction.getBidPrice());

        // 경매 입찰
        foundAuction.update(foundBuyer, request.getBidPrice());

        return AuctionUpdateResponse.from(foundAuction);
    }

    /**
     * 종료된 경매는 삭제요청 불가
     */
    @Transactional
    public void deleteAuction(Long auctionId, AuthUser authUser) {

        // 경매 종료 여부 확인 후 결과
        auctionCloseService.auctionIsCloseCheckResult(auctionId);

        // 경매 조회
        Auction foundAuction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_AUCTION));

        // 삭제 검증
        validateIsDelete(foundAuction.isDeleted());

        // 삭제되지 않은 경매 삭제
        foundAuction.auctionDelete();
    }


    /**
     * 검증 메서드
     */

    private void validateBidPrice(Integer requestBidPrice, Integer AuctionBiePrice) {

        // 요청 입찰가가 경매 입찰가보다 같거나 낮으면 예외
        if (requestBidPrice <= AuctionBiePrice) {
            throw new CustomException(ExceptionCode.BID_PRICE_TOO_LOW);
        }
    }

    private void validateUser(AuthUser authUser, User founUser) {

        Long authUserId = authUser.getId(); // 인증유저 아이디
        Long userId = founUser.getId(); // 조회된 유저 아이디

        // 인증유저와 조회된 유저가 일치하지 않을때 예외
        if (!authUserId.equals(userId)) {
            throw new CustomException(ExceptionCode.ACCESS_DENIED);
        }
    }

    private void validateIsDelete(boolean isDelete) {

        // 논리삭제 true -> 예외
        if (isDelete) {
            throw new CustomException(ExceptionCode.AUCTION_ALREADY_EXPIRED);
        }
    }

}
