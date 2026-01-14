package com.example.quicksells.domain.deal.service;

import com.example.quicksells.common.enums.ExceptionCode;
import com.example.quicksells.common.enums.StatusType;
import com.example.quicksells.common.exception.CustomException;
import com.example.quicksells.domain.deal.entity.Deal;
import com.example.quicksells.domain.deal.model.request.DealCreateRequest;
import com.example.quicksells.domain.deal.model.response.DealCreateResponse;
import com.example.quicksells.domain.deal.repository.DealRepository;
import com.example.quicksells.domain.item.entity.Item;
import com.example.quicksells.domain.item.repository.ItemRepository;
import com.example.quicksells.domain.user.entity.User;
import com.example.quicksells.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DealService {

    private final DealRepository dealrepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    /**
     * 거래 생성 API 비지니스 로직
     */
    @Transactional
    public DealCreateResponse createDeal(DealCreateRequest request) {

        // 1. 거래 대상 상품 조회
        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_ITEM));

        // 2. 구매자 조회
        User buyer = userRepository.findById(request.getBuyerId())
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));

        User seller = item.getUser();

        Deal deal = new Deal(buyer, seller, item, request.getType(), StatusType.ON_SALE, request.getDealPrice());

        dealrepository.save(deal);

        return DealCreateResponse.from(deal);
    }
}
