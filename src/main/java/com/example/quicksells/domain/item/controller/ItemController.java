package com.example.quicksells.domain.item.controller;

import com.example.quicksells.common.model.CommonResponse;
import com.example.quicksells.common.model.PageResponse;
import com.example.quicksells.domain.auth.model.dto.AuthUser;
import com.example.quicksells.domain.item.dto.request.ItemCreatedRequest;
import com.example.quicksells.domain.item.dto.request.ItemUpdateRequest;
import com.example.quicksells.domain.item.dto.response.ItemCreatedResponse;
import com.example.quicksells.domain.item.dto.response.ItemGetDetailResponse;
import com.example.quicksells.domain.item.dto.response.ItemGetListResponse;
import com.example.quicksells.domain.item.dto.response.ItemUpdateResponse;
import com.example.quicksells.domain.item.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")

public class ItemController {
    private final ItemService itemService;

    /**
     * 상품 등록 API
     * @param authUser
     * @param request
     * @return
     */
    @PostMapping("/items")
    public ResponseEntity<CommonResponse> itemCreatedApi(@AuthenticationPrincipal AuthUser authUser,@Valid @RequestBody ItemCreatedRequest request) {

        //생성 비지니스 핵심 로직
        ItemCreatedResponse responseDto = itemService.itemCreated(authUser, request);

        //공통 응답 포맷 적용 후 DTO 반환
        CommonResponse response = CommonResponse.success("상품 등록 됐습니다.", responseDto);

        //201 상태 코드 반환
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 상품 상세 조회 API
     * @param id
     * @return
     */
    @GetMapping("/items/{id}")
    public ResponseEntity<CommonResponse> itemGetDetailApi(@PathVariable Long id) {

        //상세 조회 비지니스 핵심 로직
        ItemGetDetailResponse responseDto = itemService.itemGetDetail(id);

        //공통 응답 포맷 적용 후 DTO 반환
        CommonResponse response = CommonResponse.success("삼품이 조회됐습니다.", responseDto);

        //200 상태 코드 반환
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 페이징 적용한 목록 조회
     * @param pageable
     * @return
     */
    @GetMapping("/items")
    public ResponseEntity<PageResponse> itemGetListApi(@PageableDefault(page = 0, size = 10) Pageable pageable) {

        //페이징 처리된 상품 목록 조회 로직
        Page<ItemGetListResponse> responseDto = itemService.itemGetAll(pageable);

        //공통 응답 포맷 적용 후 DTO 반환
        PageResponse itemList = PageResponse.success("삼품이 전체 조회했습니다.", responseDto);

        //200 상태 코드 반환
        return ResponseEntity.status(HttpStatus.OK).body(itemList);
    }

    /**
     * 상품 수정 API
     * @param authUser 사용자 정보
     * @param itemId 수정하려는 상품 ID
     * @param request 수정할 상품 정보(이름, 가격, 설명, 이미지)
     * @return 수정된 상품 정보 담은 응답 객체
     */
    @PutMapping("/items/{id}")
    public ResponseEntity<CommonResponse> itemUpdatedApi(@AuthenticationPrincipal AuthUser authUser,@PathVariable Long itemId, @Valid @RequestBody ItemUpdateRequest request){

        //비지니스 로직
        ItemUpdateResponse responseDto = itemService.itemUpdated(authUser,itemId, request);

        //공통 응답 포맷 적용 후 DTO 반환
        CommonResponse response = CommonResponse.success("상품 수정 완료됐습니다.", responseDto);

        //200 상테 코드 반환
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 상품 삭제 API
     * @param itemId 삭제하려는 상품 ID
     * @param authUser 로그인한 사용자 정보
     * @return 삭제 성공 메세지를 담은 공통 응답 객체
     */
    @DeleteMapping("/items/{id}")
    public ResponseEntity<CommonResponse> itemDeletedApi(@PathVariable Long itemId, @AuthenticationPrincipal AuthUser authUser){

        //비지니스 로직
        itemService.itemDeleted(itemId, authUser);

        // 성공 응답 생성
        CommonResponse response =CommonResponse.success("상품이 삭제 됐습니다.",null);

        //반환
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
