package com.example.quicksells.domain.item.service;

import com.example.quicksells.common.enums.ExceptionCode;
import com.example.quicksells.common.exception.CustomException;
import com.example.quicksells.domain.auth.model.dto.AuthUser;
import com.example.quicksells.domain.item.model.request.ItemCreatedRequest;
import com.example.quicksells.domain.item.model.request.ItemUpdateRequest;
import com.example.quicksells.domain.item.model.response.ItemCreatedResponse;
import com.example.quicksells.domain.item.model.response.ItemGetDetailResponse;
import com.example.quicksells.domain.item.model.response.ItemGetListResponse;
import com.example.quicksells.domain.item.model.response.ItemUpdateResponse;
import com.example.quicksells.domain.item.entity.Item;
import com.example.quicksells.domain.item.repository.ItemRepository;
import com.example.quicksells.domain.user.entity.User;
import com.example.quicksells.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    /**
     * 상품 생성 로직
     *
     * @param authUser 상품 생성에 필요한 데이터 요청
     * @param request  생성된 상품 정보 응답하는 DTO
     * @return
     */
    @Transactional
    public ItemCreatedResponse itemCreated(AuthUser authUser, ItemCreatedRequest request) {

        //유저(판매자) 조회
        User seller = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));

        //중복 상품 검증
        boolean exists = itemRepository.existsByUserIdAndName(authUser.getId(), request.getName());

        //중복 시 409에러 발생
        if (exists) {
            throw new CustomException(ExceptionCode.CONFLICT_ITEM);
        }

        // 엔티티 생성
        Item item = new Item(seller, request.getName(), request.getHopePrice(), request.getDescription(), request.getImage());

        // Item 엔티티 DB에 저장
        Item saved = itemRepository.save(item);

        //응답 전용 객체
        return ItemCreatedResponse.from(saved);
    }

    /**
     * 상품 상세 조회
     *
     * @param id 상품 조회 ID
     * @return 상품 상세 정보 응답 DTO
     */
    @Transactional(readOnly = true)

    public ItemGetDetailResponse itemGetDetail(Long id) {

        //상품 조회 및 검증 404에러

        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_ITEM));

        // 조회된 엔티티 -> DTO로 변환
        return ItemGetDetailResponse.from(item);
    }

    /**
     * (페이징) 상품 목록 조회
     *
     * @param pageable 페이지, 사이즈 정보
     * @return 페이징된 상품 목록 응답 DTO
     */
    @Transactional(readOnly = true)

    public Page<ItemGetListResponse> itemGetAll(Pageable pageable) {

        //상품 목록 조회
        Page<Item> result = itemRepository.findAll(pageable);

        //맵 사용하여 엔티티 목록 -> 응답 DTO 목록으로 조회
        return result.map(itemDto -> ItemGetListResponse.from(itemDto));
    }

    /**
     * 상품 수정 기능
     * @param authUser 로그인한 사용자 정보
     * @param id 수정하려는 상품 ID
     * @param request 수정할 상품 정보(이름, 희망가격, 설명, 이미지)
     * @return 수정된 상품 정보를 담은 응답 DTO
     */
    @Transactional

    public ItemUpdateResponse itemUpdated(AuthUser authUser, Long id, ItemUpdateRequest request) {

        //상품 조회 - 존재 안할 시 404에러
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_ITEM));

        //상품 작성자와 로그인한 회원이 다르면 수정 불가
        if (!item.getUser().getId().equals(authUser.getId())) {
            throw new CustomException(ExceptionCode.ACCESS_DENIED_EXCEPTION_UPDATED_ITEM);
        }

        //수정메소드 불러오기
        item.Update(request.getName(), request.getHopePrice(), request.getDescription(), request.getImage());

        //수정 결과 DTO로 변환하여 반환
        return ItemUpdateResponse.from(item);
    }

    /**
     * 상품 삭제 기능
     * @param id 삭제하려는 상품 ID
     * @param authUser 로그안한 사용자 정보
     */
    @Transactional
    public void itemDeleted(Long id, AuthUser authUser) {
        //삭제 대상 상품 조회
        Item item = itemRepository.findById(id)

                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_ITEM));

        // 상품 작성자와 로그인한 회원 정보 다르면 삭제 불가
        if (!item.getUser().getId().equals(authUser.getId())) {

            throw new CustomException(ExceptionCode.ACCESS_DENIED_EXCEPTION_DELETED_ITEM);
        }

        //소프트 삭제
        item.softDelete();
    }
}
