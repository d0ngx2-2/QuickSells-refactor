package com.example.quicksells.domain.item.controller;

import com.example.quicksells.common.model.CommonResponse;
import com.example.quicksells.common.model.PageResponse;
import com.example.quicksells.domain.auth.model.dto.AuthUser;
import com.example.quicksells.domain.item.model.request.ItemCreatedRequest;
import com.example.quicksells.domain.item.model.request.ItemUpdateRequest;
import com.example.quicksells.domain.item.model.response.ItemCreatedResponse;
import com.example.quicksells.domain.item.model.response.ItemGetDetailResponse;
import com.example.quicksells.domain.item.model.response.ItemGetListResponse;
import com.example.quicksells.domain.item.model.response.ItemUpdateResponse;
import com.example.quicksells.domain.item.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "상품(item) 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")

public class ItemController {
    private final ItemService itemService;

    /**
     * 상품 등록 API
     *
     * @param authUser
     * @param request
     * @return
     */
    @Operation(summary = "상품 등록")
    @PostMapping("/items")
    public ResponseEntity<CommonResponse> itemCreatedApi(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestPart(value = "request") ItemCreatedRequest request, MultipartFile image) { //(value = "image", required = false) -> 이미지가 없으면 400에러 발생하는데 글이 없이도 사용 가능하게 하는 로직

        //생성 비지니스 핵심 로직
        ItemCreatedResponse response = itemService.itemCreated(authUser, request, image);

        //201 상태 코드 반환
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success("상품 등록 됐습니다.", response));
    }

    /**
     * 상품 상세 조회 API
     *
     * @param id
     * @return
     */
    @Operation(summary = "상품 상세 조회")
    @GetMapping("/items/{id}")
    public ResponseEntity<CommonResponse> itemGetDetailApi(@PathVariable Long id) {

        //상세 조회 비지니스 핵심 로직
        ItemGetDetailResponse response = itemService.itemGetDetail(id);

        //200 상태 코드 반환
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("상품이 조회됐습니다.", response));
    }

    /**
     * 페이징 적용한 목록 조회
     *
     * @param pageable
     * @return
     */
    @Operation(summary = "상품 전체 조회")
    @GetMapping("/items")
    public ResponseEntity<PageResponse> itemGetListApi(@PageableDefault(page = 0, size = 10) Pageable pageable) {

        //페이징 처리된 상품 목록 조회 로직
        Page<ItemGetListResponse> itemList = itemService.itemGetAll(pageable);

        //200 상태 코드 반환
        return ResponseEntity.status(HttpStatus.OK).body(PageResponse.success("상품 목록 조회됐습니다.", itemList));
    }

    /**
     * 상품 수정 API
     *
     * @param authUser 사용자 정보
     * @param id       수정하려는 상품 ID
     * @param request  수정할 상품 정보(이름, 가격, 설명, 이미지)
     * @return 수정된 상품 정보 담은 응답 객체
     */
    @Operation(summary = "상품 수정")
    @PutMapping("/items/{id}")
    public ResponseEntity<CommonResponse> itemUpdatedApi(@AuthenticationPrincipal AuthUser authUser, @PathVariable Long id, @Valid @RequestPart("request") ItemUpdateRequest request, @RequestPart(value = "image", required = false) MultipartFile image) {

        //비지니스 로직
        ItemUpdateResponse response = itemService.itemUpdated(authUser, id, request, image);

        //200 상태 코드 반환
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("상품이 수정됐습니다.", response));
    }

    /**
     * 상품 삭제 API
     *
     * @param id       삭제하려는 상품 ID
     * @param authUser 로그인한 사용자 정보
     * @return 삭제 성공 메세지를 담은 공통 응답 객체
     */
    @Operation(summary = "상품 삭제")
    @DeleteMapping("/items/{id}")
    public ResponseEntity<CommonResponse> itemDeletedApi(@PathVariable Long id, @AuthenticationPrincipal AuthUser authUser) {

        //비지니스 로직
        itemService.itemDeleted(id, authUser);

        //반환
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("상품이 삭제됐습니다."));
    }
}
