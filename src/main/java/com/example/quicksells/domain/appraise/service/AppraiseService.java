package com.example.quicksells.domain.appraise.service;

import com.example.quicksells.common.enums.DealType;
import com.example.quicksells.common.enums.ExceptionCode;
import com.example.quicksells.common.enums.StatusType;
import com.example.quicksells.common.enums.UserRole;
import com.example.quicksells.common.exception.CustomException;
import com.example.quicksells.domain.appraise.entity.Appraise;
import com.example.quicksells.domain.appraise.model.request.AppraiseCreateRequest;
import com.example.quicksells.domain.appraise.model.request.AppraiseUpdateRequest;
import com.example.quicksells.domain.appraise.model.response.AppraiseCreateResponse;
import com.example.quicksells.domain.appraise.model.response.AppraiseGetAllResponse;
import com.example.quicksells.domain.appraise.model.response.AppraiseGetResponse;
import com.example.quicksells.domain.appraise.model.response.AppraiseUpdateResponse;
import com.example.quicksells.domain.appraise.repository.AppraiseRepository;
import com.example.quicksells.domain.auth.model.dto.AuthUser;
import com.example.quicksells.domain.deal.entity.Deal;
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
public class AppraiseService {

    private final AppraiseRepository appraiseRepository;
    private final DealRepository dealRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    /**
     * 감정 생성 (관리자 권한만 가능)
     * - Deal은 나중에 판매자가 감정을 선택할 때 생성
     */
    @Transactional
    public AppraiseCreateResponse createAppraise(Long itemId, AppraiseCreateRequest request, AuthUser authUser) {

        // 1. 상품 존재 여부 확인
        Item item = getItem(itemId);

        // 2. 상품이 감정 가능한 상태인지 확인
        validateItemStatus(item);

        // 3. 관리자가 해당 상품으로 이미 감정 생성 했는지 검증
        if (appraiseRepository.existsByItemIdAndUserId(itemId, authUser.getId())) {
            throw new CustomException(ExceptionCode.ALREADY_EXISTS_APPRAISE);
        }

        // 4. 감정사 정보 조회
        User admin = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_APPRAISER));


        // 5. 감정 엔티티 생성 (Deal 연결)
        Appraise appraise = new Appraise(
                admin,                      // user (감정사)
                item,                       // item
                null,                       // deal (아직 생성하지 않음)
                request.getBidPrice(),      // bidPrice
                false                       // isSelected (기본값)
        );

        // 6. 저장
        Appraise savedAppraise = appraiseRepository.save(appraise);

        return AppraiseCreateResponse.from(savedAppraise);
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
         if (item.isSelling()) {
             throw new CustomException(ExceptionCode.EXISTS_ITEM_SELL);
         }
    }

    /**
     * 상품의 감정 목록 전체 조회 (페이징)
     * USER(판매자) : 본인 상품만 조회 가능
     * ADMIN: 모든 상품 조회 가능
     */
    @Transactional(readOnly = true)
    public Page<AppraiseGetAllResponse> getAppraisesByItemId(Long itemId, Pageable pageable, AuthUser authUser) {
        // 1. 상품 존재 여부 확인
        Item item = getItem(itemId);

        // 2. 권한 확인: ADMIN이 아닌 경우 본인 상품인지 검증
        if (!isAdmin(authUser)) {
            validateItemOwner(item, authUser.getId());
        }

        // 3. 감정 목록 조회 (페이징)
        Page<Appraise> appraisePage = appraiseRepository.findByItemIdWithPaging(itemId, pageable);

        // 4. DTO 변환
        return appraisePage.map(AppraiseGetAllResponse::from);
    }

    /**
     * 감정 단건 조회
     * USER(판매자) : 본인 상품만 조회 가능
     * ADMIN : 모든 상품 조회 가능
     */
    @Transactional(readOnly = true)
    public AppraiseGetResponse getAppraise(Long id, Long itemId, AuthUser authUser) {

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
        return AppraiseGetResponse.from(appraise);
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
        if (!item.getSeller().getId().equals(userId)) {
            throw new CustomException(ExceptionCode.ONLY_OWNER_APPRAISE_SEARCH);
        }
    }

    /**
     * ADMIN 권한 확인
     */
    private boolean isAdmin(AuthUser authUser) {
        return authUser.getRole() == UserRole.ADMIN;
    }

    /**
     * 감정 선택 (판매자가 함)
     * - isSelected = true: 즉시 판매 → Deal 생성 (status: ON_SALE, buyer: null)
     * - isSelected = false: 판매자가 여러 감정가를 보고도 마음에 들지 않은 경우 경매 전환 > 경매 API 진행
     */
    @Transactional
    public AppraiseUpdateResponse updateAppraise(Long id, AppraiseUpdateRequest request, AuthUser authUser) {
        // 1. 감정 조회
        Appraise appraise = appraiseRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_APPRAISE));

        // 2. 권한 검증: 본인 상품의 감정인지 확인
        Item item = appraise.getItem();
        validateItemOwner(item, authUser.getId());

        // 3. 감정 검증 상태인지 확인
        validateAppraise(appraise, item);

        // 4. 선택 여부에 따른 처리
        if (request.getIsSelected()) {
            // 즉시 판매 선택
            handleImmediateSell(appraise, item, request);
        }

        appraiseRepository.flush();

        return AppraiseUpdateResponse.from(appraise);
    }

    /**
     * 즉시 판매 처리
     * - Item과 Deal은 1:N
     * - 기존 Deal이 있으면 업데이트, 없으면 생성
     */
    private void handleImmediateSell(Appraise appraise, Item item, AppraiseUpdateRequest request) {

        // 1. 해당 Item에 대한 기존 Deal 확인 (Item-Deal 1:N 관계)
        Deal deal = dealRepository.findByItem(item)
                // 기존 Deal이 존재하는 경우
                .map(existingDeal -> {
                    // 기존 Deal 업데이트
                    existingDeal.updateForAppraise(
                            DealType.IMMEDIATE_SELL,     // type: 즉시 판매
                            StatusType.ON_SALE,          // status: 거래 중
                            appraise.getBidPrice()       // dealPrice: 감정가
                    );

                    return dealRepository.save(existingDeal);
                })
                .orElseGet(() -> {
                    // 없으면 새로운 Deal 생성
                    Deal newDeal = new Deal(
                            null,                  // buyer: 즉시 판매인 경우 정보 없음
                            item.getSeller(),              // seller: 상품 판매자
                            item,                        // item: 상품 (1:N 관계)
                            DealType.IMMEDIATE_SELL,     // type: 즉시 판매
                            StatusType.ON_SALE,          // status: 거래 중
                            appraise.getBidPrice()       // dealPrice: 감정가
                    );

                    return dealRepository.save(newDeal);
                });

        dealRepository.flush();

        // 2. Appraise 업데이트
        appraise.updateSelected(request.getIsSelected());
        appraise.connectDeal(deal);

        // 3. 명시적 저장
        appraiseRepository.save(appraise);
    }

    /**
     * 감정 선택 가능한지 검증하는 메소드
     */
    private void validateAppraise(Appraise appraise, Item item) {

        // 이미 선택된 감정인지 확인 - (선택한 감정을 다시 선택시)
        if (appraise.isSeleted()) {
            throw new CustomException(ExceptionCode.ALREADY_SELECT_APPRAISE);
        }

        // 해당 상품에 이미 다른 감정이 선택되었는지 확인 - (여러 감정중에 선택한 감정은 되돌릴 수 없음)
        if (appraiseRepository.existsByItemIdAndIsSelectedTrue(item.getId())) {
            throw new CustomException(ExceptionCode.EXISTS_ALREADY_SELECT_APPRAISE);
        }
    }

    /**
     * 감정 삭제 (감정사 ADMIN만 가능)
     * - Soft Delete
     * - 본인이 작성한 감정만 삭제 가능
     * - 선택된 감정(isSelected = true)은 삭제 불가
     */
    @Transactional
    public void deleteAppraise(Long itemId, AuthUser authUser) {

        // 1. 상품 존재 여부 확인
        Item item = getItem(itemId);

        // 2. 해당 상품에 대한 현재 감정사의 감정 조회
        Appraise appraise = appraiseRepository.findByItemAndUserId(item, authUser.getId())
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_APPRAISE));

        // 3. 이미 선택된 감정인지 확인 (선택된 감정은 삭제 불가)
        if (appraise.isSeleted()) {
            throw new CustomException(ExceptionCode.NOT_DELETE_SELECTED_APPRAISE);
        }

        // 4. Soft Delete 처리
        appraise.delete();
    }
}
