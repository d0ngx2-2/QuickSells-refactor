package com.example.quicksells.domain.item.controller;

import com.example.quicksells.common.model.CommonResponse;
import com.example.quicksells.domain.item.dto.request.ItemCreatedRequest;
import com.example.quicksells.domain.item.dto.response.ItemCreatedResponse;
import com.example.quicksells.domain.item.service.ItemService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/item")

public class ItemController {
    private final ItemService itemService;

    /**
     * 상품 등록 API
     * @param userId
     * @param request
     * @return
     */
    @PostMapping("/{userId}")
    public ResponseEntity<CommonResponse> itemCreatedApi(
            @PathVariable Long userId,//로그인 적용 시 삭제 예정
            @RequestBody ItemCreatedRequest request) {

        //비지니스 핵심 로직
        ItemCreatedResponse responseDto = itemService.itemCreated(userId, request);

        //DTO 반환
        CommonResponse response = CommonResponse.success("상품 등록 됐습니다.", responseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
