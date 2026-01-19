package com.example.quicksells.domain.wishlist.model.request;

import lombok.Getter;

@Getter
public class WishListCreateRequest {

    private Long userId;
    private Long itemId;
}
