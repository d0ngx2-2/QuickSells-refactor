package com.example.quicksells.domain.item.service;

import com.example.quicksells.common.enums.ExceptionCode;
import com.example.quicksells.common.exception.CustomException;
import com.example.quicksells.domain.appraise.repository.AppraiseRepository;
import com.example.quicksells.domain.auth.model.dto.AuthUser;
import com.example.quicksells.domain.item.dto.request.ItemCreatedRequest;
import com.example.quicksells.domain.item.dto.response.ItemCreatedResponse;
import com.example.quicksells.domain.item.dto.response.ItemGetDetailResponse;
import com.example.quicksells.domain.item.dto.response.ItemGetListResponse;
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
    private final AppraiseRepository appraiseRepository;

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
        User seller = userRepository.findByIdAndIsDeletedFalse(authUser.getId())
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));

        //중복 상품 검증
        boolean exists = itemRepository.existsByUserIdAndNameAndIsDeletedFalse(authUser.getId(), request.getName());

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
     * @param itemId 상품 조회 ID
     * @return 상품 상세 정보 응답 DTO
     */
    @Transactional(readOnly = true)

    public ItemGetDetailResponse itemGetDetail(Long itemId) {

        //상품 조회 및 검증 404에러
        Item item = itemRepository.findByIdAndIsDeletedFalse(itemId)
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
        Page<Item> result = itemRepository.findAllByIsDeletedFalse(pageable);

        //맵 사용하여 엔티티 목록 -> 응답 DTO 목록으로 조회
        return result.map(itemDto -> ItemGetListResponse.from(itemDto));
    }
}
