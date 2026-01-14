package com.example.quicksells.domain.item.controller;

import com.example.quicksells.common.model.CommonResponse;
import com.example.quicksells.common.model.PageResponse;
import com.example.quicksells.domain.item.dto.dto.ItemDto;
import com.example.quicksells.domain.item.dto.request.ItemCreatedRequest;
import com.example.quicksells.domain.item.dto.response.ItemCreatedResponse;
import com.example.quicksells.domain.item.dto.response.ItemGetDetailResponse;
import com.example.quicksells.domain.item.dto.response.ItemGetListResponse;
import com.example.quicksells.domain.item.entity.Item;
import com.example.quicksells.domain.item.service.ItemService;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")

public class ItemController {
    private final ItemService itemService;

    /**
     * 상품 등록 API
     *
     * @param userId
     * @param request
     * @return
     */
    @PostMapping("/items/{userId}")
    public ResponseEntity<CommonResponse> itemCreatedApi(

            @PathVariable Long userId,//로그인 적용 시 삭제 예정

            @RequestBody ItemCreatedRequest request) {

        //비지니스 핵심 로직
        ItemCreatedResponse responseDto = itemService.itemCreated(userId, request);

        //DTO 반환
        CommonResponse response = CommonResponse.success("상품 등록 됐습니다.", responseDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/items/{itemId}")
    public ResponseEntity<CommonResponse> itemGetDetailApi(@PathVariable Long itemId) {

        ItemGetDetailResponse responseDto = itemService.itemGetDetail(itemId);

        CommonResponse response = CommonResponse.success("삼품이 조회됐습니다.", responseDto);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //페이징
    @GetMapping("/items")
    public ResponseEntity<PageResponse> itemGetListApi(

            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {

        Page<ItemGetListResponse> responseDto = itemService.itemGetAll(pageable);

        PageResponse itemList = PageResponse.success("삼품이 전체 조회했습니다.", responseDto);

        return ResponseEntity.status(HttpStatus.OK).body(itemList);
    }
}
