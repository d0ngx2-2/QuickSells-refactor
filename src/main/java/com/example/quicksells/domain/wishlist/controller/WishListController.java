package com.example.quicksells.domain.wishlist.controller;

import com.example.quicksells.common.model.CommonResponse;
import com.example.quicksells.domain.wishlist.model.request.WishListCreateRequest;
import com.example.quicksells.domain.wishlist.model.response.WishListCreateResponse;
import com.example.quicksells.domain.wishlist.service.WishListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "관심 목록(wish_list) 관리")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class WishListController {

    private final WishListService wishListService;

    @Operation(summary = "관심 목록 등록")
    @PostMapping("/wishList")
    public ResponseEntity<CommonResponse> createWishList(@RequestBody WishListCreateRequest request) {

        WishListCreateResponse result = wishListService.saveWishList(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success("관심 목록 등록에 성공했습니다.", result));
    }
}
