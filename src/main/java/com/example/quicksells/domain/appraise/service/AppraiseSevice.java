package com.example.quicksells.domain.appraise.service;

import com.example.quicksells.common.enums.ExceptionCode;
import com.example.quicksells.common.enums.UserRole;
import com.example.quicksells.common.exception.CustomException;
import com.example.quicksells.domain.appraise.entity.Appraise;
import com.example.quicksells.domain.appraise.model.request.AppraiseCreateRequest;
import com.example.quicksells.domain.appraise.model.response.AppraiseResponse;
import com.example.quicksells.domain.appraise.repository.AppraiseRepository;
import com.example.quicksells.domain.auth.model.dto.AuthUser;
import com.example.quicksells.domain.deal.repository.DealRepository;
import com.example.quicksells.domain.item.entity.Item;
import com.example.quicksells.domain.item.repository.ItemRepository;
import com.example.quicksells.domain.user.entity.User;
import com.example.quicksells.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppraiseSevice {

    private final AppraiseRepository appraiseRepository;
    private final DealRepository dealRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    /**
     * 감정 생성 (관리자 권한만 가능)
     * - Deal은 나중에 판매자가 감정을 선택할 때 생성
     */
    @Transactional
    public AppraiseResponse createAppraise(Long itemId, AppraiseCreateRequest request, Long adminId) {
        // 1. 상품 존재 여부 확인
        Item item = getItem(itemId);

        // 2. 상품이 감정 가능한 상태인지 확인
        validateItemStatus(item);

        // 3. 감정사 정보 조회
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_APPRAISER));

        // 4. 감정 엔티티 생성 (Deal 연결)
        Appraise appraise = new Appraise(
                admin,                      // user (감정사)
                item,                       // item
                null,                       // deal (아직 생성하지 않음)
                request.getBidPrice(),      // bidPrice
                false                       // isSelected (기본값)
        );

        // 5. 저장
        Appraise savedAppraise = appraiseRepository.save(appraise);

        return AppraiseResponse.from(savedAppraise);
    }


    /**
     * 상품 상태 검증
     */
    private void validateItemStatus(Item item) {
        // 상품이 삭제된 경우
        if (item.isDeleted()) {
            throw new CustomException(ExceptionCode.NOT_APPRAISE_ITEM_DELETE);
        }

        // 상품이 이미 판매 완료된 경우
         if (item.isStatus()) {
             throw new CustomException(ExceptionCode.EXISTS_ITEM_SELL);
         }
    }

    /**
     * 상품의 감정 목록 전체 조회 (페이징)
     * USER(판매자) : 본인 상품만 조회 가능
     * ADMIN: 모든 상품 조회 가능
     */
    public Page<AppraiseResponse> getAppraisesByItemId(Long itemId, Pageable pageable, AuthUser authUser) {
        // 1. 상품 존재 여부 확인
        Item item = getItem(itemId);

        // 2. 권한 확인: ADMIN이 아닌 경우 본인 상품인지 검증
        if (!isAdmin(authUser)) {
            validateItemOwner(item, authUser.getId());
        }

        // 3. 감정 목록 조회 (페이징)
        Page<Appraise> appraisePage = appraiseRepository.findByItemIdWithPaging(itemId, pageable);

        // 4. DTO 변환
        return appraisePage.map(AppraiseResponse::from);
    }

    /**
     * 감정 단건 조회
     * USER(판매자) : 본인 상품만 조회 가능
     * ADMIN : 모든 상품 조회 가능
     */
    public AppraiseResponse getAppraise(Long id, Long itemId, AuthUser authUser) {
        // 1. 상품 존재 여부 확인
        Item item = getItem(itemId);

        // 2. 권한 확인: ADMIN이 아닌 경우 본인 상품인지 검증
        if (!isAdmin(authUser)) {
            validateItemOwner(item, authUser.getId());
        }

        // 3. 감정 조회
        Appraise appraise = appraiseRepository.findByIdAndItemId(id, itemId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_APPRAISE));

        // 4. DTO 변환
        return AppraiseResponse.from(appraise);
    }

    /**
     * 상품 존재 여부 확인
     */
    private Item getItem(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_ITEM));
    }

    /**
     * 상품 소유자 검증
     */
    private void validateItemOwner(Item item, Long userId) {
        if (!item.getUser().getId().equals(userId)) {
            throw new CustomException(ExceptionCode.ONLY_OWNER_APPRAISE_SEARCH);
        }
    }

    /**
     * ADMIN 권한 확인
     */
    private boolean isAdmin(AuthUser authUser) {
        return authUser.getRole() == UserRole.ADMIN;
    }


}
