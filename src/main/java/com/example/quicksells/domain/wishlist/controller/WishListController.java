package com.example.quicksells.domain.wishlist.controller;

import com.example.quicksells.common.model.CommonResponse;
import com.example.quicksells.domain.wishlist.model.request.WishListCreateRequest;
import com.example.quicksells.domain.wishlist.model.response.WishListCreateResponse;
import com.example.quicksells.domain.wishlist.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class WishListController {

    private final WishListService wishListService;

    @PostMapping("/wishList")
    public ResponseEntity<CommonResponse> createWishList(@RequestBody WishListCreateRequest request) {

        WishListCreateResponse result = wishListService.saveWishList(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success("관심 목록 등록에 성공했습니다.", result));
    }
}
