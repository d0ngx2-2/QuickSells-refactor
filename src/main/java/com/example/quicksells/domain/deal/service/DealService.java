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
import com.example.quicksells.domain.deal.model.response.DealGetResponse;
import com.example.quicksells.domain.deal.model.response.DealListResponse;
import com.example.quicksells.domain.deal.repository.DealRepository;
import com.example.quicksells.domain.item.entity.Item;
import com.example.quicksells.domain.item.repository.ItemRepository;
import com.example.quicksells.domain.user.entity.User;
import com.example.quicksells.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DealService {

    private final DealRepository dealRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    /**
     * 거래 생성 API 비지니스 로직
     */
    @Transactional
    public DealCreateResponse createDeal(DealCreateRequest request) {

        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_ITEM));

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
     */
    @Transactional(readOnly = true)
    public DealGetResponse getDealDetail(Long dealId) {

        Deal deal = dealRepository.findById(dealId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_DEAL));

        return DealGetResponse.from(deal);
    }

    /**
     * 거래 조회 (구매 / 판매)
     */
    @Transactional(readOnly = true)
    public List<DealListResponse> getDeals(DealType type, AuthUser authUser) {

        // ADMIN이면 전부 조회
        if (isAdmin(authUser)) {
            return getAllDeals(type);
        }

        // 일반 유저
        if (type == DealType.PURCHASE) {
            return getPurchaseDeals(authUser.getId());
        }
        return getSaleDeals(authUser.getId());
    }

    /**
     * ADMIN 전체 조회
     */
    protected List<DealListResponse> getAllDeals(DealType type) {

        return dealRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(deal -> {
                    if (type == DealType.PURCHASE && deal.getBuyer() != null) {
                        return DealListResponse.forPurchase(deal);
                    }
                    return DealListResponse.forSale(deal);
                })
                .toList();
    }

    /**
     * 구매 내역 조회
     */
    protected List<DealListResponse> getPurchaseDeals(Long buyerId) {

        return dealRepository.findByBuyerIdOrderByCreatedAtDesc(buyerId)
                .stream()
                .map(DealListResponse::forPurchase)
                .toList();
    }

    /**
     * 판매 내역 조회
     */
    protected List<DealListResponse> getSaleDeals(Long sellerId) {

        return dealRepository.findBySellerIdOrderByCreatedAtDesc(sellerId)
                .stream()
                .map(DealListResponse::forSale)
                .toList();
    }

    /**
     * 권한 확인 서비스 로직
     */
    private boolean isAdmin(AuthUser authUser) {
        return authUser.getRole() == UserRole.ADMIN;
    }
}
