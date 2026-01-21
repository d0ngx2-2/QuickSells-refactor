package com.example.quicksells.domain.deal.service;

import com.example.quicksells.common.enums.DealType;
import com.example.quicksells.common.enums.ExceptionCode;
import com.example.quicksells.common.enums.StatusType;
import com.example.quicksells.common.enums.UserRole;
import com.example.quicksells.common.exception.CustomException;
import com.example.quicksells.domain.auth.model.dto.AuthUser;
import com.example.quicksells.domain.deal.entity.Deal;
import com.example.quicksells.domain.deal.model.request.DealCreateRequest;
import com.example.quicksells.domain.deal.model.response.DealCreateResponse;
import com.example.quicksells.domain.deal.model.response.DealGetAllQueryResponse;
import com.example.quicksells.domain.deal.model.response.DealGetResponse;
import com.example.quicksells.domain.deal.repository.DealRepository;
import com.example.quicksells.domain.item.entity.Item;
import com.example.quicksells.domain.item.repository.ItemRepository;
import com.example.quicksells.domain.user.entity.User;
import com.example.quicksells.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DealService {

    private final DealRepository dealRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    /**
     * 거래 생성 API 비지니스 로직
     * SOLD Deal 존재 -> 새 Deal 생성 가능
     * 막힐 시나리오 : 즉시 판매 후 경매 생성, 경매 생성 후 즉시 판매 생성, 동일 Item로 Deal 2번 생성
     */
    @Transactional
    public DealCreateResponse createDeal(DealCreateRequest request) {

        // 아이템 존재 여부
        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_ITEM));

        // 이미 판매/경매 중인 Deal 존재 여부 체크
        dealRepository.findTopByItemAndStatusOrderByCreatedAtDesc(item, StatusType.ON_SALE)
                .ifPresent(deal -> {throw new CustomException(ExceptionCode.EXISTS_ACTIVE_DEAL);});

        User seller = item.getUser();
        User buyer = null;

        // 즉시판매가 아닌 경우만 buyer 조회
        if (request.getBuyerId() != null) {
            buyer = userRepository.findById(request.getBuyerId())
                    .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));
        }

        Deal deal = new Deal(buyer, seller, item, request.getType(), StatusType.ON_SALE, request.getDealPrice());

        dealRepository.save(deal);

        return DealCreateResponse.from(deal);
    }

    /**
     * 경매 서비스 로직에 경매 생성 비지니스 로직
     */
    @Transactional
    public Deal createAuctionDeal(Item item, Integer startPrice) {

        // 판매자 = 현재 아이템 소유자
        User seller = item.getUser();

        Deal deal = new Deal(
                null,                 // buyer 없음
                seller,
                item,
                DealType.AUCTION,
                StatusType.ON_SALE,
                startPrice
        );

        return dealRepository.save(deal);
    }


    /**
     * 거래 내역 상세 조회 API 비지니스 로직
     * ADMIN, 판매자, 구매자 조회 가능
     * 제 3자 - 403
     * buyer 없는 Deal은 seller만 조회
     * deal없으면 404
     */
    @Transactional(readOnly = true)
    public DealGetResponse getDealDetail(Long dealId, AuthUser authUser) {

        Deal deal = dealRepository.findById(dealId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_DEAL));

        // 권한 검증
        validateDealAccess(deal, authUser);

        return DealGetResponse.from(deal);
    }

    /**
     * 쿼리 메서드 적용 전체 조회
     * ADMIN	 모든 거래
     * SELLER	 본인 판매 거래
     * BUYER	 본인 구매 거래
     */
    @Transactional(readOnly = true)
    public Page<DealGetAllQueryResponse> getDeals(
            DealType type,
            AuthUser authUser,
            Pageable pageable
    ) {
        if (isAdmin(authUser)) {
            return dealRepository.findAllDeals(pageable);
        }

        if (type == DealType.PURCHASE) {
            return dealRepository.findPurchaseDeals(authUser.getId(), pageable);
        }

        return dealRepository.findSaleDeals(authUser.getId(), pageable);
    }

    /**
     * 권한 확인 서비스 로직
     */
    private boolean isAdmin(AuthUser authUser) {
        return authUser.getRole() == UserRole.ADMIN;
    }

    /**
     * 거래 상세 조회 시 권한 검증 메서드
     */
    private void validateDealAccess(Deal deal, AuthUser authUser) {

        // ADMIN은 모든 거래 조회 가능
        if (authUser.getRole() == UserRole.ADMIN) {
            return;
        }

        Long userId = authUser.getId();

        boolean isSeller = deal.getSeller().getId().equals(userId);
        boolean isBuyer = deal.getBuyer() != null && deal.getBuyer().getId().equals(userId);

        if (!isSeller && !isBuyer) {
            throw new CustomException(ExceptionCode.ACCESS_DENIED_DEAL);
        }
    }
}
